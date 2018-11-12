/*
 * Copyright 2014-2018 Rudy De Busscher (https://www.atbash.be)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.atbash.util.resource;

import be.atbash.util.PublicAPI;
import be.atbash.util.StringUtils;
import be.atbash.util.ordered.OrderComparator;
import be.atbash.util.resource.internal.ClassPathResourceReader;
import be.atbash.util.resource.internal.FileResourceReader;
import be.atbash.util.resource.internal.URLResourceReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@PublicAPI
public class ResourceUtil {

    /**
     * Resource path prefix that specifies to load from a file location, value is <b>{@code file:}</b>
     */
    public static final String FILE_PREFIX = "file:";

    /**
     * Resource path prefix that specifies to load from a url location, value is <b>{@code url:}</b>
     */
    public static final String URL_PREFIX = "url:";

    /**
     * Resource path prefix that specifies to load from a classpath location, value is <b>{@code classpath:}</b>
     */
    public static final String CLASSPATH_PREFIX = "classpath:";


    private static final Object LOCK = new Object();
    private static ResourceUtil INSTANCE;

    private List<ResourceReader> readers;

    private ResourceUtil() {
        readers = new ArrayList<>();
        readers.add(new ClassPathResourceReader());
        readers.add(new URLResourceReader());
        readers.add(new FileResourceReader());
        // current Thread
        for (ResourceReader resourceReader : ServiceLoader.load(ResourceReader.class)) {
            readers.add(resourceReader);
        }
        // Classloader for deployment
        for (ResourceReader resourceReader : ServiceLoader.load(ResourceReader.class, ResourceUtil.class.getClassLoader())) {
            if (!isReaderFound(resourceReader)) {
                readers.add(resourceReader);
            }
        }
        Collections.sort(readers, new OrderComparator());
    }

    private boolean isReaderFound(ResourceReader resourceReader) {
        boolean result = false;
        for (ResourceReader reader : readers) {
            if (reader.getClass().getName().equals(resourceReader.getClass().getName())) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Returns {@code true} if the resource can be read by an implementation (based on the prefix in most cases)
     *
     * @param resourcePath
     * @return
     */
    public boolean isSupported(String resourcePath) {
        return isSupported(resourcePath, null);
    }

    /**
     * Returns {@code true} if the resource can be read by an implementation (based on the prefix in most cases)
     *
     * @param resourcePath
     * @param context      Optional value defining the context (like servletContext) from which resource must be read
     * @return
     */
    public boolean isSupported(String resourcePath, Object context) {
        if (StringUtils.isEmpty(resourcePath)) {
            // When null or empty, we can't read it!
            return false;
        }
        boolean result = false;
        Iterator<ResourceReader> iterator = getInstance().readers.iterator();
        while (!result && iterator.hasNext()) {
            result = iterator.next().canRead(resourcePath, context);
        }
        return result;
    }

    /**
     * Returns {@code true} if the resource at the specified path exists, {@code false} otherwise.  This
     * method supports scheme prefixes on the path.
     *
     * @param resourcePath the path of the resource to check.
     * @return {@code true} if the resource at the specified path exists, {@code false} otherwise.
     */
    public boolean resourceExists(String resourcePath) {
        return resourceExists(resourcePath, null);
    }

    /**
     * Returns {@code true} if the resource at the specified path exists, {@code false} otherwise.  This
     * method supports scheme prefixes on the path.
     *
     * @param resourcePath the path of the resource to check.
     * @param context      Optional value defining the context (like servletContext) from which resource must be read
     * @return {@code true} if the resource at the specified path exists, {@code false} otherwise.
     */
    public boolean resourceExists(String resourcePath, Object context) {
        boolean result = false;
        Iterator<ResourceReader> iterator = getInstance().readers.iterator();
        while (!result && iterator.hasNext()) {
            result = iterator.next().exists(resourcePath, context);
        }
        return result;

    }

    /**
     * Returns the InputStream for the resource represented by the specified path, supporting scheme
     * prefixes that direct how to acquire the input stream
     * ({@link #CLASSPATH_PREFIX CLASSPATH_PREFIX},
     * {@link #URL_PREFIX URL_PREFIX}, or {@link #FILE_PREFIX FILE_PREFIX}).  If the path is not prefixed by one
     * of these schemes, the path is assumed to be a file-based path that can be loaded with a
     * {@link FileInputStream FileInputStream}.
     *
     * @param path the String path representing the resource to obtain.
     * @return the InputStraem for the specified resource.
     * @throws IOException if there is a problem acquiring the resource at the specified path.
     */
    public InputStream getStream(String path) throws IOException {
        return getStream(path, null);
    }

    /**
     * Returns the InputStream for the resource represented by the specified path, supporting scheme
     * prefixes that direct how to acquire the input stream
     * ({@link #CLASSPATH_PREFIX CLASSPATH_PREFIX},
     * {@link #URL_PREFIX URL_PREFIX}, or {@link #FILE_PREFIX FILE_PREFIX}).  If the path is not prefixed by one
     * of these schemes, the path is assumed to be a file-based path that can be loaded with a
     * {@link FileInputStream FileInputStream}.
     *
     * @param path    the String path representing the resource to obtain.
     * @param context Optional value defining the context (like servletContext) from which resource must be read
     * @return the InputStraem for the specified resource.
     * @throws IOException if there is a problem acquiring the resource at the specified path.
     */
    public InputStream getStream(String path, Object context) throws IOException {

        InputStream result = null;
        if (StringUtils.hasText(path)) {
            Iterator<ResourceReader> iterator = getInstance().readers.iterator();
            while (result == null && iterator.hasNext()) {
                ResourceReader reader = iterator.next();
                if (reader.canRead(path, context)) {
                    result = reader.load(path, context);
                }
            }

        }
        return result;

    }

    public static ResourceUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new ResourceUtil();
                }
            }
        }
        return INSTANCE;
    }
}

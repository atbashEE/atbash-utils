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
package be.atbash.util.resource.internal;

import be.atbash.util.exception.AtbashUnexpectedException;
import be.atbash.util.resource.ResourceWalkerExecutorServiceProvider;
import be.atbash.util.resource.internal.vfs.Vfs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.lang.String.format;

// Based on org.reflections Reflection
public class ResourceWalker {

    private Logger logger = LoggerFactory.getLogger(ResourceWalker.class);

    private ExecutorService executorService;

    private Store store;

    public ResourceWalker(Store store) {
        this.store = store;

        Iterator<ResourceWalkerExecutorServiceProvider> providerIterator = ServiceLoader.load(ResourceWalkerExecutorServiceProvider.class).iterator();
        if (providerIterator.hasNext()) {
            // TODO What if there are more then 1 defined
            executorService = providerIterator.next().getExecutorService();
        }
    }

    public void scan() {
        long time = System.currentTimeMillis();
        int scannedUrls = 0;

        List<Future<?>> futures = new ArrayList<>();

        Collection<URL> urls = getClassPathURLs();

        for (final URL url : urls) {
            try {
                if (executorService != null) {

                    futures.add(executorService.submit(new Runnable() {
                        public void run() {
                            logger.debug(String.format("[%s] scanning %s", Thread.currentThread(), url));
                            scan(url);
                        }
                    }));

                } else {

                    scan(url);
                }
                scannedUrls++;
            } catch (ResourceWalkerException e) {
                logger.warn("could not create Vfs.Dir from url. Ignoring the exception and continuing", e);
            }

        }


        if (executorService != null) {
            for (Future future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    throw new AtbashUnexpectedException(e);
                }
            }
        }

        time = System.currentTimeMillis() - time;

        int keys = store.keySet().size();
        int values = 0;
        for (String index : store.keySet()) {
            values += store.get(index).size();
        }

        logger.info(format("Reflections took %d ms to scan %d urls, producing %d keys and %d values %s",
                time, scannedUrls, keys, values,
                executorService != null ? "[using executorService]" : ""));

    }

    private void scan(URL url) {
        Vfs.Dir dir = Vfs.fromURL(url);

        if (dir == null) {
            // Some URL's don't need to be scanned.
            return;
        }

        try {
            for (Vfs.File file : dir.getFiles()) {
                // scan if inputs filter accepts file relative path or fqn
                String path = file.getRelativePath();

                if (!path.endsWith(".class") && !path.startsWith("META-INF/")) {  // TODO System parameter to included those also.
                    //not a class
                    store.put(path, url.toExternalForm());
                }
            }
        } finally {
            dir.close();
        }
    }

    /**
     * Gets the current thread context class loader.
     * {@code Thread.currentThread().getContextClassLoader()}.
     *
     * @return the context class loader, may be null
     */
    private ClassLoader contextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * Gets the class loader of this library.
     * {@code Reflections.class.getClassLoader()}.
     *
     * @return the static library class loader, may be null
     */
    private ClassLoader staticClassLoader() {
        return ResourceWalker.class.getClassLoader();
    }

    private ClassLoader[] classLoaders() {

        ClassLoader contextClassLoader = contextClassLoader();
        ClassLoader staticClassLoader = staticClassLoader();
        return contextClassLoader != null ?
                staticClassLoader != null && contextClassLoader != staticClassLoader ?
                        new ClassLoader[]{contextClassLoader, staticClassLoader} :
                        new ClassLoader[]{contextClassLoader} :
                new ClassLoader[]{};

    }

    /**
     * Returns a distinct collection of URLs based on a resource.
     * <p>
     * This searches for the resource name, using {@link ClassLoader#getResources(String)}.
     * For example, {@code forResource(test.properties)} effectively returns URLs from the
     * classpath containing files of that name.
     * <p>
     * If the optional {@link ClassLoader}s are not specified, then both {@link #contextClassLoader()}
     * and {@link #staticClassLoader()} are used for {@link ClassLoader#getResources(String)}.
     * <p>
     * The returned URLs retains the order of the given {@code classLoaders}.
     *
     * @return the collection of URLs, not null
     */
    private Collection<URL> getClassPathURLs() {
        List<URL> result = new ArrayList<>();
        ClassLoader[] loaders = classLoaders();
        for (ClassLoader classLoader : loaders) {
            try {
                // Finds entries in the classpath which points to a directory
                Enumeration<URL> urls = classLoader.getResources("");
                addFoundURLs(result, urls);
                // Most JARs have manifest file, and thus find the JAR entries on the classpath
                urls = classLoader.getResources("META-INF/MANIFEST.MF");
                addFoundURLs(result, urls);
            } catch (IOException e) {
                logger.error("error getting resources  ", e);

            }
        }
        return distinctUrls(result);
    }

    private void addFoundURLs(List<URL> result, Enumeration<URL> urls) {
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();

            result.add(url);

        }
    }

    private static Collection<URL> distinctUrls(Collection<URL> urls) {
        Map<String, URL> distinct = new LinkedHashMap<>(urls.size());
        for (URL url : urls) {
            distinct.put(url.toExternalForm(), url);
        }
        return distinct.values();
    }
}

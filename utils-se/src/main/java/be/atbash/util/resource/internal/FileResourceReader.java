/*
 * Copyright 2014-2020 Rudy De Busscher (https://www.atbash.be)
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

import be.atbash.util.ordered.Order;
import be.atbash.util.resource.ResourceReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import static be.atbash.util.resource.ResourceUtil.*;

@Order(Long.MAX_VALUE)
public class FileResourceReader implements ResourceReader {

    private static final Logger LOG = LoggerFactory.getLogger(FileResourceReader.class);


    @Override
    public boolean canRead(String resourcePath, Object context) {
        // Pro forma. This is the last reader which is checked and we assume that it is not classpath, URL or custom defined and thus file.
        String lowerCasePath = resourcePath.toLowerCase(Locale.ENGLISH);

        return !resourcePath.startsWith(CLASSPATH_PREFIX) && !resourcePath.startsWith(URL_PREFIX) &&
                !lowerCasePath.startsWith(URLResourceReader.HTTP) && !lowerCasePath.startsWith(URLResourceReader.HTTPS);
    }

    @Override
    public boolean exists(String resourcePath, Object context) {
        //We don't try if it can be read, see also the canRead comment.
        String path = stripPrefix(resourcePath);
        File file = new File(path);
        return file.exists() && file.canRead();
    }

    @Override
    public InputStream load(String resourcePath, Object context) throws IOException {
        String path = stripPrefix(resourcePath);
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Opening file [%s] ...", path));
        }
        return new FileInputStream(path);

    }

    private static String stripPrefix(String resourcePath) {
        String lowerCasePath = resourcePath.toLowerCase(Locale.ENGLISH);
        if (!lowerCasePath.startsWith(FILE_PREFIX)) {
            return resourcePath;
        }
        return resourcePath.substring(resourcePath.indexOf(':') + 1);
    }
}

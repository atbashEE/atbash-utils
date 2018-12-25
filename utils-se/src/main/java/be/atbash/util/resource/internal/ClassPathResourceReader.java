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

import be.atbash.util.ordered.Order;
import be.atbash.util.reflection.ClassUtils;
import be.atbash.util.resource.ResourceReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import static be.atbash.util.resource.ResourceUtil.CLASSPATH_PREFIX;

@Order(-50)
public class ClassPathResourceReader implements ResourceReader {

    private static final Logger LOG = LoggerFactory.getLogger(ClassPathResourceReader.class);


    @Override
    public boolean canRead(String resourcePath, Object context) {
        return resourcePath.startsWith(CLASSPATH_PREFIX);
    }

    @Override
    public boolean exists(String resourcePath, Object context) {
        boolean result = canRead(resourcePath, context);
        if (!result) {
            return false;
        }

        try {
            InputStream is = load(resourcePath, context);
            if (is == null) {
                result = false;
            } else {
                is.close();
            }
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    @Override
    public InputStream load(String resourcePath, Object context) throws IOException {
        if (!canRead(resourcePath, context)) {
            return null;
        }
        String cleanedPath = stripPrefix(resourcePath);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Opening resource from class path [{}]", cleanedPath);
        }
        InputStream result = ClassUtils.getResourceAsStream(cleanedPath);
        if (result == null && cleanedPath.startsWith("/")) {
            String newPath = cleanedPath.substring(1);
            result = load(newPath, context);
        }
        return result;
    }

    private static String stripPrefix(String resourcePath) {
        return resourcePath.substring(resourcePath.indexOf(':') + 1);
    }

}

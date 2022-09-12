/*
 * Copyright 2014-2022 Rudy De Busscher (https://www.atbash.be)
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static be.atbash.util.resource.ResourceUtil.URL_PREFIX;

@Order(-20)
public class URLResourceReader implements ResourceReader {

    private static final Logger LOG = LoggerFactory.getLogger(ClassPathResourceReader.class);

    static final String HTTP = "http:";
    static final String HTTPS = "https:";
    static final String JAR = "jar:";


    @Override
    public boolean canRead(String resourcePath, Object context) {
        String lowerCasePath = resourcePath.toLowerCase(Locale.ENGLISH);
        return resourcePath.startsWith(URL_PREFIX)
                || lowerCasePath.startsWith(HTTP)
                || lowerCasePath.startsWith(HTTPS)
                || lowerCasePath.startsWith(JAR);
    }

    @Override
    public boolean exists(String resourcePath, Object context) {
        boolean result = canRead(resourcePath, context);
        if (!result) {
            return false;
        }

        try {
            load(resourcePath, context);
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
        String urlPath = stripPrefix(resourcePath);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Opening url {}", urlPath);
        }
        URL url = new URL(urlPath);
        return url.openStream();

    }

    @Override
    public List<URI> getResources(String resourcePath) {
        if (!canRead(resourcePath, null)) {
            return Collections.emptyList();
        }
        List<URI> result = new ArrayList<>();
        String urlPath = stripPrefix(resourcePath);
        try {
            URL url = new URL(urlPath);
            result.add(url.toURI());
        } catch (MalformedURLException | URISyntaxException e) {
            LOG.warn("Invalid URL {}", urlPath);
        }
        return result;

    }

    private static String stripPrefix(String resourcePath) {
        String lowerCasePath = resourcePath.toLowerCase(Locale.ENGLISH);
        if (lowerCasePath.startsWith(HTTP) || lowerCasePath.startsWith(HTTPS) || lowerCasePath.startsWith(JAR)) {
            return resourcePath;
        }
        return resourcePath.substring(resourcePath.indexOf(':') + 1);
    }
}

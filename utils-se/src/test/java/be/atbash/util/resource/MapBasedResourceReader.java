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

import be.atbash.util.ordered.Order;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Order(50)
public class MapBasedResourceReader implements ResourceReader {

    @Override
    public boolean canRead(String resourcePath, Object context) {
        return context instanceof Map;
    }

    @Override
    public boolean exists(String resourcePath, Object context) {
        boolean result = canRead(resourcePath, context);
        if (result) {
            Map<String, String> data = (Map<String, String>) context;
            result = data.containsKey(resourcePath);
        }
        return result;
    }

    @Override
    public InputStream load(String resourcePath, Object context) throws IOException {
        if (exists(resourcePath, context)) {
            Map<String, String> data = (Map<String, String>) context;
            return new ByteArrayInputStream(data.get(resourcePath).getBytes());
        }
        return null;
    }
}

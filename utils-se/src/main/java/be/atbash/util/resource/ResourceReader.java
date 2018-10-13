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

import java.io.IOException;
import java.io.InputStream;

@PublicAPI
public interface ResourceReader {

    /**
     * Determines if the implementation can read the resource based on the prefix.
     *
     * @param resourcePath Must be a non value containing the path pointing to the resource.
     * @param context Optional value defining the context (like servletContext) from which resource must be read.
     * @return
     */
    boolean canRead(String resourcePath, Object context);

    /**
     * Determines if the resource exists and can be read.
     *
     * @param resourcePath
     * @param context Optional value defining the context (like servletContext) from which resource must be checked.
     * @return
     */
    boolean exists(String resourcePath, Object context);

    /**
     * Loads the resource.
     * @param resourcePath
     * @param context Optional value defining the context (like servletContext) from which resource must be loaded.
     * @return null if the implementation can't read the resource (based on the prefix).
     */
    InputStream load(String resourcePath, Object context) throws IOException;
}

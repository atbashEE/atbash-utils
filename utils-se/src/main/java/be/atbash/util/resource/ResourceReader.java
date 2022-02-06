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
package be.atbash.util.resource;

import be.atbash.util.PublicAPI;
import be.atbash.util.SecurityReview;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

@PublicAPI
public interface ResourceReader {

    /**
     * Determines if the implementation can read the resource based on the prefix.
     *
     * @param resourcePath Must be a non empty value containing the path pointing to the resource.
     * @param context      Optional value defining the context (like servletContext) from which resource must be read.
     * @return true when resource can be read.
     */
    boolean canRead(String resourcePath, Object context);

    /**
     * Determines if the resource exists and can be read.
     *
     * @param resourcePath Must be a non empty value containing the path pointing to the resource.
     * @param context      Optional value defining the context (like servletContext) from which resource must be checked.
     * @return true when the resource exists.
     */
    boolean exists(String resourcePath, Object context);

    /**
     * Loads the resource.
     * Security check : Make sure that the resourcePath is controlled by the developer so that no unwanted files are read.
     * @param resourcePath Must be a non empty value containing the path pointing to the resource.
     * @param context      Optional value defining the context (like servletContext) from which resource must be loaded.
     * @return null if the implementation can't read the resource (based on the prefix).
     */
    @SecurityReview
    InputStream load(String resourcePath, Object context) throws IOException;

    /**
     * Determine the URI for the resource. This could be multiple as a resource file may be present in multiple JAR files on the classpath.
     * The returned list might be empty when no resource with that name is found or when the {@link ResourceReader} doesn't
     * support representation to a URI.
     * @param resourcePath Must be a non empty value containing the path pointing to the resource.
     * @return List of URI for the resource or empty List.
     */
    List<URI> getResources(String resourcePath);
}

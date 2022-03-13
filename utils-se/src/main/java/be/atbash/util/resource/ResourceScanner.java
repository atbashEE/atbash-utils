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
import be.atbash.util.resource.internal.ResourceWalker;
import be.atbash.util.resource.internal.Store;
import be.atbash.util.resource.internal.vfs.Vfs;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@PublicAPI
public class ResourceScanner {

    private static ResourceScanner instance;

    private final Store store;

    private ResourceScanner() {
        store = new Store();
        new ResourceWalker(store).scan();
    }

    /**
     * Returns all resources which matches the Regular Expression Pattern.
     * Security Review : Make sure the Pattern is defined by the developer since some regular expression can be
     * very time consuming and thus used by user to trigger a DOS.
     * @param resourceName The Regular expression pattern to resources much match
     * @return The resources matching the regular expression
     */
    @SecurityReview
    public Set<String> getResources(String resourceName) {
        Pattern pattern = Pattern.compile(resourceName);
        return getResources(pattern);
    }

    /**
     * Returns all resources which matches the Regular Expression Pattern.
     * Security Review : Make sure the Pattern is defined by the developer since some regular expression can be
     * very time consuming and thus used by user to trigger a DOS.
     * @param pattern The Regular expression pattern to resources much match
     * @return The resources matching the regular expression
     */
    @SecurityReview
    public Set<String> getResources(Pattern pattern) {

        Set<String> result = new HashSet<>();

        for (String key : store.keySet()) {
            if (pattern.matcher(key).matches()) {
                result.add(key);
            }
        }
        return result;
    }

    /**
     * Returns all resource locations which matches the Regular Expression Pattern.
     * Security Review : Make sure the Pattern is defined by the developer since some regular expression can be
     * very time consuming and thus used by user to trigger a DOS.
     * @param resourceName The Regular expression pattern to resources much match
     * @return The locations of the resources matching the regular expression
     */
    @SecurityReview
    public Set<String> getResourcePaths(String resourceName) {
        Pattern pattern = Pattern.compile(resourceName);
        return getResourcePaths(pattern);
    }

    /**
     * Returns all resource locations which matches the Regular Expression Pattern.
     * Security Review : Make sure the Pattern is defined by the developer since some regular expression can be
     * very time consuming and thus used by user to trigger a DOS.
     * @param pattern The Regular expression pattern to resources much match
     * @return The locations of the resources matching the regular expression
     */
    @SecurityReview
    public Set<String> getResourcePaths(Pattern pattern) {

        Set<String> result = new HashSet<>();

        for (String key : store.keySet()) {
            if (pattern.matcher(key).matches()) {
                result.addAll(store.get(key));
            }
        }
        return result;
    }


    public boolean existsResource(String resourceName) {
        return store.keySet().contains(resourceName);
    }

    public boolean isUniqueResource(String resourceName) {
        boolean result = existsResource(resourceName);
        if (result) {
            result = store.get(resourceName).size() == 1;
        }
        return result;
    }

    public static synchronized ResourceScanner getInstance() {
        // Synchronize methods are not so bad for performance anymore and since only 1 synchronized static there are no side effects
        if (instance == null) {
            instance = new ResourceScanner();
        }

        return instance;
    }

    public static void registerURLType(UrlType urlType) {

        Vfs.registerURLType(urlType);
    }
}

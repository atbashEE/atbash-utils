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
import be.atbash.util.resource.internal.ResourceWalker;
import be.atbash.util.resource.internal.Store;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@PublicAPI
public class ResourceScanner {

    private static final Object LOCK = new Object();

    private static ResourceScanner INSTANCE;

    private Store store;

    private ResourceScanner() {
        store = new Store();
        new ResourceWalker(store).scan();
    }

    public Set<String> getResources(Pattern pattern) {

        Set<String> result = new HashSet<>();

        for (String key : store.keySet()) {
            if (pattern.matcher(key).matches()) {
                result.add(key);
            }
        }
        return result;
    }

    public Set<String> getResourcePaths(String resourceName) {
        Pattern pattern = Pattern.compile(resourceName);
        return getResourcePaths(pattern);
    }

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

    public static ResourceScanner getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new ResourceScanner();
                }
            }
        }
        return INSTANCE;
    }
}

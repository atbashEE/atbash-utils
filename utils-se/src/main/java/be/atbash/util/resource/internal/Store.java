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

import java.util.*;

/**
 * stores metadata information.
 */
// Based on org.reflections Store
public class Store {

    //private transient boolean concurrent;  TODO Using ExecutorService to scan in parallel
    private final Map<String, List<String>> storeMap; // FIXME Review nested map.

    public Store() {
        storeMap = new HashMap<>();
    }

    /**
     * return all indices
     */
    public Set<String> keySet() {
        return storeMap.keySet();
    }

    public void put(String index, String value) {
        getOrCreate(index).add(value);
    }

    /**
     * get or create the multimap object for the given {@code key}
     */
    public List<String> getOrCreate(String key) {
        List<String> mmap = storeMap.get(key);
        if (mmap == null) {

            mmap = new ArrayList<>();
            storeMap.put(key, mmap);
        }
        return mmap;
    }

    /**
     * get the List object for the given {@code index}, otherwise throws a {@link AtbashUnexpectedException}
     */
    public List<String> get(String key) {
        List<String> mmap = storeMap.get(key);
        if (mmap == null) {
            // FIXME Review
            throw new AtbashUnexpectedException("Scanner " + key + " was not configured");
        }
        return mmap;
    }

}

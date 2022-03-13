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
package be.atbash.util;

import java.util.*;

/**
 * Static helper class for use dealing with Collections. Parts of these methods are taken from Apache Shiro.
 */
@PublicAPI
public final class CollectionUtils {

    /**
     * Singleton pattern.
     */
    private CollectionUtils() {
    }

    /**
     * Returns the items in the vararg as a {@link Set} but keeping there order by using a {@link LinkedHashSet}.
     * Items must be of the same type.
     *
     * @param elements items to be placed in Set
     * @param <E>      The type of the items and the resulting {@link Set}
     * @return The items as a modifiable Set.
     */
    @SafeVarargs
    public static <E> Set<E> asSet(E... elements) {
        if (elements == null || elements.length == 0) {
            return Collections.emptySet();
        }
        LinkedHashSet<E> set = new LinkedHashSet<>(elements.length * 4 / 3 + 1);
        Collections.addAll(set, elements);
        return set;
    }

    /**
     * Returns {@code true} if the specified {@code Collection} is {@code null} or {@link Collection#isEmpty empty},
     * {@code false} otherwise.
     *
     * @param c the collection to check
     * @return {@code true} if the specified {@code Collection} is {@code null} or {@link Collection#isEmpty empty},
     * {@code false} otherwise.
     */
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    /**
     * Returns {@code true} if the specified {@code Map} is {@code null} or {@link Map#isEmpty empty},
     * {@code false} otherwise.
     *
     * @param m the {@code Map} to check
     * @return {@code true} if the specified {@code Map} is {@code null} or {@link Map#isEmpty empty},
     * {@code false} otherwise.
     */
    public static boolean isEmpty(Map<?,?> m) {
        return m == null || m.isEmpty();
    }

    /**
     * Returns the size of the specified collection or {@code 0} if the collection is {@code null}.
     *
     * @param c the collection to check
     * @return the size of the specified collection or {@code 0} if the collection is {@code null}.
     */
    public static int size(Collection<?> c) {
        return c != null ? c.size() : 0;
    }

    /**
     * Returns the size of the specified map or {@code 0} if the map is {@code null}.
     *
     * @param m the map to check
     * @return the size of the specified map or {@code 0} if the map is {@code null}.
     */
    public static int size(Map<?,?> m) {
        return m != null ? m.size() : 0;
    }

    /**
     * Converts the vararg argument to to a {@link List} instance. The list is a regular list but the initial size is
     * computed to accomadate exactly the number of items specified by the argument.
     *
     * @param elements The elements for the list.
     * @param <E>      The type of the List items.
     * @return The elements within a {@link List} instance.
     */
    @SafeVarargs
    public static <E> List<E> asList(E... elements) {
        if (elements == null || elements.length == 0) {
            return Collections.emptyList();
        }
        // Avoid integer overflow when a large array is passed in
        int capacity = computeListCapacity(elements.length);
        List<E> list = new ArrayList<>(capacity);
        Collections.addAll(list, elements);
        return list;
    }

    public static<T> Iterable<T> iteratorToIterable(Iterator<T> iterator) {
        return () -> iterator;
    }

    static int computeListCapacity(int arraySize) {
        return (int) Math.min(5L + arraySize + (arraySize / 10), Integer.MAX_VALUE);
    }

    public static <T> List<T> unmodifiableList(List<? extends T> list) {
        return Collections.unmodifiableList(list);
    }
}

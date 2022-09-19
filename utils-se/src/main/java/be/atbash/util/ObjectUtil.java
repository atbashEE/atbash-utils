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

import java.util.Objects;
import java.util.function.Predicate;

@PublicAPI
public final class ObjectUtil {

    private ObjectUtil() {
    }


    /**
     * Throws an exception if the object is null or the predicate applied on the object is true.
     *
     * @param object      The Object to test
     * @param predicate   The predicate to test on the Object
     * @param msgToCaller Message for the IllegalArgumentException when the predicate is valid on the object.
     * @param <T>         Type of the object to test.
     * @return The original Object or IllegalArgumentException is thrown.
     */
    public static <T> T requireNot(T object, Predicate<T> predicate, String msgToCaller) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(predicate);
        if (predicate.test(object)) {
            throw new IllegalArgumentException(msgToCaller);
        }
        return object;
    }

    /**
     * Specialised version of {@code requireNot()} to test if a string is not null and is not blank.
     *
     * @param data        String to Test
     * @param msgToCaller Specific message the IllegalArgumentException when the String is empty.
     * @return The String or IllegalArgumentException is thrown.
     */
    public static String requireNotBlank(String data, String msgToCaller) {
        Objects.requireNonNull(data);

        if (data.trim().isEmpty()) {
            throw new IllegalArgumentException(msgToCaller);
        }
        return data;
    }

    /**
     * Tests if the parameter is not blank.  If null or when the String is blank, an {@link IllegalArgumentException}
     * with the standard message 'Value should not be null or blank' is thrown.
     *
     * @param object The String to test
     * @return The String in unaltered content or IllegalArgumentException is thrown.
     */
    public static String requireNotBlank(String object) {
        requireNotBlank(object, "Value should not be null or blank");
        return object;
    }
}

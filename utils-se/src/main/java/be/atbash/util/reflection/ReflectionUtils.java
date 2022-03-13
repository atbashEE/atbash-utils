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
package be.atbash.util.reflection;

import be.atbash.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static List<Field> findFields(Class<?> clazz, Predicate<Field> predicate) {

        return CollectionUtils.unmodifiableList(findAllFields(clazz).stream().filter(predicate).collect(Collectors.toList()));
    }

    private static List<Field> findAllFields(Class<?> clazz) {
        return Arrays.stream(clazz.getFields()).filter(field -> !field.isSynthetic()).collect(Collectors.toList());
    }

}

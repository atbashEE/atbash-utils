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
import be.atbash.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ReflectionUtils {

    private static final String PROPERTY_ACCESSOR_PREFIX_GET = "get";

    private static final String PROPERTY_ACCESSOR_PREFIX_RECORD = "";
    private static final String PROPERTY_ACCESSOR_PREFIX_IS = "is";
    private static final String PROPERTY_MUTATOR_PREFIX = "set";

    private ReflectionUtils() {
    }

    public static List<Field> findFields(Class<?> clazz, Predicate<Field> predicate) {

        return CollectionUtils.unmodifiableList(findAllFields(clazz).stream().filter(predicate).collect(Collectors.toList()));
    }

    private static List<Field> findAllFields(Class<?> clazz) {
        return Arrays.stream(clazz.getFields()).filter(field -> !field.isSynthetic()).collect(Collectors.toList());
    }

    /**
     * @param containingClass never null
     * @param propertyName    never null
     * @return sometimes null
     */
    public static Method getGetterMethod(Class<?> containingClass, String propertyName) {

        String propertyNameWithCapital = StringUtils.withInitialCapital(propertyName);
        String getterName = PROPERTY_ACCESSOR_PREFIX_GET + propertyNameWithCapital;
        try {
            return containingClass.getMethod(getterName);
        } catch (NoSuchMethodException e) {
            // intentionally empty
        }

        try {
            // Try property name for use with Java 14 records.
            return containingClass.getMethod(propertyName);
        } catch (NoSuchMethodException e) {
            // intentionally empty
        }


        String isserName = PROPERTY_ACCESSOR_PREFIX_IS + propertyNameWithCapital;
        try {
            Method method = containingClass.getMethod(isserName);
            if (method.getReturnType() == boolean.class) {
                return method;
            }
        } catch (NoSuchMethodException e) {
            // intentionally empty
        }
        return null;
    }

    /**
     * @param containingClass never null
     * @param fieldName       never null
     * @return sometimes null
     */
    public static Field getField(Class<?> containingClass, String fieldName) {
        try {
            return containingClass.getField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * @param containingClass never null
     * @param propertyType    never null
     * @param propertyName    never null
     * @return null if it doesn't exist
     */
    public static Method getSetterMethod(Class<?> containingClass, Class<?> propertyType, String propertyName) {
        String setterName = PROPERTY_MUTATOR_PREFIX + StringUtils.withInitialCapital(propertyName);
        try {
            return containingClass.getMethod(setterName, propertyType);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}

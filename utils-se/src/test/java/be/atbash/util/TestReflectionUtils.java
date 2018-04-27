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
package be.atbash.util;

import be.atbash.util.exception.AtbashUnexpectedException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *
 */
public final class TestReflectionUtils {

    private TestReflectionUtils() {
    }

    /**
     * Injects objects into the target (private) fields by matching the type.
     *
     * @param target       The target object where the dependencies are injected
     * @param dependencies The objects we like to set into the target.
     * @throws IllegalAccessException Should not happen since we overrule the accessibility
     */
    public static void injectDependencies(final Object target, final Object... dependencies) throws IllegalAccessException {
        Class targetClass = target.getClass();
        while (targetClass != null && targetClass != Object.class) {
            if (targetClass.getName().contains("$")) {
                targetClass = targetClass.getSuperclass();
            }
            for (Field field : targetClass.getDeclaredFields()) {
                field.setAccessible(true);
                for (Object dependency : dependencies) {
                    if (!Modifier.isFinal(field.getModifiers()) && field.getType().isAssignableFrom(dependency.getClass())) {
                        field.set(target, dependency);
                    }
                }

            }
            targetClass = targetClass.getSuperclass();
        }
    }

    /**
     * Retrieves the value of the field from the target. When the target is a class, it looks through the static fields.
     * When it is an instance it looks through the fields of this class and all parent classes. Visibility of the field doesn't matter (also private fields value can be returned)
     *
     * @param target    Class (for static fields) or instance (for regular fields)
     * @param fieldName Name of the field
     * @param <T>
     * @return Value of the field of Exception thrown when not found.
     * @throws NoSuchFieldException When no such field is found
     */
    public static <T> T getValueOf(Object target, String fieldName) throws NoSuchFieldException {
        Field field = findFieldInHierarchy(target, fieldName);

        if (field == null) {
            throw new NoSuchFieldException(String.format("Field %s not found", fieldName));
        }
        field.setAccessible(true);
        try {
            return (T) field.get(target);
        } catch (IllegalAccessException e) {
            throw new AtbashUnexpectedException(e);
        }

    }

    /**
     * Clears the value of the field from the target. Alternative for setFieldValue() with value null.
     *
     * @param target    Class (for static fields) or instance (for regular fields)
     * @param fieldName Value to set, must be assignable compatible.
     * @throws NoSuchFieldException When no such field is found
     */
    public static void resetOf(Object target, String fieldName) throws NoSuchFieldException {
        setFieldValue(target, fieldName, null);
    }

    /**
     * Sets the value of the field from the target. When the target is a class, it sets the static field.
     * When it is an instance it looks through the fields of this class and all parent classes. Visibility of the field doesn't matter (also private fields value can be set).
     *
     * @param target    Class (for static fields) or instance (for regular fields)
     * @param fieldName Name of the field
     * @param value     Value to set, must be assignable compatible.
     * @throws NoSuchFieldException When no such field is found
     */
    public static void setFieldValue(Object target, String fieldName, Object value) throws NoSuchFieldException {

        Field field = findFieldInHierarchy(target, fieldName);

        if (field == null) {
            throw new NoSuchFieldException(String.format("Field %s not found", fieldName));
        }

        field.setAccessible(true);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new AtbashUnexpectedException(e);
        }
    }

    private static Field findFieldInHierarchy(Object target, String fieldName) throws NoSuchFieldException {
        if (target instanceof Class<?>) {
            // static field
            return ((Class<?>) target).getDeclaredField(fieldName);
        }
        Class<?> targetClass = target.getClass();
        Field field = findField(targetClass, fieldName);
        while (field == null && !Object.class.equals(targetClass)) {
            targetClass = targetClass.getSuperclass();
            field = findField(targetClass, fieldName);
        }
        return field;
    }

    private static Field findField(Class<?> targetClass, String fieldName) {
        Field result = null;
        for (Field field : targetClass.getDeclaredFields()) {
            if (fieldName.equals(field.getName())) {
                result = field;
            }
        }
        return result;
    }
}

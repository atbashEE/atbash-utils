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

import be.atbash.util.exception.AtbashException;

/**
 * Runtime exception thrown by the framework when unable no Constructor was found on the class for the specified arguments.
 */
class NoConstructorFoundException extends AtbashException {

    NoConstructorFoundException(Class<?> clazz, Class<?>[] argTypes) {
        super(createExceptionMessage(clazz, argTypes));
    }

    private static String createExceptionMessage(Class<?> clazz, Class<?>[] argTypes) {
        StringBuilder result = new StringBuilder();
        result.append("No constructor or more then one found at the class ");
        result.append(clazz.getName());
        result.append(" having the parameter(s) (");
        for (int i = 0; i < argTypes.length; i++) {
            if (i > 0) {
                result.append(", ");
            }
            if (argTypes[i] != null) {
                result.append(argTypes[i].getName());
            } else {
                result.append("null");
            }
        }
        result.append(")");
        return result.toString();
    }

}

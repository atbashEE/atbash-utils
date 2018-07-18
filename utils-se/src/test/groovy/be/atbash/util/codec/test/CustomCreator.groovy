/**
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
package be.atbash.util.codec.test

import be.atbash.util.codec.ByteSource
import be.atbash.util.codec.CodecException
import be.atbash.util.codec.DefaultByteSource
import be.atbash.util.codec.DefaultByteSourceCreator

/**
 *
 */

class CustomCreator extends DefaultByteSourceCreator {

    boolean defaultCreator

    CustomCreator() {
        defaultCreator = System.getProperty("default.creator") == "true"
    }

    @Override
    boolean isCompatible(Object source) {
        boolean result = super.isCompatible(source);
        if (!result && !defaultCreator) {
            result = source instanceof Byte
        }
        return result
    }

    @Override
    ByteSource bytes(Object source) {
        if (defaultCreator) {
            return super.bytes(source)
        }
        if (source instanceof Byte) {
            return new DefaultByteSource([((Byte) source).byteValue()] as byte[])
        }
        throw new CodecException("Not supported type " + source.class)
    }
}

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
package be.atbash.util.codec.test;

import be.atbash.util.codec.ByteSource;
import be.atbash.util.codec.CodecException;
import be.atbash.util.codec.DefaultByteSource;
import be.atbash.util.codec.DefaultByteSourceCreator;

/**
 *
 */

public class CustomCreator extends DefaultByteSourceCreator {

    private boolean isDefaultCreator() {
        return System.getProperty("default.creator").equals("true");
    }

    @Override
    public boolean isCompatible(Object source) {
        boolean result = super.isCompatible(source);
        if (!result && !isDefaultCreator()) {
            result = source instanceof Byte;
        }
        return result;
    }

    @Override
    public ByteSource bytes(Object source) {
        if (isDefaultCreator()) {
            return super.bytes(source);
        }
        if (source instanceof Byte) {
            byte[] data = new byte[]{(Byte) source};
            return new DefaultByteSource(data);
        }
        throw new CodecException("Not supported type " + source.getClass());
    }

}

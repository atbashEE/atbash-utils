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
package be.atbash.util.codec;

import java.io.File;
import java.io.InputStream;

/**
 *
 */

public class DefaultByteSourceCreator implements ByteSourceCreator {

    /**
     * Returns {@code true} if the specified object is a recognized data type that can be easily converted to
     * bytes by instances of this class, {@code false} otherwise.
     * <p/>
     * This implementation returns {@code true} IF the specified object is an instance of one of the following
     * types:
     * <ul>
     * <li>{@code byte[]}</li>
     * <li>{@code char[]}</li>
     * <li>{@link ByteSource}</li>
     * <li>{@link String}</li>
     * <li>{@link File}</li>
     * </li>{@link InputStream}</li>
     * </ul>
     *
     * @param source the object to test to see if it can be easily converted to bytes by instances of this class.
     * @return {@code true} if the specified object can be easily converted to bytes by instances of this class,
     * {@code false} otherwise.
     */
    public boolean isCompatible(Object source) {
        return source instanceof byte[] || source instanceof char[] || source instanceof String ||
                source instanceof ByteSource || source instanceof File || source instanceof InputStream;
    }

    @Override
    public ByteSource bytes(Object source) {
        if (source == null) {
            return null;
        }
        if (!isCompatible(source)) {
            String msg = String.format("Unable to heuristically acquire bytes for object of type [%s]." +
                            " If this type is indeed a byte-backed data type, you might " +
                            "want to write your own ByteSourceCreator implementation to extract its bytes explicitly."
                    , source.getClass().getName());
            throw new CodecException(msg);
        }
        if (source instanceof byte[]) {
            return new DefaultByteSource((byte[]) source);
        } else if (source instanceof ByteSource) {
            return (ByteSource) source;
        } else if (source instanceof char[]) {
            return new DefaultByteSource((char[]) source);
        } else if (source instanceof String) {
            return new DefaultByteSource((String) source);
        } else if (source instanceof File) {
            return new DefaultByteSource((File) source);
        } else if (source instanceof InputStream) {
            return new DefaultByteSource((InputStream) source);
        } else {
            throw new IllegalStateException("Encountered unexpected byte source.  This is a bug - " +
                    "(the isCompatible implementation does not reflect this method's implementation).");
        }

    }

}

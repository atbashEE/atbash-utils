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


import be.atbash.util.base64.Base64Codec;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Very simple {@link ByteSource ByteSource} implementation that maintains an internal {@code byte[]} array and uses the
 * {@link Hex Hex} and {@link be.atbash.util.base64.Base64Codec Base64} codec classes to support the
 * {@link #toHex() toHex()} and {@link #toBase64() toBase64()} implementations.
 * <p/>
 * The constructors on this class accept the following implicit byte-backed data types and will convert them to
 * a byte-array automatically:
 * <ul>
 * <li>byte[]</li>
 * <li>char[]</li>
 * <li>String</li>
 * <li>{@link ByteSource ByteSource}</li>
 * <li>{@link File File}</li>
 * <li>{@link InputStream InputStream}</li>
 * </ul>
 */
//@ShiroEquivalent(shiroClassNames = {"org.apache.shiro.util.SimpleByteSource"})
public class SimpleByteSource implements ByteSource {

    private final byte[] bytes;
    private String cachedHex;
    private String cachedBase64;

    public SimpleByteSource(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Creates an instance by converting the characters to a byte array (assumes UTF-8 encoding).
     *
     * @param chars the source characters to use to create the underlying byte array.
     */
    public SimpleByteSource(char[] chars) {
        bytes = CodecSupport.toBytes(chars);
    }

    /**
     * Creates an instance by converting the String to a byte array (assumes UTF-8 encoding).
     *
     * @param string the source string to convert to a byte array (assumes UTF-8 encoding).
     */
    public SimpleByteSource(String string) {
        bytes = CodecSupport.toBytes(string);
    }

    /**
     * Creates an instance using the sources bytes directly - it does not create a copy of the
     * argument's byte array.
     *
     * @param source the source to use to populate the underlying byte array.
     */
    public SimpleByteSource(ByteSource source) {
        bytes = source.getBytes();
    }

    /**
     * Creates an instance by converting the file to a byte array.
     *
     * @param file the file from which to acquire bytes.
     */
    public SimpleByteSource(File file) {
        bytes = new BytesHelper().getBytes(file);
    }

    /**
     * Creates an instance by converting the stream to a byte array.
     *
     * @param stream the stream from which to acquire bytes.
     */
    public SimpleByteSource(InputStream stream) {
        bytes = new BytesHelper().getBytes(stream);
    }

    /**
     * Returns {@code true} if the specified object is a recognized data type that can be easily converted to
     * bytes by instances of this class, {@code false} otherwise.
     * <p/>
     * This implementation returns {@code true} IFF the specified object is an instance of one of the following
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
     * @param o the object to test to see if it can be easily converted to bytes by instances of this class.
     * @return {@code true} if the specified object can be easily converted to bytes by instances of this class,
     * {@code false} otherwise.
     */
    public static boolean isCompatible(Object o) {
        return o instanceof byte[] || o instanceof char[] || o instanceof String ||
                o instanceof ByteSource || o instanceof File || o instanceof InputStream;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public boolean isEmpty() {
        return bytes == null || bytes.length == 0;
    }

    public String toHex() {
        if (cachedHex == null) {
            cachedHex = Hex.encodeToString(getBytes());
        }
        return cachedHex;
    }

    public String toBase64() {
        if (cachedBase64 == null) {
            cachedBase64 = Base64Codec.encodeToString(getBytes(), true);
        }
        return cachedBase64;
    }

    public String toString() {
        return toBase64();
    }

    public int hashCode() {
        if (bytes == null || bytes.length == 0) {
            return 0;
        }
        return Arrays.hashCode(bytes);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ByteSource) {
            ByteSource bs = (ByteSource) o;
            return Arrays.equals(getBytes(), bs.getBytes());
        }
        return false;
    }

    //TODO Restructure CodecSupport and this class for the strange construct (abstract and here extends to access some methods)
    private static final class BytesHelper extends CodecSupport {
        public byte[] getBytes(File file) {
            return toBytes(file);
        }

        public byte[] getBytes(InputStream stream) {
            return toBytes(stream);
        }
    }
}

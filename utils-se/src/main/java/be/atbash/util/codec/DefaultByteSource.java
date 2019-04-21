/*
 * Copyright 2014-2019 Rudy De Busscher (https://www.atbash.be)
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
import java.util.Arrays;
import java.util.Base64;

/**
 * Very simple {@link ByteSource ByteSource} implementation that maintains an internal {@code byte[]} array and uses the
 * {@link Hex Hex} and {@link java.util.Base64 Base64} classes to support the
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
public class DefaultByteSource implements ByteSource {

    private final byte[] bytes;
    private String cachedHex;
    private String cachedBase64;
    private String cachedBase32;

    // Public constructors as class can be reused by custom ByteSourceCreator.
    public DefaultByteSource(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Creates an instance by converting the characters to a byte array (assumes UTF-8 encoding).
     *
     * @param chars the source characters to use to create the underlying byte array.
     */
    public DefaultByteSource(char[] chars) {
        bytes = CodecSupport.toBytes(chars);
    }

    /**
     * Creates an instance by converting the String to a byte array (assumes UTF-8 encoding).
     *
     * @param string the source string to convert to a byte array (assumes UTF-8 encoding).
     */
    public DefaultByteSource(String string) {
        bytes = CodecSupport.toBytes(string);
    }

    /**
     * Creates an instance by converting the file to a byte array.
     *
     * @param file the file from which to acquire bytes.
     */
    public DefaultByteSource(File file) {
        bytes = new BytesHelper().getBytes(file);
    }

    /**
     * Creates an instance by converting the stream to a byte array.
     *
     * @param stream the stream from which to acquire bytes.
     */
    public DefaultByteSource(InputStream stream) {
        bytes = new BytesHelper().getBytes(stream);
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
            cachedBase64 = Base64.getUrlEncoder()
                    .withoutPadding().encodeToString(getBytes());
        }
        return cachedBase64;
    }

    public String toBase32() {
        if (cachedBase32 == null) {
            cachedBase32 = Base32Codec.encodeToString(getBytes());
        }
        return cachedBase32;
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

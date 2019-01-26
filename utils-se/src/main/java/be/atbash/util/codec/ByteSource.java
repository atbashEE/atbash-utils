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

import be.atbash.util.PublicAPI;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * A {@code ByteSource} wraps a byte array and provides additional encoding operations.  Most users will find the
 * {@link DefaultByteSourceCreator} class sufficient to construct ByteSource instances.
 */
//@ShiroEquivalent(shiroClassNames = {"org.apache.shiro.util.ByteSource"})
@PublicAPI
public interface ByteSource {

    /**
     * Returns the wrapped byte array.
     *
     * @return the wrapped byte array.
     */
    byte[] getBytes();

    /**
     * Returns the <a href="http://en.wikipedia.org/wiki/Hexadecimal">Hex</a>-formatted String representation of the
     * underlying wrapped byte array.
     *
     * @return the <a href="http://en.wikipedia.org/wiki/Hexadecimal">Hex</a>-formatted String representation of the
     * underlying wrapped byte array.
     */
    String toHex();

    /**
     * Returns the <a href="http://en.wikipedia.org/wiki/Base64">Base 64</a>-formatted String representation of the
     * underlying wrapped byte array.
     *
     * @return the <a href="http://en.wikipedia.org/wiki/Base64">Base 64</a>-formatted String representation of the
     * underlying wrapped byte array.
     */
    String toBase64();

    /**
     * Returns the Base 32-formatted String representation of the
     * underlying wrapped byte array.
     *
     * @return the Base32-formatted String representation of the
     * underlying wrapped byte array.
     */
    String toBase32();

    /**
     * Returns {@code true} if the underlying wrapped byte array is null or empty (zero length), {@code false}
     * otherwise.
     *
     * @return {@code true} if the underlying wrapped byte array is null or empty (zero length), {@code false}
     * otherwise.
     */
    boolean isEmpty();

    ByteSourceCreator creator = CreatorInitializer.defineCreator();

    final class CreatorInitializer {

        private CreatorInitializer() {
        }

        static ByteSourceCreator defineCreator() {

            Iterator<ByteSourceCreator> creatorIterator = ServiceLoader.load(ByteSourceCreator.class).iterator();
            if (creatorIterator.hasNext()) {
                return creatorIterator.next(); // TODO What if there are multiple defined?
            } else {
                return new DefaultByteSourceCreator();
            }
        }
    }

}

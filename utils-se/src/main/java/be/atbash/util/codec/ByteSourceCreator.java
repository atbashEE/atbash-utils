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

import be.atbash.util.PublicAPI;

/**
 *
 */
@PublicAPI
public interface ByteSourceCreator {

    /**
     * Returns {@code true} if the specified object can be easily represented as a {@code ByteSource} using
     * the {@link DefaultByteSourceCreator}'s default heuristics, {@code false} otherwise.
     * <p/>
     * This implementation returns true for byte[], char[], String, ByteSource, File, and InputStream.
     *
     * @param source the object to test to see if it can be easily converted to ByteSource instances using default
     *               heuristics.
     * @return {@code true} if the specified object can be easily represented as a {@code ByteSource} using
     * the {@link DefaultByteSourceCreator}'s default heuristics, {@code false} otherwise.
     */
    boolean isCompatible(Object source);

    /**
     * Returns a {@code ByteSource} instance representing the specified byte source argument.  If the argument
     * <em>cannot</em> be easily converted to bytes (as is indicated by the {@link #isCompatible(Object)} JavaDoc),
     * this method will throw an {@link IllegalArgumentException}.
     *
     * @param source the byte-backed instance that should be represented as a {@code ByteSource} instance.
     * @return a {@code ByteSource} instance representing the specified byte source argument.
     * @throws CodecException if the argument <em>cannot</em> be easily converted to bytes
     *                                  (as indicated by the {@link #isCompatible(Object)} JavaDoc)
     */
    ByteSource bytes(Object source);
}
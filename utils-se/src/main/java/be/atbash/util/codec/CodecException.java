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

import be.atbash.util.exception.AtbashException;

/**
 * Root exception related to issues during encoding or decoding.
 */
public class CodecException extends AtbashException {

    /**
     * Creates a new <code>CodecException</code>.
     */
    public CodecException() {
        super();
    }

    /**
     * Creates a new <code>CodecException</code>.
     *
     * @param message the reason for the exception.
     */
    public CodecException(String message) {
        super(message);
    }

    /**
     * Creates a new <code>CodecException</code>.
     *
     * @param cause the underlying cause of the exception.
     */
    public CodecException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new <code>CodecException</code>.
     *
     * @param message the reason for the exception.
     * @param cause   the underlying cause of the exception.
     */
    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }
}

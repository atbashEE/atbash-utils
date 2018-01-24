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
package be.atbash.util.exception;

import be.atbash.util.PublicAPI;

/**
 * Root exception for all Atbash runtime exceptions.  This class is used as the root for all
 * Atbash exception
 */
@PublicAPI
public class AtbashException extends RuntimeException {

    /**
     * Creates a new AtbashException.
     */

    public AtbashException() {
        super();
    }

    /**
     * Constructs a new AtbashException with a reason message.
     *
     * @param message the reason for the exception
     */
    public AtbashException(String message) {
        super(message);
    }

    /**
     * Constructs a new AtbashException keeping the original Throwable.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public AtbashException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new AtbashException with a reason message and keeping the original Throwable.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public AtbashException(String message, Throwable cause) {
        super(message, cause);
    }

}
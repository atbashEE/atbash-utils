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
import be.atbash.util.StringUtils;

/**
 * Developer can use this to rethrow an exception when we know that it should never happen.
 * It allows the capture a checked exception and propagate it as un unchecked.
 * <p>Typically used for catching something when you you don't expect to have ever that issue. But since it
 * is a checked exception, you need to capture it and this is a convention way to let the exception bubble up to
 * a handler which handle any AtbashException.
 */
@PublicAPI
public class AtbashUnexpectedException extends AtbashException {

    /**
     * Creates an unexpectedException with a message.
     *
     * @param message The message what went unexpectedly wrong.
     */
    public AtbashUnexpectedException(String message) {
        super(message);
        if (StringUtils.isEmpty(message)) {
            throw new AtbashIllegalActionException("An AtbashUnexpectedException always requires an explanation message");
        }

    }

    /**
     * Creates an unexpectedException with a Throwable.
     *
     * @param cause The Throwable which you is not expected to occur.
     */
    public AtbashUnexpectedException(Throwable cause) {
        super(cause);
    }

}

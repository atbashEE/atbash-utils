/*
 * Copyright 2014-2022 Rudy De Busscher (https://www.atbash.be)
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
package be.atbash.util.base32;

import be.atbash.util.codec.Base32Codec;
import be.atbash.util.codec.CodecException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class Base32CodecTest {

    @Test
    void encodeToString() {
        String encode = Base32Codec.encodeToString("test".getBytes());
        Assertions.assertThat(encode).isEqualTo("ORSXG5A");
    }

    @Test
    void decode() {
        byte[] value = Base32Codec.decode("ORSXG5A");
        Assertions.assertThat("test").isEqualTo(new String(value));

    }

    @Test
    void decode_WrongCharacter() {
        Assertions.assertThatThrownBy(() ->
                        Base32Codec.decode("ORSXG8"))
                .isInstanceOf(CodecException.class);
    }

    @Test
    void decode_toUppercase() {
        byte[] value = Base32Codec.decode("ORsxG5a");
        Assertions.assertThat("test").isEqualTo(new String(value));
    }

}
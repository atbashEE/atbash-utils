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
package be.atbash.util.base32;

import be.atbash.util.codec.Base32Codec;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Base32CodecTest {

    @Test
    public void encodeToString() {
        String encode = Base32Codec.encodeToString("test".getBytes());
        assertThat(encode).isEqualTo("ORSXG5A");
    }

    @Test
    public void decode() {
        byte[] value = Base32Codec.decode("ORSXG5A");
        assertThat("test").isEqualTo(new String(value));

    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_WrongCharacter() {
        Base32Codec.decode("ORSXG8");
    }

    @Test
    public void decode_toUppercase() {
        byte[] value = Base32Codec.decode("ORsxG5a");
        assertThat("test").isEqualTo(new String(value));
    }

}
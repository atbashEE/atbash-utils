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
package be.atbash.util.codec;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class HexTest {

    @Test
    void encodeToString() {
        Assertions.assertThat(Hex.encodeToString("Hello World".getBytes(StandardCharsets.UTF_8))).isEqualTo("48656C6C6F20576F726C64");
    }

    @Test
    void decodeToString() {
        Assertions.assertThat(new String(Hex.decode("48656C6C6F20576F726C64"))).isEqualTo("Hello World");
    }

    @Test
    void isHexEncoded() {
        Assertions.assertThat(Hex.isHexEncoded("48656C6C6F20576F726C64")).isTrue();
    }

    @Test
    void isHexEncoded_oddNumber() {
        Assertions.assertThat(Hex.isHexEncoded("48656C6C6F20576F726C6")).isFalse(); // last digit removed
    }

    @Test
    void isHexEncoded_wrongCharacter() {
        Assertions.assertThat(Hex.isHexEncoded("48656C6C6F20576F726C6X")).isFalse(); // Wrong character
    }


}

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ByteSourceTest {

    @AfterEach
    void tearDown() {
        System.clearProperty("default.creator");
    }

    @Test
    void default_Creator() {
        System.setProperty("default.creator", "true");

        Assertions.assertThat(ByteSource.creator.isCompatible("Atbash")).isTrue();
        Assertions.assertThat(ByteSource.creator.bytes("Atbash").getBytes()).isEqualTo(new byte[]{65, 116, 98, 97, 115, 104});

    }

    @Test
    void unknown_type_with_default_creator() {
        Assertions.assertThatThrownBy(() -> {
            Base32Codec.decode("ORSXG8");
            System.setProperty("default.creator", "true");
            ByteSource.creator.bytes(123L);
        }).isInstanceOf(CodecException.class);
    }

    @Test
    void extra_type_with_custom_creator() {
        System.setProperty("default.creator", "false");

        Assertions.assertThat(ByteSource.creator.isCompatible((byte) 123)).isTrue();
        Assertions.assertThat(ByteSource.creator.bytes((byte) 123).getBytes()).isEqualTo(new byte[]{123});

    }

}

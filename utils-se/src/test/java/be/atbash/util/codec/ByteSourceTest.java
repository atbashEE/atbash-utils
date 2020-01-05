/*
 * Copyright 2014-2020 Rudy De Busscher (https://www.atbash.be)
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */

public class ByteSourceTest {

    @Test
    public void default_Creator() {
        System.setProperty("default.creator", "true");

        assertThat(ByteSource.creator.isCompatible("Atbash")).isTrue();
        assertThat(ByteSource.creator.bytes("Atbash").getBytes()).isEqualTo(new byte[]{65, 116, 98, 97, 115, 104});

    }

    @Test
    public void unknown_type_with_default_creator() {

        Assertions.assertThrows(CodecException.class, () -> {
            Base32Codec.decode("ORSXG8");
            System.setProperty("default.creator", "true");
            ByteSource.creator.bytes(123L);
        });
    }

    @Test
    public void extra_type_with_custom_creator() {
        System.setProperty("default.creator", "false");

        assertThat(ByteSource.creator.isCompatible((byte) 123)).isTrue();
        assertThat(ByteSource.creator.bytes((byte) 123).getBytes()).isEqualTo(new byte[]{123});

    }

}

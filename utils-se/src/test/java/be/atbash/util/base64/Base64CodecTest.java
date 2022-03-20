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
package be.atbash.util.base64;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

class Base64CodecTest {


    @Test
    void encodeToString_plain() {

        String actual = Base64.getUrlEncoder().withoutPadding().encodeToString("My Plain String".getBytes(StandardCharsets.UTF_8));
        Assertions.assertThat(actual).isEqualTo("TXkgUGxhaW4gU3RyaW5n");
    }

    @Test
    void encodeToString_multiLine() {

        Assertions.assertThat(Base64.getUrlEncoder().withoutPadding().encodeToString("My Plain String\nMy Plain String".getBytes(StandardCharsets.UTF_8))).isEqualTo("TXkgUGxhaW4gU3RyaW5nCk15IFBsYWluIFN0cmluZw");
    }

    @Test
    void EncodeToString_unicode() {

        Assertions.assertThat(Base64.getEncoder().encodeToString("小飼弾".getBytes(StandardCharsets.UTF_8))).isEqualTo("5bCP6aO85by+");
    }

    @Test
    void encodeToString_unicode_URLSafe() {

        Assertions.assertThat(Base64.getUrlEncoder().withoutPadding().encodeToString("小飼弾".getBytes(StandardCharsets.UTF_8))).isEqualTo("5bCP6aO85by-");
    }

    @Test
    void encodeToString_cyrillic() {

        String actual = Base64.getEncoder().encodeToString("зис из плэйн стринг".getBytes(StandardCharsets.UTF_8));
        Assertions.assertThat(actual).isEqualTo("0LfQuNGBINC40Lcg0L/Qu9GN0LnQvSDRgdGC0YDQuNC90LM=");
    }

    @Test
    void encodeToString_cyrillic_URLSafe() {

        String actual = Base64.getUrlEncoder().withoutPadding().encodeToString("зис из плэйн стринг".getBytes(StandardCharsets.UTF_8));
        Assertions.assertThat(actual).isEqualTo("0LfQuNGBINC40Lcg0L_Qu9GN0LnQvSDRgdGC0YDQuNC90LM");
    }

    @Test
    void decode_plain() {

        Assertions.assertThat(new String(Base64.getDecoder().decode("TXkgUGxhaW4gU3RyaW5n"))).isEqualTo("My Plain String");
    }

    @Test
    void decode_empty() {

        Assertions.assertThat(Base64.getDecoder().decode("")).isEmpty();
    }

    @Test
    void decode_cyrillic() {

        Assertions.assertThat(
                new String(Base64.getDecoder().decode("5bCP6aO85by+"))).isEqualTo("小飼弾");
        Assertions.assertThat(new String(Base64.getUrlDecoder().decode("5bCP6aO85by-"))).isEqualTo("小飼弾");
    }

    @Test
    void decode_byteArray() {
        byte[] decode = Base64.getDecoder().decode("VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2dzLg==".getBytes(StandardCharsets.UTF_8));
        Assertions.assertThat(new String(decode)).isEqualTo("The quick brown fox jumped over the lazy dogs.");
    }

}

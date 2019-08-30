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
package be.atbash.util.base64;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */

public class Base64CodecTest {


    @Test
    public void encodeToString_plain() {

        String actual = Base64.getUrlEncoder().withoutPadding().encodeToString("My Plain String".getBytes(StandardCharsets.UTF_8));
        assertThat(actual).isEqualTo("TXkgUGxhaW4gU3RyaW5n");
    }

    @Test
    public void encodeToString_multiLine() {

        assertThat(Base64.getUrlEncoder().withoutPadding().encodeToString("My Plain String\nMy Plain String".getBytes(StandardCharsets.UTF_8))).isEqualTo("TXkgUGxhaW4gU3RyaW5nCk15IFBsYWluIFN0cmluZw");
    }

    @Test
    public void EncodeToString_unicode() {

        assertThat(Base64.getEncoder().encodeToString("小飼弾".getBytes(StandardCharsets.UTF_8))).isEqualTo("5bCP6aO85by+");
    }

    @Test
    public void encodeToString_unicode_URLSafe() {

        assertThat(Base64.getUrlEncoder().withoutPadding().encodeToString("小飼弾".getBytes(StandardCharsets.UTF_8))).isEqualTo("5bCP6aO85by-");
    }

    @Test
    public void encodeToString_cyrillic() {

        String actual = Base64.getEncoder().encodeToString("зис из плэйн стринг".getBytes(StandardCharsets.UTF_8));
        assertThat(actual).isEqualTo("0LfQuNGBINC40Lcg0L/Qu9GN0LnQvSDRgdGC0YDQuNC90LM=");
    }

    @Test
    public void encodeToString_cyrillic_URLSafe() {

        String actual = Base64.getUrlEncoder().withoutPadding().encodeToString("зис из плэйн стринг".getBytes(StandardCharsets.UTF_8));
        assertThat(actual).isEqualTo("0LfQuNGBINC40Lcg0L_Qu9GN0LnQvSDRgdGC0YDQuNC90LM");
    }

    @Test
    public void decode_plain() {

        assertThat(new String(Base64.getDecoder().decode("TXkgUGxhaW4gU3RyaW5n"))).isEqualTo("My Plain String");
    }

    @Test
    public void decode_empty() {

        assertThat(Base64.getDecoder().decode("")).isEmpty();
    }

    @Test
    public void decode_cyrillic() {

        assertThat(
                new String(Base64.getDecoder().decode("5bCP6aO85by+"))).isEqualTo("小飼弾");
        assertThat(new String(Base64.getUrlDecoder().decode("5bCP6aO85by-"))).isEqualTo("小飼弾");
    }

    @Test
    public void decode_byteArray() {
        byte[] decode = Base64.getDecoder().decode("VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2dzLg==".getBytes(StandardCharsets.UTF_8));
        assertThat(new String(decode)).isEqualTo("The quick brown fox jumped over the lazy dogs.");
    }

}

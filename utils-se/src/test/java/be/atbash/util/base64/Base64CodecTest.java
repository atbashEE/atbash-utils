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
package be.atbash.util.base64;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */

public class Base64CodecTest {

    @Test
    public void isBase64Encoded() {
        assertThat(Base64Codec.isBase64Encoded("TXkgUGxhaW4gU3RyaW5n")).isTrue();
    }

    @Test
    public void isBase64Encoded_separators() {
        assertThat(Base64Codec.isBase64Encoded("0LfQuNGBINC40Lcg0L/Qu9GN0L\nnQvSDRgdGC0YDQuNC90LM=")).isTrue();
    }

    @Test
    public void isBase64Encoded_missingChar() {
        assertThat(Base64Codec.isBase64Encoded("TXkgUGxhaW4gU3RyaW5")).isFalse();
    }

    @Test
    public void isBase64Encoded_IllegalChar() {
        assertThat(Base64Codec.isBase64Encoded("зис из плэйн стринг")).isFalse();
        assertThat(Base64Codec.isBase64Encoded("小飼弾")).isFalse();
        assertThat(Base64Codec.isBase64Encoded("TXkgU\tGxhaW4gU3RyaW5n")).isFalse();
    }

    @Test
    public void encodeToString_plain() {

        String actual = Base64Codec.encodeToString("My Plain String".getBytes(Charset.forName("UTF-8")), true);
        assertThat(actual).isEqualTo("TXkgUGxhaW4gU3RyaW5n");
    }

    @Test
    public void encodeToString_multiLine() {

        assertThat(Base64Codec.encodeToString("My Plain String\nMy Plain String".getBytes(Charset.forName("UTF-8")), true)).isEqualTo("TXkgUGxhaW4gU3RyaW5nCk15IFBsYWluIFN0cmluZw");
    }

    @Test
    public void EncodeToString_unicode() {

        assertThat(Base64Codec.encodeToString("小飼弾".getBytes(Charset.forName("UTF-8")), false)).isEqualTo("5bCP6aO85by+");
    }

    @Test
    public void encodeToString_unicode_URLSafe() {

        assertThat(Base64Codec.encodeToString("小飼弾".getBytes(Charset.forName("UTF-8")), true)).isEqualTo("5bCP6aO85by-");
    }

    @Test
    public void encodeToString_cyrillic() {

        String actual = Base64Codec.encodeToString("зис из плэйн стринг".getBytes(Charset.forName("UTF-8")), false);
        assertThat(actual).isEqualTo("0LfQuNGBINC40Lcg0L/Qu9GN0LnQvSDRgdGC0YDQuNC90LM=");
    }

    @Test
    public void encodeToString_cyrillic_URLSafe() {

        String actual = Base64Codec.encodeToString("зис из плэйн стринг".getBytes(Charset.forName("UTF-8")), true);
        assertThat(actual).isEqualTo("0LfQuNGBINC40Lcg0L_Qu9GN0LnQvSDRgdGC0YDQuNC90LM");
    }

    @Test
    public void decode_plain() {

        assertThat(new String(Base64Codec.decode("TXkgUGxhaW4gU3RyaW5n"))).isEqualTo("My Plain String");
    }

    @Test
    public void decode_empty() {

        assertThat(Base64Codec.decode("")).isEmpty();
    }

    @Test
    public void decode_null() {

        assertThat(Base64Codec.decode((String) null)).isEmpty();
    }

    @Test
    public void decode_cyrillic() {

        assertThat(
                new String(Base64Codec.decode("5bCP6aO85by+"))).isEqualTo("小飼弾");
        assertThat(new String(Base64Codec.decode("5bCP6aO85by-"))).isEqualTo("小飼弾");
    }

    @Test
    public void decode_byteArray() throws UnsupportedEncodingException {
        byte[] decode = Base64Codec.decode("VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2dzLg==".getBytes("UTF-8"));
        assertThat(new String(decode)).isEqualTo("The quick brown fox jumped over the lazy dogs.");
    }

}

/**
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
package be.atbash.util.base64

import spock.lang.Specification

import java.nio.charset.Charset

/**
 *
 */

class Base64CodecTest extends Specification {

    def "isBase64Encoded"() {
        expect:
        Base64Codec.isBase64Encoded("TXkgUGxhaW4gU3RyaW5n")
    }

    def "isBase64Encoded_separators"() {
        expect:
        Base64Codec.isBase64Encoded("0LfQuNGBINC40Lcg0L/Qu9GN0L\nnQvSDRgdGC0YDQuNC90LM=")
    }

    def "isBase64Encoded_missingChar"() {
        expect:
        !Base64Codec.isBase64Encoded("TXkgUGxhaW4gU3RyaW5")
    }

    def "isBase64Encoded_IllegalChar"() {
        expect:
        !Base64Codec.isBase64Encoded("зис из плэйн стринг")
        !Base64Codec.isBase64Encoded("小飼弾")
        !Base64Codec.isBase64Encoded("TXkgU\tGxhaW4gU3RyaW5n")
    }

    def "EncodeToString_plain"() {

        expect:
        Base64Codec.encodeToString("My Plain String".getBytes(Charset.forName("UTF-8")), true) == "TXkgUGxhaW4gU3RyaW5n"
    }

    def "EncodeToString_multiLine"() {

        expect:
        Base64Codec.encodeToString("My Plain String\nMy Plain String".getBytes(Charset.forName("UTF-8")), true) == "TXkgUGxhaW4gU3RyaW5nCk15IFBsYWluIFN0cmluZw"
    }

    def "EncodeToString_unicode"() {

        expect:
        Base64Codec.encodeToString("小飼弾".getBytes(Charset.forName("UTF-8")), false) == "5bCP6aO85by+"
    }

    def "EncodeToString_unicode_URLSafe"() {

        expect:
        Base64Codec.encodeToString("小飼弾".getBytes(Charset.forName("UTF-8")), true) == "5bCP6aO85by-"
    }

    def "EncodeToString_cyrillic"() {

        expect:
        Base64Codec.encodeToString("зис из плэйн стринг".getBytes(Charset.forName("UTF-8")), false) == "0LfQuNGBINC40Lcg0L/Qu9GN0LnQvSDRgdGC0YDQuNC90LM="
    }

    def "EncodeToString_cyrillic_URLSafe"() {

        expect:
        Base64Codec.encodeToString("зис из плэйн стринг".getBytes(Charset.forName("UTF-8")), true) == "0LfQuNGBINC40Lcg0L_Qu9GN0LnQvSDRgdGC0YDQuNC90LM"
    }

    def "Decode_plain"() {

        expect:
        new String(Base64Codec.decode("TXkgUGxhaW4gU3RyaW5n")) == "My Plain String"
    }

    def "Decode_empty"() {

        expect:
        Base64Codec.decode("") == []
    }

    /*
    Groovy can decide which overloaded method :(
    def "Decode_null"() {

        expect:
        Base64Codec.decode((String)null) == null
    }
    */

    def "Decode_cyrillic"() {

        expect:
        new String(Base64Codec.decode("5bCP6aO85by+")) == "小飼弾"
        new String(Base64Codec.decode("5bCP6aO85by-")) == "小飼弾"
    }

    def "Decode_byteArray"() {
        expect:
        new String(Base64Codec.decode("VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2dzLg==".getBytes("UTF-8"))) == "The quick brown fox jumped over the lazy dogs."
    }
}

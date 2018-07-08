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
package be.atbash.util.codec

import spock.lang.Specification

/**
 *
 */

class HexTest extends Specification {

    def "EncodeToString"() {
        expect:

        Hex.encodeToString("Hello World".getBytes("UTF-8")) == "48656C6C6F20576F726C64"
    }

    def "DecodeToString"() {
        expect:

        new String(Hex.decode("48656C6C6F20576F726C64")) == "Hello World"
    }

    def "isHexEncoded"() {
        expect:

        Hex.isHexEncoded("48656C6C6F20576F726C64")
    }

    def "isHexEncoded odd number"() {
        expect:

        !Hex.isHexEncoded("48656C6C6F20576F726C6") // last digit removed
    }

    def "isHexEncoded wrong character"() {
        expect:

        !Hex.isHexEncoded("48656C6C6F20576F726C6X") // Wrong character
    }
}

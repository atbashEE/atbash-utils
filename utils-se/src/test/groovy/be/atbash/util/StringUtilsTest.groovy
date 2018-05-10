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
package be.atbash.util

import spock.lang.Specification

/**
 *
 */

class StringUtilsTest extends Specification {

    def "CountOccurrences"() {
        expect:
        StringUtils.countOccurrences("abcabc", 'a' as char) == 2

    }

    def "CountOccurrences_notFound"() {
        expect:
        StringUtils.countOccurrences("abcabc", 'd' as char) == 0

    }

    def "CountOccurrences_empty"() {
        expect:
        StringUtils.countOccurrences("", 'd' as char) == 0

    }

    def "CountOccurrences_null"() {
        expect:
        StringUtils.countOccurrences(null, 'd' as char) == 0

    }

    def "hasLength_null"() {
        expect:
        !StringUtils.hasLength(null)
    }

    def "hasLength_empty"() {
        expect:
        !StringUtils.hasLength("")
    }

    def "hasLength_spaces"() {
        expect:
        StringUtils.hasLength(" ")
    }

    def "hasLength_value"() {
        expect:
        StringUtils.hasLength("A")
    }

    def "hasText_null"() {
        expect:
        !StringUtils.hasText(null)
    }

    def "hasText_empty"() {
        expect:
        !StringUtils.hasText("")
    }

    def "hasText_spaces"() {
        expect:
        !StringUtils.hasText(" ")
    }

    def "hasText_whiteSpace"() {
        when:
        char[] data = [Character.SPACE_SEPARATOR, Character.LINE_SEPARATOR, '\n']

        then:
        !StringUtils.hasText(new String(data))
    }

    def "hasText_text"() {
        expect:
        StringUtils.hasText(" a ")
    }

    def "isEmpty_null"() {
        expect:
        StringUtils.isEmpty(null)
    }

    def "isEmpty_empty"() {
        expect:
        StringUtils.isEmpty("")
    }

    def "isEmpty_spaces"() {
        expect:
        StringUtils.isEmpty(" ")
    }

    def "isEmpty_whiteSpace"() {
        when:
        char[] data = [Character.SPACE_SEPARATOR, Character.LINE_SEPARATOR, '\n']

        then:
        StringUtils.isEmpty(new String(data))
    }

    def "isEmpty_text"() {
        expect:
        !StringUtils.isEmpty(" a ")
    }

    def "isEmpty_nullCharArray"() {
        when:
        char[] data = null

        then:
        StringUtils.isEmpty(data)
    }

    def "isEmpty_emptyCharArray"() {
        when:
        char[] data = []

        then:
        StringUtils.isEmpty(data)
    }

    def "isEmpty_spacesCharArray"() {
        when:
        char[] data = [' ']

        then:
        StringUtils.isEmpty(data)
    }

    def "isEmpty_textCharArray"() {
        when:
        char[] data = " a ".toCharArray();

        then:
        !StringUtils.isEmpty(data)
    }

    def "startsWithIgnoreCase"() {
        expect:
        StringUtils.startsWithIgnoreCase("Atbash", "At")
    }

    def "startsWithIgnoreCase_differentCase"() {
        expect:
        StringUtils.startsWithIgnoreCase("Atbash", "at")
    }

    def "startsWithIgnoreCase_OtherValue"() {
        expect:
        !StringUtils.startsWithIgnoreCase("Atbash", "bash")
    }

    def "clean_null"() {
        expect:
        StringUtils.clean(null) == null
    }

    def "clean_empty"() {
        expect:
        StringUtils.clean("") == null
    }

    def "clean_whitespace"() {
        expect:
        StringUtils.clean("  ") == null
    }

    def "clean_trimmed"() {
        expect:
        StringUtils.clean(" Atbash ") == "Atbash"
    }

    def "clean_regular"() {
        expect:
        StringUtils.clean("Atbash") == "Atbash"
    }

    def "_toString"() {
        expect:
        StringUtils.toString("Atbash", "rocks") == "Atbash,rocks"
    }

    def "_toString_null"() {
        expect:
        StringUtils.toString(null) == ""
    }

    def "toDelimitedString"() {
        expect:
        StringUtils.toDelimitedString(["Atbash", "rocks"], " ") == "Atbash rocks"
    }

    def "toDelimitedString_List"() {
        expect:
        StringUtils.toDelimitedString(["Atbash", "rocks"] as List, " ") == "Atbash rocks"

    }

    def "tokenizeToStringArray"() {
        when:
        String[] data = StringUtils.tokenizeToStringArray("Atbash  , , rocks", ",")
        String[] expected = ["Atbash", "rocks"]

        then:
        Arrays.equals(data, expected)
    }

    def "tokenizeToStringArray_empty"() {
        expect:
        StringUtils.tokenizeToStringArray("", ",").size() == 0
    }

    def "tokenizeToStringArray_null"() {
        expect:
        StringUtils.tokenizeToStringArray(null, ",") == null
    }

    def "split"() {
        when:
        String[] data = StringUtils.split("Atbash  , , rocks")
        String[] expected = ["Atbash", "", "rocks"]

        then:
        Arrays.equals(data, expected)
    }

    def "join"() {
        when:
        List<String> data = ["Atbash", "rocks"]

        then:
        StringUtils.join(data.iterator(), " ") == "Atbash rocks"
    }
}

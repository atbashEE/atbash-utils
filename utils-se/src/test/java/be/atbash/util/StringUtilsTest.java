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
package be.atbash.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class StringUtilsTest {

    @Test
    void countOccurrences() {

        Assertions.assertThat(StringUtils.countOccurrences("abcabc", 'a')).isEqualTo(2);

    }

    @Test
    void countOccurrences_notFound() {
        Assertions.assertThat(StringUtils.countOccurrences("abcabc", 'd')).isEqualTo(0);

    }

    @Test
    void CountOccurrences_empty() {
        Assertions.assertThat(StringUtils.countOccurrences("", 'd')).isEqualTo(0);

    }

    @Test
    void countOccurrences_null() {
        Assertions.assertThat(StringUtils.countOccurrences(null, 'd')).isEqualTo(0);

    }

    @Test
    void hasLength_null() {
        Assertions.assertThat(StringUtils.hasLength(null)).isFalse();
    }

    @Test
    void hasLength_empty() {
        Assertions.assertThat(StringUtils.hasLength("")).isFalse();
    }

    @Test
    void hasLength_spaces() {
        Assertions.assertThat(StringUtils.hasLength(" ")).isTrue();
    }

    @Test
    void hasLength_value() {
        Assertions.assertThat(StringUtils.hasLength("A")).isTrue();
    }

    @Test
    void hasText_null() {
        Assertions.assertThat(StringUtils.hasText(null)).isFalse();
    }

    @Test
    void hasText_empty() {
        Assertions.assertThat(StringUtils.hasText("")).isFalse();
    }

    @Test
    void hasText_spaces() {
        Assertions.assertThat(StringUtils.hasText(" ")).isFalse();
    }

    @Test
    void hasText_whiteSpace() {
        char[] data = new char[]{Character.SPACE_SEPARATOR, Character.LINE_SEPARATOR, '\n'};

        Assertions.assertThat(StringUtils.hasText(new String(data))).isFalse();
    }

    @Test
    void hasText_text() {
        Assertions.assertThat(StringUtils.hasText(" a ")).isTrue();
    }

    @Test
    void isEmpty_null() {
        Assertions.assertThat(StringUtils.isEmpty((String) null)).isTrue();
    }

    @Test
    void isEmpty_empty() {
        Assertions.assertThat(StringUtils.isEmpty("")).isTrue();
    }

    @Test
    void isEmpty_spaces() {
        Assertions.assertThat(StringUtils.isEmpty(" ")).isTrue();
    }

    @Test
    void isEmpty_whiteSpace() {
        char[] data = new char[]{Character.SPACE_SEPARATOR, Character.LINE_SEPARATOR, '\n'};

        Assertions.assertThat(StringUtils.isEmpty(new String(data))).isTrue();
    }

    @Test
    void isEmpty_text() {
        Assertions.assertThat(StringUtils.isEmpty(" a ")).isFalse();
    }

    @Test
    void isEmpty_nullCharArray() {
        Assertions.assertThat(StringUtils.isEmpty((char[]) null)).isTrue();
    }

    @Test
    void isEmpty_emptyCharArray() {

        Assertions.assertThat(StringUtils.isEmpty(new char[]{})).isTrue();
    }

    @Test
    void isEmpty_spacesCharArray() {
        char[] data = new char[]{' '};

        Assertions.assertThat(StringUtils.isEmpty(data)).isTrue();
    }

    @Test
    void isEmpty_textCharArray() {
        Assertions.assertThat(StringUtils.isEmpty(" a ".toCharArray())).isFalse();
    }

    @Test
    void startsWithIgnoreCase() {
        Assertions.assertThat(StringUtils.startsWithIgnoreCase("Atbash", "At")).isTrue();
    }

    @Test
    void startsWithIgnoreCase_differentCase() {
        Assertions.assertThat(StringUtils.startsWithIgnoreCase("Atbash", "at")).isTrue();
    }

    @Test
    void startsWithIgnoreCase_OtherValue() {
        Assertions.assertThat(StringUtils.startsWithIgnoreCase("Atbash", "bash")).isFalse();
    }

    @Test
    void clean_null() {
        Assertions.assertThat(StringUtils.clean(null)).isNull();
    }

    @Test
    void clean_empty() {
        Assertions.assertThat(StringUtils.clean("")).isNull();
    }

    @Test
    void clean_whitespace() {
        Assertions.assertThat(StringUtils.clean("  ")).isNull();
    }

    @Test
    void clean_trimmed() {
        Assertions.assertThat(StringUtils.clean(" Atbash ")).isEqualTo("Atbash");
    }

    @Test
    void clean_regular() {
        Assertions.assertThat(StringUtils.clean("Atbash")).isEqualTo("Atbash");
    }

    @Test
    void toDelimitedString_defaultSeparator() {
        Assertions.assertThat(StringUtils.toDelimitedString(",", "Atbash", "rocks")).isEqualTo("Atbash,rocks");
    }

    @Test
    void _toDelimitedString_null() {
        Assertions.assertThat(StringUtils.toDelimitedString(null)).isEmpty();
    }

    @Test
    void toDelimitedString() {
        Assertions.assertThat(StringUtils.toDelimitedString(" ", "Atbash", "rocks")).isEqualTo("Atbash rocks");
    }

    @Test
    void toDelimitedString_List() {
        List<String> data = new ArrayList<>();
        data.add("Atbash");
        data.add("rocks");
        Assertions.assertThat(StringUtils.toDelimitedString(" ", data)).isEqualTo("Atbash rocks");

    }

    @Test
    void toDelimitedString_Iterator() {
        List<String> data = new ArrayList<>();
        data.add("Atbash");
        data.add("rocks");
        Assertions.assertThat(StringUtils.toDelimitedString(" ", data.iterator())).isEqualTo("Atbash rocks");

    }

    @Test
    void tokenizeToStringArray() {

        String[] data = StringUtils.tokenizeToStringArray("Atbash  , , rocks", ",");
        Assertions.assertThat(data).containsExactly("Atbash", "rocks");

    }

    @Test
    void tokenizeToStringArray_empty() {
        Assertions.assertThat(StringUtils.tokenizeToStringArray("", ",")).isEmpty();
    }

    @Test
    void tokenizeToStringArray_null() {
        Assertions.assertThat(StringUtils.tokenizeToStringArray(null, ",")).isNull();
    }

    @Test
    void split() {
        String[] data = StringUtils.split("Atbash  , , rocks");

        Assertions.assertThat(data).containsExactly("Atbash", "", "rocks");
    }

}

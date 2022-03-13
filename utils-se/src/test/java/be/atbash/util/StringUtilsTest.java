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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */

public class StringUtilsTest {

    @Test
    public void countOccurrences() {

        assertThat(StringUtils.countOccurrences("abcabc", 'a')).isEqualTo(2);

    }

    @Test
    public void countOccurrences_notFound() {
        assertThat(StringUtils.countOccurrences("abcabc", 'd')).isEqualTo(0);

    }

    @Test
    public void CountOccurrences_empty() {
        assertThat(StringUtils.countOccurrences("", 'd')).isEqualTo(0);

    }

    @Test
    public void countOccurrences_null() {
        assertThat(StringUtils.countOccurrences(null, 'd')).isEqualTo(0);

    }

    @Test
    public void hasLength_null() {
        assertThat(StringUtils.hasLength(null)).isFalse();
    }

    @Test
    public void hasLength_empty() {
        assertThat(StringUtils.hasLength("")).isFalse();
    }

    @Test
    public void hasLength_spaces() {
        assertThat(StringUtils.hasLength(" ")).isTrue();
    }

    @Test
    public void hasLength_value() {
        assertThat(StringUtils.hasLength("A")).isTrue();
    }

    @Test
    public void hasText_null() {
        assertThat(StringUtils.hasText(null)).isFalse();
    }

    @Test
    public void hasText_empty() {
        assertThat(StringUtils.hasText("")).isFalse();
    }

    @Test
    public void hasText_spaces() {
        assertThat(StringUtils.hasText(" ")).isFalse();
    }

    @Test
    public void hasText_whiteSpace() {
        char[] data = new char[]{Character.SPACE_SEPARATOR, Character.LINE_SEPARATOR, '\n'};

        assertThat(StringUtils.hasText(new String(data))).isFalse();
    }

    @Test
    public void hasText_text() {
        assertThat(StringUtils.hasText(" a ")).isTrue();
    }

    @Test
    public void isEmpty_null() {
        assertThat(StringUtils.isEmpty((String) null)).isTrue();
    }

    @Test
    public void isEmpty_empty() {
        assertThat(StringUtils.isEmpty("")).isTrue();
    }

    @Test
    public void isEmpty_spaces() {
        assertThat(StringUtils.isEmpty(" ")).isTrue();
    }

    @Test
    public void isEmpty_whiteSpace() {
        char[] data = new char[]{Character.SPACE_SEPARATOR, Character.LINE_SEPARATOR, '\n'};

        assertThat(StringUtils.isEmpty(new String(data))).isTrue();
    }

    @Test
    public void isEmpty_text() {
        assertThat(StringUtils.isEmpty(" a ")).isFalse();
    }

    @Test
    public void isEmpty_nullCharArray() {
        assertThat(StringUtils.isEmpty((char[]) null)).isTrue();
    }

    @Test
    public void isEmpty_emptyCharArray() {

        assertThat(StringUtils.isEmpty(new char[]{})).isTrue();
    }

    @Test
    public void isEmpty_spacesCharArray() {
        char[] data = new char[]{' '};

        assertThat(StringUtils.isEmpty(data)).isTrue();
    }

    @Test
    public void isEmpty_textCharArray() {
        assertThat(StringUtils.isEmpty(" a ".toCharArray())).isFalse();
    }

    @Test
    public void startsWithIgnoreCase() {
        assertThat(StringUtils.startsWithIgnoreCase("Atbash", "At")).isTrue();
    }

    @Test
    public void startsWithIgnoreCase_differentCase() {
        assertThat(StringUtils.startsWithIgnoreCase("Atbash", "at")).isTrue();
    }

    @Test
    public void startsWithIgnoreCase_OtherValue() {
        assertThat(StringUtils.startsWithIgnoreCase("Atbash", "bash")).isFalse();
    }

    @Test
    public void clean_null() {
        assertThat(StringUtils.clean(null)).isNull();
    }

    @Test
    public void clean_empty() {
        assertThat(StringUtils.clean("")).isNull();
    }

    @Test
    public void clean_whitespace() {
        assertThat(StringUtils.clean("  ")).isNull();
    }

    @Test
    public void clean_trimmed() {
        assertThat(StringUtils.clean(" Atbash ")).isEqualTo("Atbash");
    }

    @Test
    public void clean_regular() {
        assertThat(StringUtils.clean("Atbash")).isEqualTo("Atbash");
    }

    @Test
    public void toDelimitedString_defaultSeparator() {
        assertThat(StringUtils.toDelimitedString(",", "Atbash", "rocks")).isEqualTo("Atbash,rocks");
    }

    @Test
    public void _toDelimitedString_null() {
        assertThat(StringUtils.toDelimitedString(null)).isEmpty();
    }

    @Test
    public void toDelimitedString() {
        assertThat(StringUtils.toDelimitedString(" ", "Atbash", "rocks")).isEqualTo("Atbash rocks");
    }

    @Test
    public void toDelimitedString_List() {
        List<String> data = new ArrayList<>();
        data.add("Atbash");
        data.add("rocks");
        assertThat(StringUtils.toDelimitedString(" ", data)).isEqualTo("Atbash rocks");

    }

    @Test
    public void toDelimitedString_Iterator() {
        List<String> data = new ArrayList<>();
        data.add("Atbash");
        data.add("rocks");
        assertThat(StringUtils.toDelimitedString(" ", data.iterator())).isEqualTo("Atbash rocks");

    }

    @Test
    public void tokenizeToStringArray() {

        String[] data = StringUtils.tokenizeToStringArray("Atbash  , , rocks", ",");
        assertThat(data).containsExactly("Atbash", "rocks");

    }

    @Test
    public void tokenizeToStringArray_empty() {
        assertThat(StringUtils.tokenizeToStringArray("", ",")).isEmpty();
    }

    @Test
    public void tokenizeToStringArray_null() {
        assertThat(StringUtils.tokenizeToStringArray(null, ",")).isNull();
    }

    @Test
    public void split() {
        String[] data = StringUtils.split("Atbash  , , rocks");

        assertThat(data).containsExactly("Atbash", "", "rocks");
    }

}

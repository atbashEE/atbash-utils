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

import be.atbash.util.testclasses.Pojo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ObjectUtilTest {

    public static final String MSG_TO_CALLER = "String should not be null or empty";

    @Test
    void requireNot_String_null() {
        String data = null;
        Assertions.assertThatThrownBy(
                        () -> ObjectUtil.requireNot(data, String::isEmpty, MSG_TO_CALLER))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void requireNot_String_empty() {
        String data = "";
        Assertions.assertThatThrownBy(
                        () -> ObjectUtil.requireNot(data, String::isEmpty, MSG_TO_CALLER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MSG_TO_CALLER);
    }

    @Test
    void requireNot_String_blank_notEmptyCheck() {
        String data = " ";
        String result = ObjectUtil.requireNot(data, String::isEmpty, MSG_TO_CALLER);
        Assertions.assertThat(result).isEqualTo(data);
    }

    @Test
    void requireNot_String_blank_notBlankCheck() {
        String data = " ";
        Assertions.assertThatThrownBy(
                        () -> ObjectUtil.requireNot(data, (s) -> s.trim().isEmpty(), MSG_TO_CALLER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MSG_TO_CALLER);
    }

    @Test
    void requireNot_pojo_fieldNull() {
        Pojo data = new Pojo();
        Assertions.assertThatThrownBy(
                        () -> ObjectUtil.requireNot(data, p -> p.getId() == null, MSG_TO_CALLER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MSG_TO_CALLER);
    }

    @Test
    void requireNot_pojo_fieldNotNull() {
        Pojo data = new Pojo();
        data.setId(123L);
        Pojo result = ObjectUtil.requireNot(data, p -> p.getId() == null, MSG_TO_CALLER);
        Assertions.assertThat(result).isEqualTo(data);
    }

    @Test
    void requireNotBlank_null() {
        String data = null;
        Assertions.assertThatThrownBy(
                        () -> ObjectUtil.requireNotBlank(data, MSG_TO_CALLER))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void requireNotBlank_empty() {
        String data = "";
        Assertions.assertThatThrownBy(
                        () -> ObjectUtil.requireNotBlank(data, MSG_TO_CALLER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MSG_TO_CALLER);
    }

    @Test
    void requireNotBlank_blank() {
        String data = " ";

        Assertions.assertThatThrownBy(
                        () -> ObjectUtil.requireNotBlank(data, MSG_TO_CALLER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MSG_TO_CALLER);
    }

    @Test
    void requireNotBlank_String_blank_notBlankCheck() {
        String data = "JUnit";
        String result = ObjectUtil.requireNotBlank(data, MSG_TO_CALLER);
        Assertions.assertThat(result).isEqualTo(data);
    }
}
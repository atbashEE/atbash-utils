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
package be.atbash.util.reflection;

import be.atbash.util.exception.FieldNotFoundException;
import be.atbash.util.exception.SetFieldValueException;
import be.atbash.util.reflection.testclasses.FieldAccess;
import be.atbash.util.reflection.testclasses.PropertyAccess;
import be.atbash.util.testclasses.Pojo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Date;

class FastPropertyMemberAccessorTest {

    @Test
    void testAll() {
        // Proves that we can call getter and setter through LambdaMetaFactory.
        Pojo pojo = new Pojo();
        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(Pojo.class, "name");

        memberAccessor.set(pojo, "JUnit");

        String data = memberAccessor.get(pojo);
        // The original value is prepended with values added in getter and setter method.
        Assertions.assertThat(data).isEqualTo("Getter:Setter:JUnit");
    }

    @Test
    void testString() {
        PropertyAccess data = new PropertyAccess("ReadOnlyValue");

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(PropertyAccess.class, "value");

        Assertions.assertThat(memberAccessor.supportSet()).isTrue();
        String value = memberAccessor.get(data);
        Assertions.assertThat(value).isNull();
        memberAccessor.set(data, "JUnit");
        Assertions.assertThat(data.getValue()).isEqualTo("JUnit");

        data.setValue("Changed");

        value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo("Changed");

        memberAccessor.set(data, null);
        Assertions.assertThat(data.getValue()).isNull();
    }

    @Test
    void testLong() {
        PropertyAccess data = new PropertyAccess("ReadOnlyValue");

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(PropertyAccess.class, "number");

        Long value = memberAccessor.get(data);
        Assertions.assertThat(value).isNull();
        memberAccessor.set(data, 123L);
        Assertions.assertThat(data.getNumber()).isEqualTo(123L);

        data.setNumber(321L);

        value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo(321L);

        memberAccessor.set(data, null);
        Assertions.assertThat(data.getNumber()).isNull();
    }

    @Test
    void testBoolean() {
        PropertyAccess data = new PropertyAccess("ReadOnlyValue");

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(PropertyAccess.class, "flag");

        data.setFlag(true);

        Boolean value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo(Boolean.TRUE);
        memberAccessor.set(data, Boolean.FALSE);
        Assertions.assertThat(data.getFlag()).isFalse();

        value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo(Boolean.FALSE);

        memberAccessor.set(data, Boolean.TRUE);
        Assertions.assertThat(data.getFlag()).isTrue();
    }

    @Test
    void testDate() {
        PropertyAccess data = new PropertyAccess("ReadOnlyValue");

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(PropertyAccess.class, "timeStamp");

        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime yesterday = today.minusDays(1);

        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        Date todayAsDate = Date.from(today.toInstant(offset));
        Date yesterdayAsDate = Date.from(yesterday.toInstant(offset));

        data.setTimeStamp(todayAsDate);

        Date value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo(todayAsDate);
        memberAccessor.set(data, yesterdayAsDate);
        Assertions.assertThat(data.getTimeStamp()).isEqualTo(yesterdayAsDate);

        value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo(yesterdayAsDate);

        memberAccessor.set(data, null);
        Assertions.assertThat(data.getTimeStamp()).isNull();
    }

    @Test
    void testReadonly() {
        PropertyAccess data = new PropertyAccess("ReadOnlyValue");

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(PropertyAccess.class, "readonly");

        String value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo("ReadOnlyValue");
        Assertions.assertThat(memberAccessor.supportSet()).isFalse();

        Assertions.assertThatThrownBy(() ->
                        memberAccessor.set(data, "JUnit")
                ).isInstanceOf(SetFieldValueException.class)
                .hasMessage("The field 'readonly' is read-only and cannot set the value");
    }

    @Test
    void testUnknown() {
        Assertions.assertThatThrownBy(() ->
                        new FastPropertyMemberAccessor(PropertyAccess.class, "foo"))
                .isInstanceOf(FieldNotFoundException.class)
                .hasMessage("Property 'foo' not found in class class be.atbash.util.reflection.testclasses.PropertyAccess");

        // search is case sensitive
        Assertions.assertThatThrownBy(() ->
                        new FastPropertyMemberAccessor(PropertyAccess.class, "readOnly"))
                .isInstanceOf(FieldNotFoundException.class)
                .hasMessage("Property 'readOnly' not found in class class be.atbash.util.reflection.testclasses.PropertyAccess");
    }

    @Test
    void testMetaData() {
        PropertyAccess data = new PropertyAccess("ReadOnlyValue");

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(PropertyAccess.class, "value");
        Assertions.assertThat(memberAccessor.getName()).isEqualTo("value");
        Assertions.assertThat(memberAccessor.getType()).isEqualTo(String.class);
        Assertions.assertThat(memberAccessor.toString()).isEqualTo("Bean property 'value' on class be.atbash.util.reflection.testclasses.PropertyAccess");
    }

    @Test
    void testWrongClassAsValue() {
        PropertyAccess data = new PropertyAccess("ReadOnlyValue");

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(PropertyAccess.class, "number");

        Long value = memberAccessor.get(data);
        Assertions.assertThat(value).isNull();
        Assertions.assertThatThrownBy(() ->
                        memberAccessor.set(data, "JUnit"))
                .isInstanceOf(ClassCastException.class);
    }

    // Field Access
    @Test
    void testString_Field() {
        FieldAccess data = new FieldAccess();

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(FieldAccess.class, "value");

        Assertions.assertThat(memberAccessor.supportSet()).isTrue();
        String value = memberAccessor.get(data);
        Assertions.assertThat(value).isNull();
        memberAccessor.set(data, "JUnit");
        Assertions.assertThat(data.value).isEqualTo("JUnit");

        data.value = "Changed";

        value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo("Changed");

        memberAccessor.set(data, null);
        Assertions.assertThat(data.value).isNull();
    }

    @Test
    void testLong_Field() {
        FieldAccess data = new FieldAccess();

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(FieldAccess.class, "number");

        Long value = memberAccessor.get(data);
        Assertions.assertThat(value).isNull();
        memberAccessor.set(data, 123L);
        Assertions.assertThat(data.number).isEqualTo(123L);

        data.number = 321L;
        value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo(321L);

        memberAccessor.set(data, null);
        Assertions.assertThat(data.number).isNull();
    }

    @Test
    void testBoolean_Field() {
        FieldAccess data = new FieldAccess();

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(FieldAccess.class, "flag");

        data.flag = true;

        Boolean value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo(Boolean.TRUE);
        memberAccessor.set(data, Boolean.FALSE);
        Assertions.assertThat(data.flag).isFalse();

        value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo(Boolean.FALSE);

        memberAccessor.set(data, Boolean.TRUE);
        Assertions.assertThat(data.flag).isTrue();
    }

    @Test
    void testDate_Field() {
        FieldAccess data = new FieldAccess();

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(FieldAccess.class, "timeStamp");

        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime yesterday = today.minusDays(1);

        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        Date todayAsDate = Date.from(today.toInstant(offset));
        Date yesterdayAsDate = Date.from(yesterday.toInstant(offset));

        data.timeStamp = todayAsDate;

        Date value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo(todayAsDate);
        memberAccessor.set(data, yesterdayAsDate);
        Assertions.assertThat(data.timeStamp).isEqualTo(yesterdayAsDate);

        value = memberAccessor.get(data);
        Assertions.assertThat(value).isEqualTo(yesterdayAsDate);

        memberAccessor.set(data, null);
        Assertions.assertThat(data.timeStamp).isNull();
    }


    @Test
    void testUnknown_Field() {
        Assertions.assertThatThrownBy(() ->
                        new FastPropertyMemberAccessor(FieldAccess.class, "foo"))
                .isInstanceOf(FieldNotFoundException.class)
                .hasMessage("Property 'foo' not found in class class be.atbash.util.reflection.testclasses.FieldAccess");

        // search is case sensitive
        Assertions.assertThatThrownBy(() ->
                        new FastPropertyMemberAccessor(FieldAccess.class, "timestamp"))
                .isInstanceOf(FieldNotFoundException.class)
                .hasMessage("Property 'timestamp' not found in class class be.atbash.util.reflection.testclasses.FieldAccess");
    }

    @Test
    void testMetaData_Field() {
        FieldAccess data = new FieldAccess();

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(FieldAccess.class, "value");
        Assertions.assertThat(memberAccessor.getName()).isEqualTo("value");
        Assertions.assertThat(memberAccessor.getType()).isEqualTo(String.class);
        Assertions.assertThat(memberAccessor.toString()).isEqualTo("Bean property 'value' on class be.atbash.util.reflection.testclasses.FieldAccess");
    }

    @Test
    void testWrongClassAsValue_Field() {
        FieldAccess data = new FieldAccess();

        FastPropertyMemberAccessor memberAccessor = new FastPropertyMemberAccessor(FieldAccess.class, "number");

        Long value = memberAccessor.get(data);
        Assertions.assertThat(value).isNull();
        Assertions.assertThatThrownBy(() ->
                        memberAccessor.set(data, "JUnit"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    //                      Add <T> to FastPropertyMemberAccessor
}
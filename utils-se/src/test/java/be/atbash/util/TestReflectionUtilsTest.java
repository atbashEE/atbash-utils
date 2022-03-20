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

import be.atbash.util.testclasses.Bar;
import be.atbash.util.testclasses.Child;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class TestReflectionUtilsTest {

    @Test
    void getValueOf_child() throws NoSuchFieldException {

        Child child = new Child();

        String childField = TestReflectionUtils.getValueOf(child, "childField");
        Assertions.assertThat(childField).isEqualTo("childValue");

    }

    @Test
    void getValueOf_parent() throws NoSuchFieldException {
        Child child = new Child();

        String parentField = TestReflectionUtils.getValueOf(child, "parentField");
        Assertions.assertThat(parentField).isEqualTo("parentValue");

    }

    @Test
    void getValueOf_static_Child() throws NoSuchFieldException {
        String staticChildField = TestReflectionUtils.getValueOf(Child.class, "staticChildField");
        Assertions.assertThat(staticChildField).isEqualTo("static childValue");

    }

    @Test
    void getValueOf_unknown_field() {
        Assertions.assertThatThrownBy(() -> {
            Child child = new Child();
            TestReflectionUtils.getValueOf(child, "unknown");
        }).isInstanceOf(NoSuchFieldException.class);
    }

    @Test
    void setFieldValue_child() throws NoSuchFieldException {
        Child child = new Child();

        TestReflectionUtils.setFieldValue(child, "childField", "Octopus");

        Assertions.assertThat(child.getChildField()).isEqualTo("Octopus");

    }

    @Test
    void setFieldValue_parent() throws NoSuchFieldException {
        Child child = new Child();

        TestReflectionUtils.setFieldValue(child, "parentField", "Octopus");

        Assertions.assertThat(child.getParentField()).isEqualTo("Octopus");
    }

    @Test
    void setFieldValue_static_Child() throws NoSuchFieldException {

        TestReflectionUtils.setFieldValue(Child.class, "staticChildField", "Octopus");

        Assertions.assertThat(Child.getStaticChildField()).isEqualTo("Octopus");

    }

    @Test
    void setFieldValue_unknown_field() {
        Assertions.assertThatThrownBy(() -> {
            Child child = new Child();
            TestReflectionUtils.setFieldValue(child, "unknown", "Octopus");
        }).isInstanceOf(NoSuchFieldException.class);
    }

    @Test
    void setFieldValue_Assignable() throws NoSuchFieldException {

        Child child = new Child();
        Bar bar = new Bar();
        TestReflectionUtils.setFieldValue(child, "fooValue", bar);

        Assertions.assertThat(child.getFooValue()).isInstanceOf(Bar.class);

    }

    @Test
    void setFieldValue_Assignable_failure() {
        Assertions.assertThatThrownBy(() -> {
            Child child = new Child();
            TestReflectionUtils.setFieldValue(child, "childField", 123L);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void resetOf_child() throws NoSuchFieldException {
        Child child = new Child();

        TestReflectionUtils.resetOf(child, "childField");

        Assertions.assertThat(child.getChildField()).isNull();

    }

    @Test
    void resetOf_parent() throws NoSuchFieldException {
        Child child = new Child();

        TestReflectionUtils.resetOf(child, "parentField");

        Assertions.assertThat(child.getParentField()).isNull();

    }

    @Test
    void resetOf_static_Child() throws NoSuchFieldException {

        TestReflectionUtils.resetOf(Child.class, "staticChildField");

        Assertions.assertThat(Child.getStaticChildField()).isNull();
    }

    @Test
    void resetOf_unknown_field() {
        Assertions.assertThatThrownBy(() -> {
            Child child = new Child();
            TestReflectionUtils.resetOf(child, "unknown");
        }).isInstanceOf(NoSuchFieldException.class);
    }

}
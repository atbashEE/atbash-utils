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
package be.atbash.util;

import be.atbash.util.testclasses.Bar;
import be.atbash.util.testclasses.Child;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestReflectionUtilsTest {

    @Test
    public void getValueOf_child() throws NoSuchFieldException {

        Child child = new Child();

        String childField = TestReflectionUtils.getValueOf(child, "childField");
        assertThat(childField).isEqualTo("childValue");

    }

    @Test
    public void getValueOf_parent() throws NoSuchFieldException {
        Child child = new Child();

        String parentField = TestReflectionUtils.getValueOf(child, "parentField");
        assertThat(parentField).isEqualTo("parentValue");

    }

    @Test
    public void getValueOf_static_Child() throws NoSuchFieldException {
        String staticChildField = TestReflectionUtils.getValueOf(Child.class, "staticChildField");
        assertThat(staticChildField).isEqualTo("static childValue");

    }

    @Test(expected = NoSuchFieldException.class)
    public void getValueOf_unknown_field() throws NoSuchFieldException {
        Child child = new Child();

        TestReflectionUtils.getValueOf(child, "unknown");

    }

    @Test
    public void setFieldValue_child() throws NoSuchFieldException {
        Child child = new Child();

        TestReflectionUtils.setFieldValue(child, "childField", "Octopus");

        assertThat(child.getChildField()).isEqualTo("Octopus");

    }

    @Test
    public void setFieldValue_parent() throws NoSuchFieldException {
        Child child = new Child();

        TestReflectionUtils.setFieldValue(child, "parentField", "Octopus");

        assertThat(child.getParentField()).isEqualTo("Octopus");
    }

    @Test
    public void setFieldValue_static_Child() throws NoSuchFieldException {

        TestReflectionUtils.setFieldValue(Child.class, "staticChildField", "Octopus");

        assertThat(Child.getStaticChildField()).isEqualTo("Octopus");

    }

    @Test(expected = NoSuchFieldException.class)
    public void setFieldValue_unknown_field() throws NoSuchFieldException {
        Child child = new Child();


        TestReflectionUtils.setFieldValue(child, "unknown", "Octopus");

    }

    @Test
    public void setFieldValue_Assignable() throws NoSuchFieldException {

        Child child = new Child();
        Bar bar = new Bar();
        TestReflectionUtils.setFieldValue(child, "fooValue", bar);

        assertThat(child.getFooValue()).isInstanceOf(Bar.class);

    }

    @Test(expected = IllegalArgumentException.class)
    public void setFieldValue_Assignable_failure() throws NoSuchFieldException {
        Child child = new Child();

        TestReflectionUtils.setFieldValue(child, "childField", 123L);


    }

    @Test
    public void resetOf_child() throws NoSuchFieldException {
        Child child = new Child();

        TestReflectionUtils.resetOf(child, "childField");

        assertThat(child.getChildField()).isNull();

    }

    @Test
    public void resetOf_parent() throws NoSuchFieldException {
        Child child = new Child();

        TestReflectionUtils.resetOf(child, "parentField");

        assertThat(child.getParentField()).isNull();

    }

    @Test
    public void resetOf_static_Child() throws NoSuchFieldException {

        TestReflectionUtils.resetOf(Child.class, "staticChildField");

        assertThat(Child.getStaticChildField()).isNull();
    }

    @Test(expected = NoSuchFieldException.class)
    public void resetOf_unknown_field() throws NoSuchFieldException {
        Child child = new Child();

        TestReflectionUtils.resetOf(child, "unknown");

    }


}
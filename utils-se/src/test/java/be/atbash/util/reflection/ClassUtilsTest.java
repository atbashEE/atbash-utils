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

import be.atbash.util.reflection.testclasses.Foo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Calendar;
import java.util.List;

class ClassUtilsTest {

    @Test
    void getConstructor_noArg() {
        Assertions.assertThat(ClassUtils.getConstructor(Foo.class)).isNotNull();
    }

    @Test
    void getConstructor_OneArg() {
        Assertions.assertThat(ClassUtils.getConstructor(Foo.class, String.class)).isNotNull();
    }

    @Test
    void getConstructor_AnyOneArg() {
        Assertions.assertThat(ClassUtils.getConstructor(Foo.class, new Class[]{null})).isNotNull();
    }

    @Test
    void getConstructor_BestMatchArgument() {
        Assertions.assertThat(ClassUtils.getConstructor(Foo.class, Long.class, null)).isNotNull();
    }

    @Test
    public void getConstructor_SubTypeMatchArgument() {
        Assertions.assertThat(ClassUtils.getConstructor(Foo.class, Integer.class, null)).isNotNull();
    }

    @Test
    void getConstructor_NoMatchingArgument() {
        Assertions.assertThatThrownBy(() ->
                ClassUtils.getConstructor(Foo.class, Calendar.class)
        ).isInstanceOf(NoConstructorFoundException.class);
    }

    @Test
    void getConstructor_TooManyMatching() {
        Assertions.assertThatThrownBy(() ->
                ClassUtils.getConstructor(Foo.class, null, null)
        ).isInstanceOf(NoConstructorFoundException.class);
    }

    @Test
    void newInstance_noArg() {
        Foo foo = ClassUtils.newInstance(Foo.class);

        Assertions.assertThat(foo.value).isEqualTo("DefaultConstructor");
        Assertions.assertThat(foo.number).isNull();
        Assertions.assertThat(foo.longValue).isNull();
    }

    @Test
    void newInstance_OneArg() {
        Foo foo = ClassUtils.newInstance(Foo.class, "Atbash");

        Assertions.assertThat(foo.value).isEqualTo("Atbash");
        Assertions.assertThat(foo.number).isNull();
        Assertions.assertThat(foo.longValue).isNull();

    }

    @Test
    void newInstance_BestMatchArgument() {
        Foo foo = ClassUtils.newInstance(Foo.class, 15L, null);

        Assertions.assertThat(foo.value).isNull();
        Assertions.assertThat(foo.number).isNull();
        Assertions.assertThat(foo.longValue).isEqualTo(15L);
    }

    @Test
    void newInstance_SubTypeMatchArgument() {
        Foo foo = ClassUtils.newInstance(Foo.class, 15, null);

        Assertions.assertThat(foo.value).isNull();
        Assertions.assertThat(foo.number).isEqualTo(15);
        Assertions.assertThat(foo.longValue).isNull();
    }

    @Test
    void newInstanceName_noArg() {
        Foo foo = ClassUtils.newInstance(Foo.class.getName());

        Assertions.assertThat(foo.value).isEqualTo("DefaultConstructor");
        Assertions.assertThat(foo.number).isNull();
        Assertions.assertThat(foo.longValue).isNull();
    }

    @Test
    void newInstanceName_OneArg() {
        Foo foo = ClassUtils.newInstance(Foo.class.getName(), "Atbash");

        Assertions.assertThat(foo.value).isEqualTo("Atbash");
        Assertions.assertThat(foo.number).isNull();
        Assertions.assertThat(foo.longValue).isNull();

    }

    @Test
    void newInstanceName_BestMatchArgument() {
        Foo foo = ClassUtils.newInstance(Foo.class.getName(), 15L, null);

        Assertions.assertThat(foo.value).isNull();
        Assertions.assertThat(foo.number).isNull();
        Assertions.assertThat(foo.longValue).isEqualTo(15L);
    }

    @Test
    void newInstanceName_SubTypeMatchArgument() {
        Foo foo = ClassUtils.newInstance(Foo.class.getName(), 15, null);

        Assertions.assertThat(foo.value).isNull();
        Assertions.assertThat(foo.number).isEqualTo(15);
        Assertions.assertThat(foo.longValue).isNull();
    }

    @Test
    void newInstanceName_UnknownClass() {
        Assertions.assertThatThrownBy(() ->
                        ClassUtils.newInstance("some.package.Atbash")
                ).isInstanceOf(UnknownClassException.class)
                .hasMessage("Unable to load class named [some.package.Atbash] from the thread context, current, or system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found.");
    }

    @Test
    void forName_match() {
        Assertions.assertThat(ClassUtils.forName(Foo.class.getName())).isEqualTo(Foo.class);
    }

    @Test
    void forName_noMatch() {
        Assertions.assertThatThrownBy(() ->
                        ClassUtils.forName("some.package.Atbash")
                ).isInstanceOf(UnknownClassException.class)
                .hasMessage("Unable to load class named [some.package.Atbash] from the thread context, current, or system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found.");
    }

    @Test
    void isAvailable_match() {
        Assertions.assertThat(ClassUtils.isAvailable(Foo.class.getName())).isTrue();
    }

    @Test
    void isAvailable_noMatch() {
        Assertions.assertThat(ClassUtils.isAvailable("some.package.Atbash")).isFalse();

    }

    @Test
    void getAllResources() {
        List<URL> resources = ClassUtils.getAllResources("walker/file1");
        Assertions.assertThat(resources).hasSize(1);
        Assertions.assertThat(resources.get(0).getPath()).endsWith("/test-classes/walker/file1");

    }

    @Test
    void getAllResources_noMatch() {
        List<URL> resources = ClassUtils.getAllResources("someRandomFile");
        Assertions.assertThat(resources).isEmpty();

    }
}

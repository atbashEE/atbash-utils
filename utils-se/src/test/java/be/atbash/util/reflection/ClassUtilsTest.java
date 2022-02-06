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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */

public class ClassUtilsTest {

    @Test
    public void getConstructor_noArg() {
        assertThat(ClassUtils.getConstructor(Foo.class)).isNotNull();
    }

    @Test
    public void getConstructor_OneArg() {
        assertThat(ClassUtils.getConstructor(Foo.class, String.class)).isNotNull();
    }

    @Test
    public void getConstructor_AnyOneArg() {
        assertThat(ClassUtils.getConstructor(Foo.class, new Class[]{null})).isNotNull();
    }

    @Test
    public void getConstructor_BestMatchArgument() {
        assertThat(ClassUtils.getConstructor(Foo.class, Long.class, null)).isNotNull();
    }

    @Test
    public void getConstructor_SubTypeMatchArgument() {
        assertThat(ClassUtils.getConstructor(Foo.class, Integer.class, null)).isNotNull();
    }

    @Test
    public void getConstructor_NoMatchingArgument() {
        Assertions.assertThrows(NoConstructorFoundException.class, () -> {
            ClassUtils.getConstructor(Foo.class, Calendar.class);
        });
    }

    @Test
    public void getConstructor_TooManyMatching() {
        Assertions.assertThrows(NoConstructorFoundException.class, () -> {
            ClassUtils.getConstructor(Foo.class, null, null);
        });
    }

    @Test
    public void newInstance_noArg() {
        Foo foo = ClassUtils.newInstance(Foo.class);

        assertThat(foo.value).isEqualTo("DefaultConstructor");
        assertThat(foo.number).isNull();
        assertThat(foo.longValue).isNull();
    }

    @Test
    public void newInstance_OneArg() {
        Foo foo = ClassUtils.newInstance(Foo.class, "Atbash");

        assertThat(foo.value).isEqualTo("Atbash");
        assertThat(foo.number).isNull();
        assertThat(foo.longValue).isNull();

    }

    @Test
    public void newInstance_BestMatchArgument() {
        Foo foo = ClassUtils.newInstance(Foo.class, 15L, null);

        assertThat(foo.value).isNull();
        assertThat(foo.number).isNull();
        assertThat(foo.longValue).isEqualTo(15L);
    }

    @Test
    public void newInstance_SubTypeMatchArgument() {
        Foo foo = ClassUtils.newInstance(Foo.class, 15, null);

        assertThat(foo.value).isNull();
        assertThat(foo.number).isEqualTo(15);
        assertThat(foo.longValue).isNull();
    }

    @Test
    public void newInstanceName_noArg() {
        Foo foo = ClassUtils.newInstance(Foo.class.getName());

        assertThat(foo.value).isEqualTo("DefaultConstructor");
        assertThat(foo.number).isNull();
        assertThat(foo.longValue).isNull();
    }

    @Test
    public void newInstanceName_OneArg() {
        Foo foo = ClassUtils.newInstance(Foo.class.getName(), "Atbash");

        assertThat(foo.value).isEqualTo("Atbash");
        assertThat(foo.number).isNull();
        assertThat(foo.longValue).isNull();

    }

    @Test
    public void newInstanceName_BestMatchArgument() {
        Foo foo = ClassUtils.newInstance(Foo.class.getName(), 15L, null);

        assertThat(foo.value).isNull();
        assertThat(foo.number).isNull();
        assertThat(foo.longValue).isEqualTo(15L);
    }

    @Test
    public void newInstanceName_SubTypeMatchArgument() {
        Foo foo = ClassUtils.newInstance(Foo.class.getName(), 15, null);

        assertThat(foo.value).isNull();
        assertThat(foo.number).isEqualTo(15);
        assertThat(foo.longValue).isNull();
    }

    @Test
    public void newInstanceName_UnknownClass() {
        try {
            ClassUtils.newInstance("some.package.Atbash");
        } catch (UnknownClassException ex) {
            assertThat(ex.getMessage()).isEqualTo("Unable to load class named [some.package.Atbash] from the thread context, current, or system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found.");
        }
    }

    @Test
    public void forName_match() {
        assertThat(ClassUtils.forName(Foo.class.getName())).isEqualTo(Foo.class);
    }

    @Test
    public void forName_noMatch() {
        try {
            ClassUtils.forName("some.package.Atbash");
        } catch (UnknownClassException ex) {
            assertThat(ex.getMessage()).isEqualTo("Unable to load class named [some.package.Atbash] from the thread context, current, or system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found.");
        }
    }

    @Test
    public void isAvailable_match() {
        assertThat(ClassUtils.isAvailable(Foo.class.getName())).isTrue();
    }

    @Test
    public void isAvailable_noMatch() {
        assertThat(ClassUtils.isAvailable("some.package.Atbash")).isFalse();

    }

    @Test
    public void getAllResources() {
        List<URL> resources = ClassUtils.getAllResources("walker/file1");
        assertThat(resources).hasSize(1);
        assertThat(resources.get(0).getPath()).endsWith("/test-classes/walker/file1");

    }

    @Test
    public void getAllResources_noMatch() {
        List<URL> resources = ClassUtils.getAllResources("someRandomFile");
        assertThat(resources).isEmpty();

    }
}

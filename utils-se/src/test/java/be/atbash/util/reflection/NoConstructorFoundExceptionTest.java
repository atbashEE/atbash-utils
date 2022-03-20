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

class NoConstructorFoundExceptionTest {

    @Test
    void hasText_empty() {
        NoConstructorFoundException exception = new NoConstructorFoundException(Foo.class, new Class[0]);
        Assertions.assertThat(exception.getMessage()).isEqualTo("No constructor or more then one found at the class be.atbash.util.reflection.testclasses.Foo having the parameter(s) ()");
    }

    @Test
    void hasText_SingleArgument() {
        NoConstructorFoundException exception = new NoConstructorFoundException(Foo.class, new Class[]{String.class});
        Assertions.assertThat(exception.getMessage()).isEqualTo("No constructor or more then one found at the class be.atbash.util.reflection.testclasses.Foo having the parameter(s) (java.lang.String)");
    }

    @Test
    void hasText_multipleArgument() {
        NoConstructorFoundException exception = new NoConstructorFoundException(Foo.class, new Class[]{Integer.class, String.class});
        Assertions.assertThat(exception.getMessage()).isEqualTo("No constructor or more then one found at the class be.atbash.util.reflection.testclasses.Foo having the parameter(s) (java.lang.Integer, java.lang.String)");
    }

    @Test
    void hasText_nullArgument() {

        NoConstructorFoundException exception = new NoConstructorFoundException(Foo.class, new Class[]{String.class, null, String.class});
        Assertions.assertThat(exception.getMessage()).isEqualTo("No constructor or more then one found at the class be.atbash.util.reflection.testclasses.Foo having the parameter(s) (java.lang.String, null, java.lang.String)");
    }

}

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
package be.atbash.util.reflection

import be.atbash.util.reflection.testclasses.Foo
import spock.lang.Specification

/**
 * Test cases for the message creation of NoConstructorFoundException. The Foo.class is used but the error messages
 * are unrelated to the constructors defined within the class.
 */

class NoConstructorFoundExceptionTest extends Specification {

    def "hasText_empty"() {
        expect:
        new NoConstructorFoundException(Foo, new Class[0]).message == "No constructor found at the class be.atbash.util.reflection.testclasses.Foo having the parameters ()"
    }

    def "hasText_SingleArgument"() {

        expect:
        new NoConstructorFoundException(Foo, [String] as Class[]).message == "No constructor found at the class be.atbash.util.reflection.testclasses.Foo having the parameters (java.lang.String)"
    }

    def "hasText_multipleArgument"() {

        expect:
        new NoConstructorFoundException(Foo, [Integer, String] as Class[]).message == "No constructor found at the class be.atbash.util.reflection.testclasses.Foo having the parameters (java.lang.Integer, java.lang.String)"
    }

    def "hasText_nullArgument"() {

        expect:
        new NoConstructorFoundException(Foo, [String, null, String] as Class[]).message == "No constructor found at the class be.atbash.util.reflection.testclasses.Foo having the parameters (java.lang.String, null, java.lang.String)"
    }

}

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
package be.atbash.util.reflection.testclasses

/**
 *
 */

class Foo {

    String value
    Number number
    Long longValue

    Foo() { value = "DefaultConstructor" }

    Foo(String value) {
        this.value = value
    }

    Foo(Number number, String value) {
        this.number = number
        this.value = value
    }

    Foo(Long longValue, String value) {
        this.longValue = longValue
        this.value = value
    }
}

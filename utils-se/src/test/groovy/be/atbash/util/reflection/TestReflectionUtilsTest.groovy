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

import be.atbash.util.TestReflectionUtils
import be.atbash.util.reflection.testclasses.Bar
import be.atbash.util.reflection.testclasses.Child
import spock.lang.Specification

/**
 *
 */

class TestReflectionUtilsTest extends Specification {
    def "getValueOf child"() {
        when:
        def child = new Child()

        then:
        TestReflectionUtils.getValueOf(child, "childField") == "childValue"

    }

    def "getValueOf parent"() {
        when:
        def child = new Child()

        then:
        TestReflectionUtils.getValueOf(child, "parentField") == "parentValue"

    }

    def "getValueOf static Child"() {

        expect:
        TestReflectionUtils.getValueOf(Child, "staticChildField") == "static childValue"

    }

    def "getValueOf unknown field"() {
        setup:
        def child = new Child()

        when:
        TestReflectionUtils.getValueOf(child, "unknown")

        then:
        thrown NoSuchFieldException

    }

    def "setFieldValue child"() {
        when:
        def child = new Child()
        TestReflectionUtils.setFieldValue(child, "childField", "Octopus")

        then:
        child.childField == "Octopus"

    }

    def "setFieldValue parent"() {
        when:
        def child = new Child()
        TestReflectionUtils.setFieldValue(child, "parentField", "Octopus")

        then:
        child.parentField == "Octopus"
    }

    def "setFieldValue static Child"() {

        when:
        TestReflectionUtils.setFieldValue(Child, "staticChildField", "Octopus")

        then:
        Child.staticChildField == "Octopus"
    }

    def "setFieldValue unknown field"() {
        setup:
        def child = new Child()

        when:
        TestReflectionUtils.setFieldValue(child, "unknown", "Octopus")

        then:
        thrown NoSuchFieldException

    }

    def "setFieldValue Assignable"() {
        when:
        def child = new Child()
        def bar = new Bar()
        TestReflectionUtils.setFieldValue(child, "fooValue", bar)

        then:
        child.fooValue instanceof Bar

    }

    def "setFieldValue Assignable failure"() {
        when:
        def child = new Child()
        TestReflectionUtils.setFieldValue(child, "childField", 123L)

        then:
        thrown IllegalArgumentException

    }

    def "resetOf child"() {
        when:
        def child = new Child()
        TestReflectionUtils.resetOf(child, "childField")

        then:
        child.childField == null

    }

    def "resetOf parent"() {
        when:
        def child = new Child()
        TestReflectionUtils.resetOf(child, "parentField")

        then:
        child.parentField == null
    }

    def "resetOf static Child"() {

        when:
        TestReflectionUtils.resetOf(Child, "staticChildField")

        then:
        Child.staticChildField == null
    }

    def "resetOf unknown field"() {
        setup:
        def child = new Child()

        when:
        TestReflectionUtils.resetOf(child, "unknown")

        then:
        thrown NoSuchFieldException

    }

}

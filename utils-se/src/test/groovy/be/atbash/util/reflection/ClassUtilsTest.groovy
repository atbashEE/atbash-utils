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
 *
 */

class ClassUtilsTest extends Specification {
    def "GetConstructor_noArg"() {
        expect:
        ClassUtils.getConstructor(Foo) != null
    }

    def "GetConstructor_OneArg"() {
        expect:
        ClassUtils.getConstructor(Foo, String) != null
    }

    def "GetConstructor_AnyOneArg"() {
        expect:
        ClassUtils.getConstructor(Foo, [null] as Class[]) != null
    }

    def "GetConstructor_BestMatchArgument"() {
        expect:
        ClassUtils.getConstructor(Foo, [Long, null] as Class[]) != null
    }

    def "GetConstructor_SubTypeMatchArgument"() {
        expect:
        ClassUtils.getConstructor(Foo, [Integer, null] as Class[]) != null
    }

    def "GetConstructor_NoMatchingArgument"() {

        when:
        ClassUtils.getConstructor(Foo, [Calendar] as Class[])

        then:
        def ex = thrown(NoConstructorFoundException)
        ex.message == "No constructor or more then one found at the class be.atbash.util.reflection.testclasses.Foo having the parameter(s) (java.util.Calendar)"
    }

    def "GetConstructor_TooManyMatching"() {

        when:
        ClassUtils.getConstructor(Foo, [null, null] as Class[])

        then:
        def ex = thrown(NoConstructorFoundException)
        ex.message == "No constructor or more then one found at the class be.atbash.util.reflection.testclasses.Foo having the parameter(s) (null, null)"
    }

    def "newInstance_noArg"() {
        when:
        Foo foo = ClassUtils.newInstance(Foo)

        then:
        foo.value == "DefaultConstructor"
        foo.number == null
        foo.longValue == null
    }

    def "newInstance_OneArg"() {
        when:
        Foo foo = ClassUtils.newInstance(Foo, "Atbash")

        then:
        foo.value == "Atbash"
        foo.number == null
        foo.longValue == null

    }

    def "newInstance_BestMatchArgument"() {
        when:
        Foo foo = ClassUtils.newInstance(Foo, 15L, null)

        then:
        foo.value == null
        foo.number == null
        foo.longValue == 15L
    }

    def "newInstance_SubTypeMatchArgument"() {
        when:
        Foo foo = ClassUtils.newInstance(Foo, 15, null)

        then:
        foo.value == null
        foo.number == 15
        foo.longValue == null
    }

    def "newInstanceName_noArg"() {
        when:
        Foo foo = ClassUtils.newInstance(Foo.getName())

        then:
        foo.value == "DefaultConstructor"
        foo.number == null
        foo.longValue == null
    }

    def "newInstanceName_OneArg"() {
        when:
        Foo foo = ClassUtils.newInstance(Foo.getName(), "Atbash")

        then:
        foo.value == "Atbash"
        foo.number == null
        foo.longValue == null

    }

    def "newInstanceName_BestMatchArgument"() {
        when:
        Foo foo = ClassUtils.newInstance(Foo.getName(), 15L, null)

        then:
        foo.value == null
        foo.number == null
        foo.longValue == 15L
    }

    def "newInstanceName_SubTypeMatchArgument"() {
        when:
        Foo foo = ClassUtils.newInstance(Foo.getName(), 15, null)

        then:
        foo.value == null
        foo.number == 15
        foo.longValue == null
    }

    def "newInstanceName_UnknownClass"() {
        when:
        ClassUtils.newInstance("some.package.Atbash")

        then:
        def ex = thrown(UnknownClassException)

        ex.message == "Unable to load class named [some.package.Atbash] from the thread context, current, or system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found."
    }

    def "forName_match"() {
        expect:
        ClassUtils.forName(Foo.getName()) == Foo
    }

    def "forName_noMatch"() {
        when:
        ClassUtils.forName("some.package.Atbash")

        then:
        def ex = thrown(UnknownClassException)

        ex.message == "Unable to load class named [some.package.Atbash] from the thread context, current, or system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found."
    }

    def "isAvailable_match"() {
        expect:
        ClassUtils.isAvailable(Foo.getName())
    }

    def "isAvailable_noMatch"() {
        expect:
        !ClassUtils.isAvailable("some.package.Atbash")

    }
}

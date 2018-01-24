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
package be.atbash.util

import spock.lang.Specification

/**
 *
 */

class CollectionUtilsTest extends Specification {
    def "AsSet"() {
        when:
        Set<String> data =  CollectionUtils.asSet("Value1", "Value2")

        then:
        data.size() == 2
        ++data.iterator() == "Value1"  // ++ is .next() on iterator but is not more readable !!
    }

    def "IsEmpty_emptyCollection"() {
        expect:
        CollectionUtils.isEmpty([])
    }

    /*
    Well, Groovy is unable to work correctly with overloaded methods, bummer.
    def "IsEmpty_nullCollection"() {
        expect:
        CollectionUtils.isEmpty(null as Collection)
    }
    */

    def "IsEmpty_NotEmptyCollection"() {
        expect:
        !CollectionUtils.isEmpty(["Atbash"])
    }

    def "IsEmpty_emptyMap"() {
        expect:
        CollectionUtils.isEmpty([:])
    }

    /*
    Well, Groovy is unable to work correctly with overloaded methods, bummer.
    def "IsEmpty_nullMap"() {
        expect:
        CollectionUtils.isEmpty(null as Map)
    }
    */

    def "IsEmpty_NotEmptyMap"() {
        expect:
        !CollectionUtils.isEmpty(["key":"value"])
    }

    def "Size_emptyCollection"() {
        expect:
        CollectionUtils.size([]) == 0

    }

    /*
    Well, Groovy is unable to work correctly with overloaded methods, bummer.
    def "Size_nullCollection"() {
        expect:
        CollectionUtils.size(null as Collection) == 0

    }
    */

    def "Size_notEmptyCollection"() {
        expect:
        CollectionUtils.size(["Atbash"]) == 1

    }

    def "Size_emptyMap"() {
        expect:
        CollectionUtils.size([:]) == 0
    }

    /*
    Well, Groovy is unable to work correctly with overloaded methods, bummer.
    def "Size_nullMap"() {
        expect:
        CollectionUtils.size(null as Map) == 0

    }
    */

    def "Size_notEmptyMap"() {
        expect:
        CollectionUtils.size(["key":"value"]) == 1

    }

    def "AsList"() {
        when:
        List<String> data =  CollectionUtils.asList("Value1", "Value2")

        then:
        data.size() == 2
        ++data.iterator() == "Value1"  // ++ is .next() on iterator but is not more readable !!
    }

}

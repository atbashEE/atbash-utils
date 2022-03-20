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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class CollectionUtilsTest {

    @Test
    void asSet() {
        Set<String> data = CollectionUtils.asSet("Value1", "Value2");

        Assertions.assertThat(data).hasSize(2)
                .containsExactly("Value1", "Value2");
    }

    @Test
    void isEmpty_emptyCollection() {
        Assertions.assertThat(CollectionUtils.isEmpty(new ArrayList<>())).isTrue();
    }

    @Test
    void isEmpty_emptyCollectionSet() {
        Assertions.assertThat(CollectionUtils.isEmpty(new HashSet<>())).isTrue();
    }

    @Test
    void isEmpty_nullCollection() {
        Assertions.assertThat(CollectionUtils.isEmpty((Collection) null)).isTrue();
    }

    @Test
    void isEmpty_NotEmptyCollection() {
        List<String> data = new ArrayList<>();
        data.add("Atbash");
        Assertions.assertThat(CollectionUtils.isEmpty(data)).isFalse();
    }

    @Test
    void isEmpty_emptyMap() {
        Assertions.assertThat(CollectionUtils.isEmpty(new HashMap<>())).isTrue();
    }

    @Test
    void isEmpty_nullMap() {
        Assertions.assertThat(CollectionUtils.isEmpty((Map) null)).isTrue();
    }

    @Test
    void isEmpty_NotEmptyMap() {
        Map<String, String> data = new HashMap<>();
        data.put("key", "value");
        Assertions.assertThat(CollectionUtils.isEmpty(data)).isFalse();
    }

    @Test
    void size_emptyCollection() {
        Assertions.assertThat(CollectionUtils.size(new ArrayList<>())).isEqualTo(0);
    }

    @Test
    void size_nullCollection() {
        Assertions.assertThat(CollectionUtils.size((Collection) null)).isEqualTo(0);
    }

    @Test
    void size_NotEmptyCollection() {
        List<String> data = new ArrayList<>();
        data.add("Atbash");
        Assertions.assertThat(CollectionUtils.size(data)).isEqualTo(1);
    }

    @Test
    void size_emptyMap() {
        Assertions.assertThat(CollectionUtils.size(new HashMap<>())).isEqualTo(0);
    }

    @Test
    void size_nullMap() {
        Assertions.assertThat(CollectionUtils.size((Map) null)).isEqualTo(0);
    }

    @Test
    void size_NotEmptyMap() {
        Map<String, String> data = new HashMap<>();
        data.put("key", "value");
        Assertions.assertThat(CollectionUtils.size(data)).isEqualTo(1);
    }

    @Test
    void asList() {

        List<String> data = CollectionUtils.asList("Value1", "Value2");

        Assertions.assertThat(data).hasSize(2);
        Assertions.assertThat(data.iterator().next()).isEqualTo("Value1");

    }

}

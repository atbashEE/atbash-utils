/*
 * Copyright 2014-2020 Rudy De Busscher (https://www.atbash.be)
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

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */

public class CollectionUtilsTest {

    @Test
    public void asSet() {
        Set<String> data = CollectionUtils.asSet("Value1", "Value2");

        assertThat(data).hasSize(2);
        assertThat(data).containsExactly("Value1", "Value2");
    }

    @Test
    public void isEmpty_emptyCollection() {
        assertThat(CollectionUtils.isEmpty(new ArrayList())).isTrue();
    }

    @Test
    public void isEmpty_emptyCollectionSet() {
        assertThat(CollectionUtils.isEmpty(new HashSet())).isTrue();
    }

    @Test
    public void isEmpty_nullCollection() {
        assertThat(CollectionUtils.isEmpty((Collection) null)).isTrue();
    }

    @Test
    public void isEmpty_NotEmptyCollection() {
        List<String> data = new ArrayList<>();
        data.add("Atbash");
        assertThat(CollectionUtils.isEmpty(data)).isFalse();
    }

    @Test
    public void isEmpty_emptyMap() {
        assertThat(CollectionUtils.isEmpty(new HashMap())).isTrue();
    }

    @Test
    public void isEmpty_nullMap() {
        assertThat(CollectionUtils.isEmpty((Map) null)).isTrue();
    }

    @Test
    public void isEmpty_NotEmptyMap() {
        Map<String, String> data = new HashMap<>();
        data.put("key", "value");
        assertThat(CollectionUtils.isEmpty(data)).isFalse();
    }

    @Test
    public void size_emptyCollection() {
        assertThat(CollectionUtils.size(new ArrayList())).isEqualTo(0);
    }

    @Test
    public void size_nullCollection() {
        assertThat(CollectionUtils.size((Collection) null)).isEqualTo(0);
    }

    @Test
    public void size_NotEmptyCollection() {
        List<String> data = new ArrayList<>();
        data.add("Atbash");
        assertThat(CollectionUtils.size(data)).isEqualTo(1);
    }

    @Test
    public void size_emptyMap() {
        assertThat(CollectionUtils.size(new HashMap())).isEqualTo(0);
    }

    @Test
    public void size_nullMap() {
        assertThat(CollectionUtils.size((Map) null)).isEqualTo(0);
    }

    @Test
    public void size_NotEmptyMap() {
        Map<String, String> data = new HashMap<>();
        data.put("key", "value");
        assertThat(CollectionUtils.size(data)).isEqualTo(1);
    }

    @Test
    public void asList() {

        List<String> data = CollectionUtils.asList("Value1", "Value2");

        assertThat(data).hasSize(2);
        assertThat(data.iterator().next()).isEqualTo("Value1");

    }

}

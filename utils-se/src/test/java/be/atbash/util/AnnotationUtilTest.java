/*
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
package be.atbash.util;

import be.atbash.util.testclasses.Child;
import be.atbash.util.testclasses.MyAnnotation;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AnnotationUtilTest {

    @Test
    public void getAnnotation_available() {
        assertThat(AnnotationUtil.getAnnotation(Child.class, MyAnnotation.class)).isNotNull();
    }

    @Test
    public void getAnnotation_notAvailable() {
        assertThat(AnnotationUtil.getAnnotation(Child.class, PublicAPI.class)).isNull();
    }
}
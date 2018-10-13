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
package be.atbash.util.ordered;

import be.atbash.util.AnnotationUtil;

import java.util.Comparator;

public class OrderComparator implements Comparator<Object> {

    @Override
    public int compare(Object o1, Object o2) {
        Order annotation1 = AnnotationUtil.getAnnotation(o1.getClass(), Order.class);
        Order annotation2 = AnnotationUtil.getAnnotation(o2.getClass(), Order.class);

        if (annotation1 == null) {
            throw new MissingOrderException(o1.getClass());
        }
        Long order1 = annotation1.value();

        if (annotation2 == null) {
            throw new MissingOrderException(o2.getClass());
        }
        Long order2 = annotation2.value();

        return order1.compareTo(order2);
    }
}

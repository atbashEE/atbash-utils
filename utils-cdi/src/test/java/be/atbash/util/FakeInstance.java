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

import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Required to wrap the instances we have for a certain class type within an {@link Instance} of CDI.
 */
public class FakeInstance<T> implements Instance<T> {

    private List<T> beans;

    FakeInstance(List<T> beans) {

        if (beans == null) {
            this.beans = new ArrayList<>();
        } else {
            this.beans = beans;
        }
    }

    @Override
    public Instance<T> select(Annotation... qualifiers) {
        // TODO When using chained methods (first select and then narrow with qualifiers)
        return null;
    }

    @Override
    public <U extends T> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
        throw new UnsupportedOperationException("Not implemented be.atbash.ee.jsf.jerry.util.cdi.FakeInstance.select(java.lang.Class<U>, java.lang.annotation.Annotation...)");
    }

    @Override
    public <U extends T> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
        throw new UnsupportedOperationException("Not implemented be.atbash.ee.jsf.jerry.util.cdi.FakeInstance.select(javax.enterprise.util.TypeLiteral<U>, java.lang.annotation.Annotation...)");
    }

    @Override
    public boolean isUnsatisfied() {
        return beans.isEmpty();
    }

    @Override
    public boolean isAmbiguous() {
        return beans.size() > 1;
    }

    @Override
    public void destroy(T instance) {

    }

    @Override
    public Iterator<T> iterator() {
        return beans.iterator();
    }

    @Override
    public T get() {
        return beans.iterator().next();
    }
}

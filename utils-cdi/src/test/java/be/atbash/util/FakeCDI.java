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
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.CDIProvider;
import javax.enterprise.util.TypeLiteral;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This is actually is the Fake CDI system. Partial implementation, needs to be extended.
 */
public class FakeCDI extends CDI<Object> {

    private final BeanManager beanManager;
    private final Map<Class<?>, List<Object>> registeredObjects;

    FakeCDI(BeanManager beanManager, Map<Class<?>, List<Object>> registeredObjects) {
        this.beanManager = beanManager;
        this.registeredObjects = registeredObjects;

        // Here we register ourself as provider.
        final CDI<Object> cdiInstance = this;
        configuredProvider = new CDIProvider() {
            @Override
            public CDI<Object> getCDI() {
                return cdiInstance;
            }
        };

    }

    @Override
    public BeanManager getBeanManager() {
        return beanManager;
    }

    @Override
    public Instance<Object> select(Annotation... qualifiers) {
        // TODO
        throw new UnsupportedOperationException("Not implemented be.atbash.ee.jsf.jerry.util.cdi.FakeCDI.select(java.lang.annotation.Annotation...)");

    }

    @Override
    public <U extends Object> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
        // TODO Support Qualifiers
        return new FakeInstance<>((List<U>) registeredObjects.get(subtype));

    }

    @Override
    public <U extends Object> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
        // TODO
        throw new UnsupportedOperationException("Not implemented be.atbash.ee.jsf.jerry.util.cdi.FakeCDI.select(javax.enterprise.util.TypeLiteral<U>, java.lang.annotation.Annotation...)");
    }

    @Override
    public boolean isUnsatisfied() {
        return false;
    }

    @Override
    public boolean isAmbiguous() {
        return true;
    }

    @Override
    public void destroy(Object instance) {

    }

    @Override
    public Iterator<Object> iterator() {
        // TODO ??
        return null;
    }

    @Override
    public Object get() {
        // TODO ?
        return null;
    }
}

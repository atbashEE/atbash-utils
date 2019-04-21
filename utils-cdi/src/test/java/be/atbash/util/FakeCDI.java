/*
 * Copyright 2014-2019 Rudy De Busscher (https://www.atbash.be)
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

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.CDIProvider;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

/**
 * This is actually is the Fake CDI system. Partial implementation, needs to be extended.
 */
public class FakeCDI extends CDI<Object> {

    private final BeanManager beanManager;
    private final Map<Class<?>, List<Object>> registeredObjects;
    private final Map<AnnotationLiteral<?>, List<Object>> registeredLiterals;


    FakeCDI(BeanManager beanManager, Map<Class<?>, List<Object>> registeredObjects,
       Map<AnnotationLiteral<?>, List<Object>> registeredLiterals) {
        this.beanManager = beanManager;
        this.registeredObjects = registeredObjects;
        this.registeredLiterals = registeredLiterals;

        // Here we register ourself as provider.
        final CDI<Object> cdiInstance = this;
        configuredProvider = () -> cdiInstance;

    }

    @Override
    public BeanManager getBeanManager() {
        return beanManager;
    }

    @Override
    public Instance<Object> select(Annotation... qualifiers) {

        if(qualifiers != null && qualifiers.length > 0) {
            return new FakeInstance<>(registeredLiterals.get(qualifiers[0]));
        }

        throw new RuntimeException("Empty qualifier received");
    }

    @Override
    public <U> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
        // TODO Support Qualifiers
        return new FakeInstance<>((List<U>) registeredObjects.get(subtype));

    }

    @Override
    public <U> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
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

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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Wraps our instance within a CDI bean definition.
 */
public class FakeBean<T> implements Bean<T> {

    private Object realBean;

    FakeBean(Object realBean) {
        this.realBean = realBean;
    }

    Object getRealBean() {
        return realBean;
    }

    @Override
    public Set<Type> getTypes() {
        // TODO ?
        return null;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        // TODO
        return null;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        // Should we be able to specify the scopes? Within tests we don't have any scope
        return ApplicationScoped.class;
    }

    @Override
    public String getName() {
        // TODO ? for those named beans?
        return null;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return null;
    }

    @Override
    public Class<?> getBeanClass() {
        return null;
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return null;
    }

    @Override
    public T create(CreationalContext<T> creationalContext) {
        return null;
    }

    @Override
    public void destroy(T t, CreationalContext<T> creationalContext) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FakeBean)) {
            return false;
        }

        FakeBean fakeBean = (FakeBean) o;

        if (!realBean.equals(fakeBean.realBean)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return realBean.hashCode();
    }
}

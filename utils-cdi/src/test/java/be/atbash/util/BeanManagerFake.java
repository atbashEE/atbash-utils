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

import be.atbash.util.exception.AtbashIllegalActionException;
import be.atbash.util.literal.AnyLiteral;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.util.AnnotationLiteral;

import static org.mockito.Mockito.*;

/**
 * Fake CDI provider for unit tests. It can be used to define a mock {@link BeanManager} which holds CDI beans compatible
 * with a real CDI system.
 */
@PublicAPI
public class BeanManagerFake {

    private BeanManager beanManagerMock;

    // beans attached to a Class.
    private Map<Class<?>, List<Object>> registeredObjects;

    // beans attached to instances of AnnotationLiteral
    private Map<AnnotationLiteral<?>, List<Object>> registeredLiterals;

    // Named beans
    private Map<String, Object> registeredBeans;

    public BeanManagerFake() {
        this(mock(BeanManager.class));
    }

    public BeanManagerFake(BeanManager beanManager) {
        beanManagerMock = beanManager;

        registeredObjects = new HashMap<>();
        registeredBeans = new HashMap<>();
        registeredLiterals = new HashMap<>();

        new FakeCDI(beanManagerMock, registeredObjects, registeredLiterals);
    }

    public void registerBean(Object instance, Class<?>... typesToRegister) {
        if (typesToRegister == null || typesToRegister.length == 0) {
            throw new AtbashIllegalActionException("(CDI-DEV-51) typesToRegister is required to have at least 1 Class type.");
        }
        if (instance == null) {
            throw new AtbashIllegalActionException("(CDI-DEV-52) Can't register a null instance");
        }
        // TODO should we test that instance can be assigned to typesToRegister?
        for (Class<?> typeToRegister : typesToRegister) {
            List<Object> objects = registeredObjects.computeIfAbsent(typeToRegister, k -> new ArrayList<>());
            objects.add(instance);
        }
    }

    public void registerBean(String beanName, Object instance) {
        registeredBeans.put(beanName, instance);
    }

    public void registerBean(AnnotationLiteral beanName, Object instance) {
        registeredLiterals.put(beanName, new ArrayList<>(Collections.singletonList(instance)));
    }

    public void endRegistration() {
        for (Map.Entry<Class<?>, List<Object>> entry : registeredObjects.entrySet()) {
            Set<Bean<?>> beans = new HashSet<>();
            for (Object obj : entry.getValue()) {
                beans.add(new FakeBean<>(obj));
            }
            doReturn(beans).when(beanManagerMock).getBeans(entry.getKey(), new AnyLiteral());

            for (Bean<?> bean : beans) {

                doReturn(((FakeBean) bean).getRealBean()).when(beanManagerMock).getReference(bean, entry.getKey(), null);
            }
        }

        doAnswer(invocationOnMock -> {
            Set arg = (Set) invocationOnMock.getArguments()[0];
            return arg.iterator().next();
        }).when(beanManagerMock).resolve(anySet());

        for (Map.Entry<String, Object> entry : registeredBeans.entrySet()) {
            Set<Bean<?>> beans = new HashSet<>();
            Bean<?> bean = new FakeBean<>(entry.getValue());
            beans.add(bean);

            doReturn(beans).when(beanManagerMock).getBeans(entry.getKey());

            doReturn(entry.getValue()).when(beanManagerMock).getReference(bean, Object.class, null);
        }

    }

    public void deregistration() {

        /*
        try {
            Field field = BeanManagerProvider.class.getDeclaredField("bmpSingleton");
            field.setAccessible(true);
            field.set(null, null); // set null to the static field (instance == null)
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        */

        reset(beanManagerMock);
        beanManagerMock = null;
    }
}

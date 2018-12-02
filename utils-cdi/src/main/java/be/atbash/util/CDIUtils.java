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

import be.atbash.util.exception.AtbashIllegalActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Utility methods usable in a CDI 1.1 environment (Java EE 7+)
 */
public final class CDIUtils {

    // All the CDI Bena producer methods for a certain type.
    private static final Map<Class<?>, Method> OPTIONAL_BEAN_INFO = new HashMap<>();
    //
    private static final Map<Class<?>, Object> OPTIONAL_BEAN = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(CDIUtils.class);

    /**
     * Singleton style class.
     */
    private CDIUtils() {
    }

    /**
     * Retrieve all CDI instances which have the classType in their bean definition. The list is an unmodifiable version.
     *
     * @param classType  a {@link java.lang.Class} representing the required type.
     * @param qualifiers the additional required qualifiers.
     * @param <T>        Generic Type argument
     * @return List of all CDI instances matching the class type and qualifiers (if specified) or empty list if no beans are found.
     */
    public static <T> List<T> retrieveInstances(Class<T> classType, Annotation... qualifiers) {
        List<T> result = new ArrayList<>();
        for (T t : CDI.current().select(classType, qualifiers)) {
            result.add(t);
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Retrieve the single CDI instance which has the classType in the bean definition. It throws the standard CDI exceptions
     * in case when there are no or multiple beans which are a candidate for the type.
     *
     * @param classType  a {@link java.lang.Class} representing the required type
     * @param qualifiers the additional required qualifiers.
     * @param <T>        Generic Type argument
     * @return CDI instance matching the class type and qualifiers (if specified).
     * @throws javax.enterprise.inject.AmbiguousResolutionException When more then 1 bean is found in the match
     * @throws UnsatisfiedResolutionException                       When no bean is found in the match.
     */
    public static <T> T retrieveInstance(Class<T> classType, Annotation... qualifiers) {
        Instance<T> instance = CDI.current().select(classType, qualifiers);
        if (instance.isUnsatisfied()) {
            // TODO Better error message
            throw new UnsatisfiedResolutionException(String.format("No bean found for class %s and qualifiers %s", classType.getName(), qualifiers));
        }
        return instance.get();
    }

    /**
     * Retrieve the Single CDI instance which has the classType in the bean definition or null when no CDI beans found.
     * When there are multiple matching beans, the standard CDI AmbiguousResolutionException is thrown.
     * Method also tries to find the bean when created by a Producer method and bean has generic type information.
     *
     * @param classType  a {@link java.lang.Class} representing the required type
     * @param qualifiers the additional required qualifiers.
     * @param <T>        Generic Type argument
     * @return CDI instance matching the class type and qualifiers (if specified) or null when no bean found.
     * @throws javax.enterprise.inject.AmbiguousResolutionException When more then 1 bean is found in the match
     */
    public static <T> T retrieveOptionalInstance(Class<T> classType, Annotation... qualifiers) {
        Instance<T> instance = CDI.current().select(classType, qualifiers);
        return instance.isUnsatisfied() ? getBeanFromProducer(classType) : instance.get();
    }

    /**
     * Returns the {@link BeanManager} which is basically a shorthand for <code>CDI.current().getBeanManager()</code>.
     *
     * @return The {@link BeanManager} for the current CDI context.
     */
    public static BeanManager getBeanManager() {
        return CDI.current().getBeanManager();
    }

    /**
     * Retrieve the CDI bean assigned the bean name in the context. The bean must also be assignable to the targetClass
     * class. When the retrieved bean is not assignable, {@link UnsatisfiedResolutionException} is thrown (instead of the
     * {@link ClassCastException} by the JVM). When no bean is found, {@link UnsatisfiedResolutionException} is also thrown.
     *
     * @param beanName    The name of the bean we are interested in.
     * @param targetClass The class this bean must have (or subclass)
     * @param <T>         Generic Type argument
     * @return The CDI bean or exception when an issue in retrieving the bean.
     * @throws UnsatisfiedResolutionException When instance cannot be found or is assignable to the requested type.
     */
    public static <T> T retrieveInstanceByName(String beanName, Class<T> targetClass) {
        if (StringUtils.isEmpty(beanName)) {
            throw new AtbashIllegalActionException("(CDI-DEV-01) beanName parameter can't be null or empty.");
        }
        BeanManager beanManager = getBeanManager();
        Iterator<Bean<?>> iterator = beanManager.getBeans(beanName).iterator();
        if (!iterator.hasNext()) {
            throw new UnsatisfiedResolutionException(String.format("No bean with name '%s' found.", beanName));
        }
        Bean bean = iterator.next();
        CreationalContext ctx = beanManager.createCreationalContext(bean);
        Object o = beanManager.getReference(bean, Object.class, ctx);
        if (targetClass.isAssignableFrom(o.getClass())) {
            return (T) o;
        } else {
            throw new UnsatisfiedResolutionException(String.format("No bean with name '%s' and assignable to %s found.", beanName, targetClass.getName()));
        }
    }

    /**
     * Fire an event and notify observers. A short hand for writing <code>CDI.current().getBeanManager().fireEvent</code>.
     *
     * @param event      the event object
     * @param qualifiers the event qualifiers
     */
    public static void fireEvent(Object event, Annotation... qualifiers) {
        getBeanManager().fireEvent(event, qualifiers);
    }

    /**
     * Keeps track of all the CDI Bean producer methods.
     * @param producerMethod Method which produces a CDI bean (@Produces present)
     */
    public static void registerProducerMethod(Method producerMethod) {
        if (producerMethod.getAnnotation(ApplicationScoped.class) == null && producerMethod.getDeclaringClass().getAnnotation(ApplicationScoped.class) == null) {
            throw new AtbashIllegalActionException("(CDI-DEV-53) Only CDI bean Producer methods with scope @ApplicationScoped can be registered");
        }
        OPTIONAL_BEAN_INFO.put(producerMethod.getReturnType(), producerMethod);
    }

    private static <T> T getBeanFromProducer(Class<T> targetClass) {
        T result = null;

        if (OPTIONAL_BEAN.containsKey(targetClass)) {
            result = (T) OPTIONAL_BEAN.get(targetClass);
        } else {
            if (OPTIONAL_BEAN_INFO.containsKey(targetClass)) {
                Method method = OPTIONAL_BEAN_INFO.get(targetClass);
                Object bean = CDIUtils.retrieveInstance(method.getDeclaringClass());
                try {
                    result = (T) method.invoke(bean);
                    OPTIONAL_BEAN.put(targetClass, result);
                } catch (IllegalAccessException e) {
                    LOGGER.error("Exception occured during invocation of producer method", e);
                } catch (InvocationTargetException e) {
                    LOGGER.error("Exception occured during invocation of producer method", e);
                    // TODO The original code ran in full container and thus we had EJB access in this method
                    // The idea is to thrown the original Octopus UnauthorizedException when Producer methods call fails
                    // due to authorization issues.
                    /*
                    if (e.getTargetException() instanceof EJBException) {
                        EJBException ejbException = (EJBException) e.getTargetException();
                        if (ejbException.getCause() instanceof OctopusUnauthorizedException) {
                            throw (OctopusUnauthorizedException) ejbException.getCause();
                        }
                    }
                    */
                }
            }
        }
        return result;

    }

}

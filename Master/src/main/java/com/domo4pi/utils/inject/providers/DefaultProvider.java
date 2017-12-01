package com.domo4pi.utils.inject.providers;


import com.domo4pi.utils.inject.Inject;
import com.domo4pi.utils.inject.Injector;
import com.domo4pi.utils.inject.exceptions.MultipleElegibleConstructorException;
import com.domo4pi.utils.inject.exceptions.NonAPhysicClassException;
import com.domo4pi.utils.inject.exceptions.NonElegibleConstructorException;
import com.domo4pi.utils.inject.exceptions.NonPublicConstructorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class DefaultProvider<T> implements Provider<T> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final Class<? extends T> instanceClass;
    private Constructor<T> constructor;

    public DefaultProvider(Class<T> instanceClass) {
        this.instanceClass = instanceClass;
    }

    @Override
    public Class<? extends T> getInstanceClass() {
        return instanceClass;
    }

    @Override
    public T getInstance(Injector injector) {
        if (constructor == null) {
            constructor = getConstructor();
        }

        T instance;

        try {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            Object[] params = new Object[paramTypes.length];

            for(int i = 0;i < params.length; i++){
                params[i] = injector.getInstance(paramTypes[i]);
            }

            instance = constructor.newInstance(params);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return instance;
    }

    public Constructor<T> getConstructor() {
        if ((instanceClass.isInterface()) || (Modifier.isAbstract(instanceClass.getModifiers()))) {
            throw new NonAPhysicClassException(instanceClass);
        }

        Constructor<T> candidateConstructor = getCandidateConstructor();

        if (candidateConstructor == null) {
            throw new NonElegibleConstructorException(instanceClass);
        }

        if (!Modifier.isPublic(candidateConstructor.getModifiers())) {
            throw new NonPublicConstructorException(instanceClass);
        }

        return candidateConstructor;
    }

    @SuppressWarnings("unchecked")
    private Constructor<T> getCandidateConstructor() {
        Constructor<T>[] availableConstructors = (Constructor<T>[]) instanceClass.getDeclaredConstructors();

        Constructor<T> candidateConstructor = null;
        Constructor<T> defaultConstructor = null;

        for (Constructor<T> availableConstructor : availableConstructors) {
            if (availableConstructor.getParameterTypes().length == 0) {
                defaultConstructor = availableConstructor;
            }
            Inject inject = availableConstructor.getAnnotation(Inject.class);

            if (inject != null) {
                if (candidateConstructor == null) {
                    candidateConstructor = availableConstructor;
                } else {
                    throw new MultipleElegibleConstructorException(instanceClass);
                }
            }
        }

        if (candidateConstructor == null) {
            candidateConstructor = defaultConstructor;

        }
        return candidateConstructor;
    }
}

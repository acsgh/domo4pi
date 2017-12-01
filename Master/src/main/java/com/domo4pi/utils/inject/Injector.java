package com.domo4pi.utils.inject;

import com.domo4pi.utils.inject.exceptions.InjectFinalFieldException;
import com.domo4pi.utils.inject.providers.DefaultProvider;
import com.domo4pi.utils.inject.providers.Provider;
import com.domo4pi.utils.inject.providers.SingletonProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Injector {

    private Map<Class<?>, Bind<?>> bindings = new HashMap<>();

    public Injector clone() {
        Injector injector = new Injector();

        injector.bindings = new HashMap<>(bindings);

        return injector;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> instanceClass) {
        T instance;
        if (instanceClass == Injector.class) {
            instance = (T) this;
        } else {
            Bind<T> binding = (Bind<T>) bindings.get(instanceClass);

            if (binding == null) {
                binding = bind(instanceClass);
            }
            instance = binding.getInstance();
            injectMembers(instance);
        }
        return instance;
    }

    public void injectMembers(Object object) {
        Class<?> parent = object.getClass();

        while (parent != null) {

            Field[] fields = parent.getDeclaredFields();

            for (Field field : fields) {
                Inject inject = field.getAnnotation(Inject.class);

                if (inject != null) {
                    if (Modifier.isFinal(field.getModifiers())) {
                        throw new InjectFinalFieldException(parent, field);
                    } else {
                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);

                        try {
                            field.set(object, getInstance(field.getType()));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }

                        field.setAccessible(accessible);
                    }
                }
            }

            parent = parent.getSuperclass();
        }
    }

    public <T> Bind<T> bind(Class<T> instanceClass) {
        Bind<T> bind = new Bind<>(instanceClass, new DefaultProvider<>(instanceClass));

        bindings.put(instanceClass, bind);

        return bind;
    }

    public class Bind<T> {

        protected final Class<T> instanceClass;
        protected final Provider<? extends T> provider;

        public Bind(Class<T> instanceClass, Provider<? extends T> provider) {
            this.instanceClass = instanceClass;
            this.provider = provider;
        }

        public Bind<T> as(Class<? extends T> implementationClass) {
            Bind<T> bind = new Bind<>(instanceClass, new DefaultProvider<>(implementationClass));
            bindings.put(instanceClass, bind);
            return bind;
        }

        public Bind<T> singleton() {
            Bind<T> bind = new Bind<>(instanceClass, new SingletonProvider<>(provider.getInstanceClass()));
            bindings.put(instanceClass, bind);
            return bind;
        }

        public Bind<T> provided(Provider<T> provider) {
            Bind<T> bind = new Bind<>(instanceClass, provider);
            bindings.put(instanceClass, bind);
            return bind;
        }

        @SuppressWarnings("unchecked")
        public Bind<T> singleton(T implementation) {
            Bind<T> bind = new Bind<>(instanceClass, new SingletonProvider<>((Class<T>) provider.getInstanceClass(), implementation));
            bindings.put(instanceClass, bind);
            return bind;
        }


        private T getInstance() {
            return provider.getInstance(Injector.this);
        }
    }
}

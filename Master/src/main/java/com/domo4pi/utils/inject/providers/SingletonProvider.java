package com.domo4pi.utils.inject.providers;


import com.domo4pi.utils.inject.Injector;

public class SingletonProvider<T> extends DefaultProvider<T> {

    protected T instance;

    public SingletonProvider(Class<T> instanceClass) {
        super(instanceClass);
    }

    public SingletonProvider(Class<T> instanceClass, T instance) {
        super(instanceClass);
        this.instance = instance;
    }

    @Override
    public T getInstance(Injector injector) {
        if (instance == null) {
            instance = super.getInstance(injector);
        }

        return instance;
    }
}

package com.domo4pi.utils.inject.providers;


import com.domo4pi.utils.inject.Injector;

public interface Provider<T> {
    public T getInstance(Injector injector);

    public Class<? extends T> getInstanceClass();
}

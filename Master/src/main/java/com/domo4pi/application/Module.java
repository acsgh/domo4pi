package com.domo4pi.application;


import com.domo4pi.utils.inject.Injector;

public interface Module {
    public void configure();
    public void start();
    public void stop();
    public void configureInjection(Injector injector);
}

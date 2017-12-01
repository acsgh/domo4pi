package com.domo4pi.utils.inject.exceptions;

public class NonAPhysicClassException extends IllegalArgumentException {
    public NonAPhysicClassException(Class<?> entityClass) {
        super("The class " + entityClass.getName() + " is either abstract or an interface please define an implementation");
    }
}

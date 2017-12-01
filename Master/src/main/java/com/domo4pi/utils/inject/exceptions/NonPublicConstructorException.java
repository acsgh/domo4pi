package com.domo4pi.utils.inject.exceptions;

public class NonPublicConstructorException extends IllegalArgumentException {
    public NonPublicConstructorException(Class<?> entityClass) {
        super("The class " + entityClass.getName() + " doesn't have a public constructor");
    }

}

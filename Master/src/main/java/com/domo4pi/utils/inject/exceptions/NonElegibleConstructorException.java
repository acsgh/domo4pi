package com.domo4pi.utils.inject.exceptions;

public class NonElegibleConstructorException extends IllegalArgumentException {
    public NonElegibleConstructorException(Class<?> entityClass) {
        super("The class " + entityClass.getName() + " doesn't have an non-param constructor or a param constructor annotated with @Inject");
    }
}

package com.domo4pi.utils.inject.exceptions;

public class MultipleElegibleConstructorException extends IllegalArgumentException {
    public MultipleElegibleConstructorException(Class<?> entityClass) {
        super("The class " + entityClass.getName() + " has multiple constructor annotated with @Inject");
    }
}

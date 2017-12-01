package com.domo4pi.utils.inject.exceptions;

import java.lang.reflect.Field;

public class InjectFinalFieldException extends IllegalArgumentException {
    public InjectFinalFieldException(Class<?> entityClass, Field field) {
        super("The class " + entityClass.getName() + " has an annotated @Inject field " + field.getName() + " which is final");
    }
}

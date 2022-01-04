package com.code;

import java.lang.reflect.InvocationTargetException;

public interface Factory<E> {
    public E make(Object...objects) throws InvocationTargetException, InstantiationException, IllegalAccessException;
}

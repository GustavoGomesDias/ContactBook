package com.contact;

import com.code.Factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ContactFactory<E> implements Factory<E> {

    private final Class<E> cl;

    public ContactFactory(Class<E> cl) {
        this.cl = cl;
    }

    @Override
    public E make(Object... objects) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        try {
            Constructor<? extends E> constructor =  cl.getDeclaredConstructor(String.class, String.class, String.class, String.class);
            return constructor.newInstance((String) objects[0], (String) objects[1], (String) objects[2], (String) objects[3]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}

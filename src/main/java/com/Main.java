package com;

import com.code.Buffer;
import com.contact.Contact;
import com.ifes.tpa.GenData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        // GenData fake = new GenData();
        // fake.populateFile();

        Class<?> types = Contact.class.getComponentType();
//        Class[] parameters = new Class[types.length];
//
//        for (int i = 0; i < types.length; i++) {
//            System.out.println(types[i].getClass());
//            parameters[i] = types[i].getClass();
//        }

        Buffer<Contact> buffer = new Buffer<Contact>(1, 1, "", Contact.class.getDeclaredConstructor(String.class, String.class, String.class, String.class));
        buffer.testAnnotationAndTypes();
    }
}

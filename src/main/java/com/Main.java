package com;

import com.code.Buffer;
import com.contact.Contact;
import com.contact.ContactFactory;
import com.ifes.tpa.GenData;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException {
        // GenData fake = new GenData();
        // fake.populateFile();
        ContactFactory<Contact> wrapper = new ContactFactory<>(Contact.class);

        // TODO: Testar se funciona dessa forma
        Buffer<Contact> buffer = new Buffer<Contact>(100, 1, "./results/test.txt", wrapper);
        buffer.load(",");
        buffer.writeFile("getFullName", "getPhoneNumber", "getCity", "getCountry");
    }
}

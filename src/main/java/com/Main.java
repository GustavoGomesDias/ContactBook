package com;

import com.code.BufferManager;
import com.code.FileManager;
import com.contact.Contact;
import com.contact.ContactFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException {
        // GenData fake = new GenData();
        // fake.populateFile();
        ContactFactory<Contact> wrapper = new ContactFactory<>(Contact.class);

//        Buffer<Contact> buffer = new Buffer<Contact>(100, 1, "./results/test.txt", wrapper);
//        buffer.load(",");
//        buffer.writeFile("getFullName", "getPhoneNumber", "getCity", "getCountry");

        BufferManager bufferManager = new BufferManager("./results/test.txt");

        bufferManager.handleSplitFile();

    }
}

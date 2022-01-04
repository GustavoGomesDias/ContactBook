package com;

import com.code.Buffer;
import com.contact.Contact;
import com.contact.ContactFactory;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        // GenData fake = new GenData();
        // fake.populateFile();

        ContactFactory<Contact> wrapper = new ContactFactory<>(Contact.class);

        Buffer<Contact> buffer = new Buffer<Contact>(1, 1, "", wrapper);
    }
}

package com.contact;

import com.code.Buffer;
import com.code.BufferAction;
import com.code.IDataBuffer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ContactBuffer implements IDataBuffer<Contact> {

    public Buffer<Contact> build(long byteSize, long actualPos, String filePath, BufferAction bufferAction) {
        ContactFactory<Contact> contactFactory = new ContactFactory<>(Contact.class);
        return new Buffer<>(byteSize, actualPos, filePath, contactFactory, bufferAction);
    }

    public void writeFile(Buffer<Contact> buffer) {
        try {
            buffer.writeFile("getFullName", "getPhoneNumber", "getCity", "getCountry");
        } catch (IOException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}

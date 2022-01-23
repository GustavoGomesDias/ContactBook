package com.contact;

import com.code.buffer.Buffer;
import com.code.buffer.BufferAction;
import com.code.buffer.IDataBuffer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ContactBuffer implements IDataBuffer<Contact> {

    public Buffer<Contact> build(long byteSize, long actualPos, String filePath, BufferAction bufferAction, String fileOut) {
        ContactFactory<Contact> contactFactory = new ContactFactory<>(Contact.class);
        return new Buffer<>(byteSize, actualPos, filePath, contactFactory, bufferAction, fileOut);
    }

    public String writeFile(Buffer<Contact> buffer) {
        try {
            return buffer.writeFile("getFullName", "getPhoneNumber", "getCity", "getCountry");
        } catch (IOException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

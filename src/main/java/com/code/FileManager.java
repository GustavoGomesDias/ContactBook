package com.code;

import com.contact.Contact;
import com.contact.ContactFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/*
* TODO: Dividir arquivo
* https://www.guj.com.br/t/dividir-arquivos-em-pedacos-particoes/367336/5
* https://www.guj.com.br/t/ler-arquivo-ate-certo-determiando-tamanho/138970/8
* Splitando aproximadamente
*   */

public class FileManager {
    private String filePath;
    private int byteSize;
    private long actualPos;
    private final ArrayList<String> outFilePathList = new ArrayList<>();

    public FileManager(String filePath, int byteSize) {
        this.filePath = filePath;
        this.byteSize = byteSize;
        this.actualPos = 0;

    }

    public void splitFile() throws IOException {
        try {
            ContactFactory<Contact> contactFactory = new ContactFactory<>(Contact.class);

            Buffer<Contact> buffer = new Buffer<Contact>(this.byteSize, this.actualPos, this.filePath, contactFactory);
            buffer.load(",");
            this.setActualPos(buffer.getFinalPos());
            this.outFilePathList.add(buffer.getOutFileName());
            buffer.writeFile("getFullName", "getPhoneNumber", "getCity", "getCountry");
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public int getByteSize() {
        return byteSize;
    }

    public void setByteSize(int byteSize) {
        this.byteSize = byteSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getActualPos() {
        return actualPos;
    }

    public void setActualPos(long actualPos) {
        this.actualPos = actualPos;
    }

    public ArrayList<String> getOutFilePathList() {
        return outFilePathList;
    }
}

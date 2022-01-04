package com.code;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Buffer<E> extends ArrayList<E> {
    private long maxSize;
    private long actualPos;
    private File file;
    private final Factory<E> factory;

    public Buffer(long maxSize, long actualPos, String filePath, Factory<E> factory) {
        this.maxSize = maxSize;
        this.actualPos = actualPos;
        this.file = new File(filePath);
        this.factory = factory;
    }

    public void load(String split) {
        try {
            if (this.file.exists()) {
                System.out.println("Arquivo de entrada não existe!");
                return;
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));

            while(this.size() < this.maxSize) {

                String[] args = bufferedReader.readLine().split(split);

                E data = factory.make(args);
                this.add(data);
            }
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public long getMaxSize() { return this.maxSize; }
    public void setMaxSize(long maxSize) { this.maxSize = maxSize; }

    public long getActualPos() {
        return actualPos;
    }

    public void setActualPos(long actualPos) {
        this.actualPos = actualPos;
    }

    public File getFile() {
        return file;
    }

    public void setFile(String filePath) {
        this.file = new File(filePath);
    }
}

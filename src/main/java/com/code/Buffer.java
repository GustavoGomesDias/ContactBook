package com.code;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Buffer<E> extends ArrayList<E> {
    private long maxSize;
    private long actualPos;
    private File file;
    private final Constructor<? extends  E> constructor;

    public Buffer(long maxSize, long actualPos, String filePath, Constructor<? extends E> constructor) {
        this.maxSize = maxSize;
        this.actualPos = actualPos;
        this.file = new File(filePath);
        this.constructor = constructor;
    }

    public void load() {
        try {
            if (this.file.exists()) {
                System.out.println("Arquivo de entrada não existe!");
                return;
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));

            Type[] types = constructor.getParameterTypes();

            while(this.size() < this.maxSize) {

                E data = (E) constructor.newInstance(bufferedReader.readLine());
                this.add(data);
            }
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testAnnotationAndTypes() {
        Type[] types = constructor.getParameterTypes();
        Annotation[][] parameters = constructor.getParameterAnnotations();
        System.out.println("Types:");
        for (Type type: types) {
            System.out.println(type);
        }

        for (Annotation[] row : parameters) {
            for (Annotation annotation : row) {
                System.out.println(annotation.toString());
            }
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

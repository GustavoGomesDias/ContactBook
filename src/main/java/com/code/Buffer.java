package com.code;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

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
            if (!this.file.exists()) {
                System.out.println("Arquivo de entrada não existe!");
                return;
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));

            while(this.size() < this.maxSize) {

                String[] args = bufferedReader.readLine().split(split);

                E data = factory.make(args);
                this.add(data);
            }
            System.out.println("Dados carregados.");
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String...order) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./results/out.txt", true));
        for (E e : this) {
            StringBuilder str = new StringBuilder();
            Method[] methods = e.getClass().getMethods();
            for (String methodName : order) {
                for (Method method : methods) {
                    if (methodName.equals(method.getName())) {
                        str.append(method.invoke(e)).append(",");

                    }
                }
            }
            System.out.println(str);
            bufferedWriter.write(str.toString() + "\n");
        }

        bufferedWriter.close();
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

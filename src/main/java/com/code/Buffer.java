package com.code;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

public class Buffer<E> extends ArrayList<E> {
    private long maxSize;
    private long actualPos;
    private long finalPos;
    private File file;
    private final Factory<E> factory;
    private final String outFileName;

    public Buffer(long maxSize, long actualPos, String filePath, Factory<E> factory) {
        this.maxSize = maxSize;
        this.actualPos = actualPos;
        this.finalPos = actualPos;
        this.file = new File(filePath);
        this.factory = factory;
        long random = new SecureRandom().nextInt();
        long randomNumber = random < 0 ? random * (-1) : random;
        this.outFileName = "out-" + randomNumber + ".txt";
    }

    public void load(String split) {
        try {
            if (!this.file.exists()) {
                System.out.println("Arquivo de entrada não existe!");
                return;
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
            ObjectSizeCalculator objSizeCalc = new ObjectSizeCalculator();

            String infos = bufferedReader.readLine();

            long sizing = 0;
            long pos = 0;
            while((sizing < this.maxSize) && (infos != null)) {
                String[] args = infos.split(split);
                if (pos >= this.getActualPos()) {
                    E data = factory.make(args);
                    this.add(data);
                    sizing += objSizeCalc.getObjectSizeInBytes(data);
                    this.finalPos += 1;
                }
                infos = bufferedReader.readLine();
                pos++;
            }
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String...order) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.sort();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./results/" + this.outFileName, true));
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
            bufferedWriter.write(str.toString() + "\n");
        }

        bufferedWriter.close();
    }

    private void sort() {
        Collections.sort((ArrayList)this);
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

    public String getOutFileName() {
        return outFileName;
    }

    public long getFinalPos() {
        return finalPos;
    }

    public void setFinalPos(long finalPos) {
        this.finalPos = finalPos;
    }
}

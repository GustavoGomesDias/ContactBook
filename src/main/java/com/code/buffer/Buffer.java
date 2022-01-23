package com.code.buffer;

import com.code.Factory;
import com.code.algorithm.ObjectSizeCalculator;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Buffer<E> extends ArrayList<E> {
    private long maxSize;
    private long actualPos;
    private long finalPos;
    private File file;
    private String enterFileName;
    private String outFileName;
    private final String bufferAction;
    private final Factory<E> factory;
    private final BufferAction bAction;

    public Buffer(long maxSize, long actualPos, String filePath, Factory<E> factory, BufferAction bufferAction, String outFile) {
        this.maxSize = maxSize;
        this.actualPos = actualPos;
        this.finalPos = actualPos;
        this.bAction = bufferAction;
        if (bufferAction == BufferAction.IN || bufferAction == BufferAction.SPLIT || bufferAction == BufferAction.MERGE) {
            this.file = new File("./results/" + filePath);
            this.enterFileName = filePath;
            if (bufferAction == BufferAction.MERGE) this.bufferAction = "merge";
            else this.bufferAction = "in";
        } else {
            this.bufferAction = "out";
        }
        this.factory = factory;
        this.outFileName = outFile;
    }

    public BufferLoadState load(String split) {
        try {
            long sizing = 0, pos = 0;

            if (!this.file.exists()) {
                System.out.println("Arquivo de entrada não existe!");
                return BufferLoadState.NOTEXISTS;
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
            ObjectSizeCalculator objSizeCalc = new ObjectSizeCalculator();

            String infos = bufferedReader.readLine();

            if (infos == null || infos.equals("")) return BufferLoadState.END;

            while(sizing < (this.maxSize)) {
                String[] args = infos.split(split);
                if (pos >= this.getActualPos()) {
                    E data = factory.make(args);
                    this.add(data);
                    sizing += objSizeCalc.getObjectSizeInBytes(data);
                    this.finalPos += 1;
                    this.actualPos++;
                }
                infos = bufferedReader.readLine();

                if (infos == null) {
                    return BufferLoadState.END;
                }
                pos++;
            }
            return BufferLoadState.HASMORE;
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return BufferLoadState.ERROR;
        }
    }

    public String writeFile(String...order) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.sort();
        if (this.outFileName.equals("")) {
            this.setOutFileName(this.bufferAction);
        }
        if (this.bufferAction.equals("merge")) {
            System.out.println(this.outFileName);
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./results/" + this.outFileName, true));
        for (E E : this) {
            StringBuilder str = new StringBuilder();
            Method[] methods = E.getClass().getMethods();
            for (String methodName : order) {
                for (Method method : methods) {
                    if (methodName.equals(method.getName())) {
                        str.append(method.invoke(E)).append(",");
                    }
                }
            }
            bufferedWriter.write(str.toString() + "\n");
        }

        bufferedWriter.close();
        return this.outFileName;
    }

    public void sort() {
        Collections.sort((ArrayList)this);
    }

    public void addInBuffer(E e) {
        if (this.size() < this.maxSize) {
            this.add(e);
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

    public String getOutFileName() {
        return this.outFileName;
    }

    public long getFinalPos() {
        return finalPos;
    }

    public void setFinalPos(long finalPos) {
        this.finalPos = finalPos;
    }

    public void setOutFileName(String action) {
        if (action.equals("in") || action.equals("out") || action.equals("merge")) {
            long random = new SecureRandom().nextInt();
            long randomNumber = random < 0 ? random * (-1) : random;
            this.outFileName = action + "-" + randomNumber + ".txt";
        } else {
            this.outFileName = action;
        }
    }

    public String getEnterFileName() {
        return enterFileName;
    }

    public void setEnterFileName(String enterFileName) {
        this.enterFileName = enterFileName;
    }

    public BufferAction getbAction() {
        return bAction;
    }
}

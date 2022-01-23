package com.code;

import com.code.buffer.Buffer;
import com.code.buffer.BufferAction;
import com.code.buffer.IDataBuffer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/*
* TODO: Dividir arquivo
* https://www.guj.com.br/t/dividir-arquivos-em-pedacos-particoes/367336/5
* https://www.guj.com.br/t/ler-arquivo-ate-certo-determiando-tamanho/138970/8
* Splitando aproximadamente
*   */

public class FileManager<E> {
    private String filePath;
    private long byteSize;
    private long actualPos;
    private final ArrayList<String> outFilePathList = new ArrayList<>();
    private final IDataBuffer<E> dataBuffer;

    public FileManager(String filePath, long byteSize, IDataBuffer<E> dataBuffer) {
        this.filePath = filePath;
        this.byteSize = byteSize;
        this.actualPos = 0;
        this.dataBuffer = dataBuffer;
    }

    public void splitFile(String...order) {
        try {
            Buffer<E> buffer = this.dataBuffer.build(this.byteSize, this.actualPos, this.filePath, BufferAction.SPLIT, "");
            buffer.load(",");
            this.setActualPos(buffer.getFinalPos());
            this.outFilePathList.add(buffer.writeFile(order));
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | IOException e) {
            e.printStackTrace();
        }

    }

    public long getByteSize() {
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

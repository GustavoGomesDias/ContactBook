package com.code;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BufferManager<E> {
    private final long ramMemoryFree;
    private String filePath;
    private final FileManager<E> fileManager;
    private final ArrayList<Buffer<E>> bufferArrayList = new ArrayList<>();
    private final IDataBuffer<E> dataBuffer;

    public BufferManager(String filePath, IDataBuffer<E> dataBuffer) {
        this.ramMemoryFree = 500 * 1024; //Runtime.getRuntime().freeMemory();
        this.filePath = filePath;
        this.fileManager = new FileManager<>(this.filePath, this.ramMemoryFree, dataBuffer);
        this.dataBuffer = dataBuffer;
    }


    public void handleSplitFile(String...order) throws IOException {
        long fileLength = this.ramMemoryFree;
        File file = new File("./results/" + this.filePath);

        while (fileLength < file.length()) {
            this.fileManager.splitFile(order);
            fileLength += this.ramMemoryFree;
        }

        long splitMemorySize = 0;

        for(String path : this.fileManager.getOutFilePathList()) {
            File outFile = new File("./results/" + path);
            splitMemorySize += outFile.length();
        }

        if (splitMemorySize < file.length()) {
            this.fileManager.splitFile(order);
        }
    }

    public void genBuffers() {
        for (String filepath : this.fileManager.getOutFilePathList()) {
            Buffer<E> buffer = this.dataBuffer.build(this.ramMemoryFree, 0, filepath, BufferAction.IN);
            buffer.load(",");
            this.bufferArrayList.add(buffer);
        }
    }

    public ArrayList<Buffer<E>> getBufferArrayList() {
        return this.bufferArrayList;
    }

    public Buffer<E> genBufferOut () {
        return this.dataBuffer.build(this.ramMemoryFree, 0, "/results/merge.txt", BufferAction.OUT);
    }

    public void write(Buffer<E> buffer) {
        this.dataBuffer.writeFile(buffer);
    }

    public long getRamMemoryFree() {
        return ramMemoryFree;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public FileManager getManageFile() {
        return fileManager;
    }
}

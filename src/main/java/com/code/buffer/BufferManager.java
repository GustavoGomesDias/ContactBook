package com.code.buffer;

import com.code.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BufferManager<E> {
    private final long ramMemoryFree;
    private String filePath;
    private final FileManager<E> fileManager;
    private final ArrayList<Buffer<E>> bufferArrayList = new ArrayList<>();
    private final IDataBuffer<E> dataBuffer;
    private long bufferInMemory;

    public BufferManager(String filePath, IDataBuffer<E> dataBuffer) {
        this.ramMemoryFree = 50 * 1024; //Runtime.getRuntime().freeMemory();
        this.filePath = filePath;
        this.fileManager = new FileManager<>(this.filePath, this.ramMemoryFree, dataBuffer);
        this.dataBuffer = dataBuffer;
    }


    public void handleSplitFile(String...order) {
        System.out.println("Ok, a ação agora é [SPLIT] - Tamanho dos arquivos: " + this.ramMemoryFree);
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
        System.out.println("Ok, a ação agora é [IN] - Gerando os buffers de entrada.");
        long memoryBuffer = this.ramMemoryFree / (this.fileManager.getOutFilePathList().size() + 1);
        this.bufferInMemory = memoryBuffer;
        for (String filepath : this.fileManager.getOutFilePathList()) {
            Buffer<E> buffer = this.dataBuffer.build(memoryBuffer, 0, filepath, BufferAction.IN, "");
            buffer.load(",");
            this.bufferArrayList.add(buffer);
        }
    }

    public Buffer<E> genNewBuffer(long actualPos, String filepath, BufferAction bufferAction, String fileOut) {
        long memoryBuffer = this.ramMemoryFree / (this.fileManager.getOutFilePathList().size() + 1);
        this.bufferInMemory = memoryBuffer;
        return this.dataBuffer.build(memoryBuffer, actualPos, filepath, bufferAction, fileOut);
    }

    public ArrayList<Buffer<E>> getBufferArrayList() {
        return this.bufferArrayList;
    }

    public Buffer<E> genBufferOut (String fileOut) {
        long memoryBuffer = this.ramMemoryFree / (this.fileManager.getOutFilePathList().size() + 1);

        if (!fileOut.equals("")) {
            return this.dataBuffer.build(memoryBuffer, 0, "/results/merge.txt", BufferAction.OUT, fileOut);
        }
        return this.dataBuffer.build(memoryBuffer, 0, "/results/merge.txt", BufferAction.OUT, "");
    }

    public String write(Buffer<E> buffer) {
        return this.dataBuffer.writeFile(buffer);
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

    public FileManager<E> getManageFile() {
        return fileManager;
    }

    public long getBufferInMemory() {
        return bufferInMemory;
    }

    public void setBufferInMemory(long bufferInMemory) {
        this.bufferInMemory = bufferInMemory;
    }
}

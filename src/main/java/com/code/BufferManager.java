package com.code;

import java.io.File;
import java.io.IOException;

public class BufferManager {
    private final int ramMemoryFree;
    private String filePath;
    private FileManager fileManager;

    public BufferManager(String filePath) {
        this.ramMemoryFree = 100 * 1024;
        this.filePath = filePath;
        this.fileManager = new FileManager(this.filePath, this.ramMemoryFree);
    }


    // TODO: Está funcionando até certo ponto, como o calculo do tamanho é aproximado, ele acana mão pegando a "rebarba"
    public void handleSplitFile() {
        try {
            long fileLength = this.ramMemoryFree;
            File file = new File(this.filePath);

            while(fileLength < file.length()){
                this.fileManager.splitFile();
                fileLength += this.ramMemoryFree;
            }

            long splitMemorySize = 0;

            for(String path : this.fileManager.getOutFilePathList()) {
                File outFile = new File("./results/" + path);
                splitMemorySize += outFile.length();
            }

            if (splitMemorySize < file.length()) {
                this.fileManager.splitFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRamMemoryFree() {
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

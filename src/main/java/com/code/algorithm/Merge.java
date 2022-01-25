package com.code.algorithm;

import com.code.buffer.Buffer;
import com.code.buffer.BufferAction;
import com.code.buffer.BufferLoadState;
import com.code.buffer.BufferManager;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/*
 * Motivo do bug
 * A | A [A, A] => {A, A}
 * A | A [A, A] => {A, A, A, A}
 * A | A [A, A] => {A, A, A, A, A, A}
 * A | A [A, A] => {A, A, A, A, A, A, A, A}
 * A | A [A, A] => {A, A, A, A, A, A, A, A}
 * A | B [A, B] => {A, A, A, A, A, A, A, A, A, B}
 * A | B [A, B] => {A, A, A, A, A, A, A, A, A, B, A, B}
 * Buffer | Buffer [merge] => {arquivo}
 * */

public class Merge<E> {
    private final ArrayList<Buffer<E>> bufferArray = new ArrayList<>();
    private final ArrayList<String> fileOutArr = new ArrayList<>();
    private long maxArrayBufferSize;
    private final Buffer<E> bufferOut;
    private final BufferManager<E> bufferManager;

    public Merge(long maxArrayBufferSize, Buffer<E> bufferOut, BufferManager<E> bufferManager) {
        this.maxArrayBufferSize = maxArrayBufferSize;
        this.bufferOut = bufferOut;
        this.bufferManager = bufferManager;
    }

    public void addBuffer(Buffer<E> buffer) {
        if (this.bufferArray.size() < this.maxArrayBufferSize) this.bufferArray.add(buffer);
    }

    public void mergeTwo(Buffer<E> b1, Buffer<E> b2) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Integer lenA = 0, lenB = 0;

        while (lenA < b1.size() && lenB < b2.size()) {
            if (this.bufferOut.size() >= this.bufferOut.getMaxSize()) {
                this.bufferManager.write(this.bufferOut);
                this.bufferOut.clear();
            }
            E data1 = b1.get(lenA);
            E data2 = b2.get(lenB);
            Method compareTo = data1.getClass().getMethod("compareTo", data2.getClass());

            int  result = (int) compareTo.invoke(data1, data2);

            if (result == 0) {
                if (!this.bufferOut.contains(data1) && !this.bufferOut.contains(data2) && this.bufferOut.size() < this.bufferOut.getMaxSize()) {
                    bufferOut.addInBuffer(data1);
                    bufferOut.addInBuffer(data2);
                }
                lenA++;
                lenB++;
            } else if (result > 0) {
                if (!this.bufferOut.contains(data2) && this.bufferOut.size() < this.bufferOut.getMaxSize()) bufferOut.addInBuffer(data2);
                lenB++;
            } else {
                if (!this.bufferOut.contains(data1) && this.bufferOut.size() < this.bufferOut.getMaxSize())
                    bufferOut.addInBuffer(data1);
                lenA++;
            }
        }

        if (lenA < b1.size()) {
            if (this.bufferOut.size() >= this.bufferOut.getMaxSize()) {
                this.bufferManager.write(this.bufferOut);
                this.bufferOut.clear();
            }
            while (lenA < b1.size()) {
                if (!this.bufferOut.contains(b1.get(lenA)) && this.bufferOut.size() < this.bufferOut.getMaxSize()) bufferOut.addInBuffer(b1.get(lenA));
                lenA++;
            }
        }

        if (lenB < b2.size()) {
            if (this.bufferOut.size() >= this.bufferOut.getMaxSize()) {
                this.bufferManager.write(this.bufferOut);
                this.bufferOut.clear();
            }
            while (lenB < b2.size()) {
                if (!this.bufferOut.contains(b2.get(lenB)) && this.bufferOut.size() < this.bufferOut.getMaxSize()) bufferOut.addInBuffer(b2.get(lenB));
                lenB++;
            }
        }
        this.bufferManager.write(this.bufferOut);
    }

    public void merge(BufferLoadState bufferLoadState, ArrayList<Buffer<E>> buffers) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        int len = 0;
        boolean isAdded = false;

        if (bufferLoadState == BufferLoadState.INIT) this.bufferOut.setOutFileName("");

        if (bufferLoadState == BufferLoadState.HASMORE) this.bufferOut.setOutFileName(this.bufferOut.getOutFileName());

        while (len < this.bufferManager.getBufferInMemory()) {

            for (Buffer<E> bff : buffers) {
                if (len < bff.size()) {
                    isAdded = true;
                    E data = bff.get(len);
                    this.bufferOut.add(data);
                }
            }

            if (this.bufferOut.size() >= this.bufferOut.getMaxSize()) {
                String fileOut = this.bufferManager.write(this.bufferOut);
                if (!this.fileOutArr.contains(fileOut)) {
                    this.fileOutArr.add(fileOut);
                }
                this.bufferOut.clear();
            }
            len++;
        }

        String fileOut = this.bufferManager.write(this.bufferOut);
        if (!this.fileOutArr.contains(fileOut)) {
            this.fileOutArr.add(fileOut);
        }

        this.bufferOut.clear();
    }

    public void mergePerPart(ArrayList<Buffer<E>> lstBff, long waySize, boolean saveLast) {
        try {
            System.out.println("Sem ação do buffer, merge acontecendo...");
            ArrayList<Buffer<E>> buffers = lstBff != null ? lstBff : this.bufferManager.getBufferArrayList();
            int i = 0;
            while (i < (buffers.size() - 1)) {
                ArrayList<Buffer<E>> kWay = new ArrayList<>();
                for (int way = 0, k = i; way < waySize && k < buffers.size(); way++, k++) {
                    kWay.add(buffers.get(i + way));
                }

                this.merge(BufferLoadState.INIT, kWay);

                while (kWay.size() > 0) {
                    boolean isEnd = false;
                    int pos = -1;
                    while (!isEnd) {
                        int count = 0;
                        for (Buffer<E> bff : kWay) {
                            bff.clear();
                            BufferLoadState loadState = bff.load(",");
                            if (loadState == BufferLoadState.END) {
                                pos = count;
                                System.out.println("[pos]: " + pos);
                                isEnd = true;
                            }
                            count++;
                        }

                        if (!isEnd) this.merge(BufferLoadState.HASMORE, kWay);
                    }



                    for (int c = pos; c < kWay.size(); c++) {
                        kWay.get(c).clear();
                        BufferLoadState loadState = kWay.get(c).load(",");
                        // && (c != kWay.size() - 1)
                        if (loadState == BufferLoadState.END) kWay.remove(c);
                        if (kWay.size() > 0) this.merge(BufferLoadState.HASMORE, kWay);
                    }
                }
                i += waySize;
            }

            if (buffers.size() == 1) {
                String file = buffers.get(0).getEnterFileName();
                if (!this.fileOutArr.contains(file)) this.fileOutArr.add(file);
            }

            if (buffers.size() % 2 != 0 && saveLast) {
                String file = buffers.get(buffers.size() - 1).getEnterFileName();
                if (!this.fileOutArr.contains(file)) this.fileOutArr.add(file);
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void mergeResult(ArrayList<Buffer<E>> lstBff, long waySize) {
        this.mergePerPart(lstBff, waySize, true);

        int fileOutSize = this.fileOutArr.size();
        while (fileOutSize != 1 && fileOutSize > 0) {
            ArrayList<Buffer<E>> bffs = new ArrayList<>();
            for (String filePath : this.fileOutArr) {
                System.out.println(filePath);
                bffs.add(this.bufferManager.genNewBuffer(0, filePath, BufferAction.MERGE, ""));
            }

            this.fileOutArr.clear();
            long wayCalc = bffs.size() / waySize;

            if (wayCalc > 2) {
                this.mergePerPart(bffs, wayCalc, true);
            } else {
                System.out.println("Entrou no mergeAll");
                this.mergePerPart(bffs, bffs.size(), false);
            }

            fileOutSize = this.fileOutArr.size();
        }
    }

    public ArrayList<Buffer<E>> getBufferArray() {
        return bufferArray;
    }

    public long getMaxArrayBufferSize() {
        return maxArrayBufferSize;
    }

    public void setMaxArrayBufferSize(long maxArrayBufferSize) {
        this.maxArrayBufferSize = maxArrayBufferSize;
    }

    public BufferManager<E> getBufferManager() {
        return bufferManager;
    }

    public ArrayList<String> getFileOutArr() {
        return fileOutArr;
    }

    public void handleHasMore(BufferLoadState bufferLoadState, Buffer<E> buffer) {
        if (bufferLoadState == BufferLoadState.HASMORE) {
            String fileOut = this.bufferOut.getOutFileName();
            if (!fileOut.equals("")) this.bufferOut.setOutFileName(fileOut);
            while (bufferLoadState == BufferLoadState.HASMORE) {
                this.bufferOut.addAll(buffer);
                this.bufferManager.write(this.bufferOut);
                this.bufferOut.clear();
                bufferLoadState = buffer.load(",");
            }
        }
    }

    public void printFileOut() {
        for (String fileout : this.fileOutArr) {
            System.out.println(fileout);
        }
    }
}

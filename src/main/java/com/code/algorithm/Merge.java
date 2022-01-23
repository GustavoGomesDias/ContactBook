package com.code.algorithm;

import com.code.buffer.Buffer;
import com.code.buffer.BufferAction;
import com.code.buffer.BufferLoadState;
import com.code.buffer.BufferManager;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

// TODO: Merge não pode ser do tipo IBufferData

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

    public void merge(BufferLoadState bufferLoadState, ArrayList<Buffer<E>> buffers) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        int len = 0;
        boolean isAdded = false;
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
                this.fileOutArr.add(fileOut);
                this.bufferOut.clear();
            }

            len++;
        }

        if (bufferLoadState == BufferLoadState.INIT) this.bufferOut.setOutFileName("");

        if (bufferLoadState == BufferLoadState.HASMORE) this.bufferOut.setOutFileName(this.bufferOut.getOutFileName());
        if (isAdded) {
            String fileOut = this.bufferManager.write(this.bufferOut);
            if ((bufferLoadState == BufferLoadState.END && !this.fileOutArr.contains(fileOut))) this.fileOutArr.add(fileOut);
            if (bufferLoadState == BufferLoadState.INIT && !this.fileOutArr.contains(fileOut)) {
                this.fileOutArr.add(fileOut);
            }
        }
        this.bufferOut.clear();
    }

    public void mergePerPart(ArrayList<Buffer<E>> lstBff, long waySize, boolean saveLast) {
        try {
            System.out.println("Sem ação do buffer, merge acontecendo...");
            ArrayList<Buffer<E>> buffers = lstBff != null ? lstBff : this.bufferManager.getBufferArrayList();
            int i = 0, step = 0;
            while (i < (buffers.size() - 1)) {
                System.out.println("[step]: " + step);
                ArrayList<Buffer<E>> kWay = new ArrayList<>();
                for (int way = 0; way < waySize; way++) {
                    kWay.add(buffers.get(i + way));
                }

                this.merge(BufferLoadState.INIT, kWay);

                boolean isEnd = false;
                int pos = -1, firstCount = 0;;

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

                System.out.println("Valor final de [pos]: " + pos);
                for (int c = pos; c < kWay.size(); c++) {
                    kWay.get(c).clear();
                    BufferLoadState loadState = kWay.get(c).load(",");
                    if (loadState == BufferLoadState.END && (c != kWay.size() - 1)) kWay.remove(c);
                    if (kWay.size() > 0) this.merge(BufferLoadState.HASMORE, kWay);
                }
                i += waySize;
                step++;
            }

            if (buffers.size() == 1) {
                this.fileOutArr.add(buffers.get(0).getEnterFileName());
            }

            if (buffers.size() % 2 != 0 && saveLast) {
                this.fileOutArr.add(buffers.get(buffers.size() - 1).getEnterFileName());
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

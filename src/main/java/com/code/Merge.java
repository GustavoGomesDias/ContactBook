package com.code;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

// TODO: Merge não pode ser do tipo IBufferData

public class Merge<E> {
    private final ArrayList<Buffer<E>> bufferArray = new ArrayList<>();
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

    public void merge(Buffer<E> b1, Buffer<E> b2) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
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
}

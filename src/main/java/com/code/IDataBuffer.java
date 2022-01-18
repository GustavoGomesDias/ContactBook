package com.code;


public interface IDataBuffer<E> {
    public Buffer<E> build(long byteSize, long actualPos, String filePath, BufferAction bufferAction);
    public void writeFile(Buffer<E> buffer);
}

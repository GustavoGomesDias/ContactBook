package com.code.buffer;


import com.code.buffer.Buffer;
import com.code.buffer.BufferAction;

public interface IDataBuffer<E> {
    public Buffer<E> build(long byteSize, long actualPos, String filePath, BufferAction bufferAction, String fileOut);
    public String writeFile(Buffer<E> buffer);
}

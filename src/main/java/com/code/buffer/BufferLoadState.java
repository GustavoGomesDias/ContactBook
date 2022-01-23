package com.code.buffer;

public enum BufferLoadState {
    INIT(1), END(2), HASMORE(3), NOTEXISTS(4), ERROR(5);

    private final int value;

    BufferLoadState(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}

package com.code.buffer;

public enum BufferAction {
    OUT(1), IN(2), SPLIT(3), MERGE(4);

    public final int actions;
    BufferAction(int i) {
        this.actions = i;
    }

    public int getActions() {
        return actions;
    }
}

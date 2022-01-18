package com.code;

public enum BufferAction {
    OUT(1), IN(2);

    public final int actions;
    BufferAction(int i) {
        this.actions = i;
    }

    public int getActions() {
        return actions;
    }
}

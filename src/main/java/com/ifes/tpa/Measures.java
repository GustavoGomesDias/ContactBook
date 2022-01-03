package com.ifes.tpa;


public enum Measures {
    BYTES("Bytes"), MB("MB"), GB("GB");

    private String unit;

    Measures(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

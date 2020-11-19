package com.TtPP.entities;

public class Kind {
    private int kindId;
    private String name = "";

    public Kind(int kindId, String name) {
        this.kindId = kindId;
        this.name = name;
    }

    public int getKindId() {
        return this.kindId;
    }

    public void setKindId(int kindId) {
        this.kindId = kindId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

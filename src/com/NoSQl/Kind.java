package com.NoSQl;

public class Kind<IDType> {
    private IDType kindId;
    private String name = "";

    public Kind(IDType kindId, String name) {
        this.kindId = kindId;
        this.name = name;
    }

    public IDType getKindId() {
        return this.kindId;
    }

    public void setKindId(IDType kindId) {
        this.kindId = kindId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.TtPP.builders;

import com.TtPP.entities.Kind;

public class KindBuilder {
    private Kind kind = new Kind(-1, "");

    public KindBuilder withName (String name) {
        this.kind.setName(name);
        return this;
    }

    public KindBuilder withId (int id) {
        this.kind.setKindId(id);
        return this;
    }

    public Kind build () {
        Kind tempKind = this.kind;
        this.kind = new Kind(-1, "");
        return tempKind;
    }
}

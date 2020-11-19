package com.TtPP.entities;

import com.TtPP.DAO.IDAO;

public class DogCreator implements IPetCreator {
    private final int dogKindId;

    public DogCreator (IDAO dao) throws Exception {
        this.dogKindId = dao.getKindIdByName("dog");
    }

    public Pet create () {
        return new Pet(
                -1,
                -1,
                -1,
                this.dogKindId,
                -1,
                "",
                null,
                false
        );
    }
}

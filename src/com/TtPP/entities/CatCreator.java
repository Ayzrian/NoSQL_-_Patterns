package com.TtPP.entities;

import com.TtPP.DAO.ERole;
import com.TtPP.DAO.IDAO;

public class CatCreator {
    private final int catKindId;

    public CatCreator (IDAO dao) throws Exception {
        this.catKindId = dao.getKindIdByName("cat", ERole.ADMIN);
    }

    public Pet create () {
        return new Pet(
                -1,
                -1,
                -1,
                this.catKindId,
                -1,
                "",
                null,
                false
        );
    }
}
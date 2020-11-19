package com.TtPP.DAO;

import com.TtPP.DAO.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class DAOFactory {
    public IDAO createDao(EDAOType type) throws NoSuchMethodException, IllegalAccessException, InstantiationException, SQLException, InvocationTargetException, ClassNotFoundException {
        switch (type) {
            case MySQL:
                return MySQLDAO.getInstance();
        }
        return null;
    }
}

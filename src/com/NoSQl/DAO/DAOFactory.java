package com.NoSQl.DAO;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class DAOFactory {
    public IDAO createDao(EDAOType type) throws NoSuchMethodException, IllegalAccessException, InstantiationException, SQLException, InvocationTargetException, ClassNotFoundException {
        switch (type) {
            case MySQL:
                return MySQLDAO.getInstance();
            case MongoDB:
                return MongoDAO.getInstance();
        }
        return null;
    }
}

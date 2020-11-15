package com.NoSQl.DAO;

import com.NoSQl.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// TODO: Lock tables
public class MySQLDAO implements IDAO<Integer> {
    private static final String url = "jdbc:mysql://localhost:3306/NoSQL";
    private static final String user = "root";
    private static final String password = "root";
    private static Connection connection = null;
    private static MySQLDAO instance = null;

    private MySQLDAO() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        MySQLDAO.connection = DriverManager.getConnection(MySQLDAO.url, MySQLDAO.user, MySQLDAO.password);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        connection.close();
    }

    public static synchronized MySQLDAO getInstance() throws NoSuchMethodException, IllegalAccessException, InstantiationException, SQLException, InvocationTargetException, ClassNotFoundException {
        if (MySQLDAO.instance == null)
            MySQLDAO.instance = new MySQLDAO();

        return MySQLDAO.instance;
    }

    @Override
    public List<Pet<Integer>> getAllPets () throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Pet;");

        ResultSet result = statement.executeQuery();

        return makePetsFromResult(result);
    }

    @Override
    public List<City<Integer>> getAllCities () throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM City;");

        ResultSet result = statement.executeQuery();

        List<City<Integer>> cities = new ArrayList<>();

        while (result.next()) {
            cities.add(new City<>(
                    result.getInt("cityId"),
                    result.getString("name")
            ));
        }

        return cities;
    }

    @Override
    public List<Breed<Integer>> getAllBreeds () throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Breed;");

        ResultSet result = statement.executeQuery();

        List<Breed<Integer>> breeds = new ArrayList<>();

        while (result.next()) {
            breeds.add(new Breed<>(
                    result.getInt("breedId"),
                    result.getString("name")
            ));
        }

        return breeds;
    }

    @Override
    public List<Kind<Integer>> getAllKinds () throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Kind");

        ResultSet result = statement.executeQuery();

        List<Kind<Integer>> kinds = new ArrayList<>();

        while (result.next()) {
            kinds.add(new Kind<>(
                    result.getInt("kindId"),
                    result.getString("name")
            ));
        }

        return kinds;
    }

    @Override
    public void dropOwners () throws Exception {
        connection.prepareStatement("DELETE FROM Owner").execute();
    }

    @Override
    public Integer createCity (City<Integer> city) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT City(name) VALUES(?)"
        );

        statement.setString(1, city.getName());

        statement.execute();

        return getLastInsertID();
    }

    @Override
    public void deleteCity (City<Integer> city) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM City WHERE cityId = ?"
        );

        statement.setInt(1, city.getCityId());

        lockTableWrite("City");

        statement.execute();

        unlockTables();
    }

    @Override
    public Integer createKind (Kind<Integer> kind) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT Kind(name) VALUES(?)"
        );

        statement.setString(1, kind.getName());

        statement.execute();

        return getLastInsertID();
    }

    @Override
    public Integer createBreed (Breed<Integer> breed) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT Breed(name) VALUES(?)"
        );

        statement.setString(1, breed.getName());

        statement.execute();

        return getLastInsertID();
    }

    @Override
    public Integer createPet (Pet<Integer> pet) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT Pet(fkParentId, fkOwnerId, fkKindId, fkBreedId, name, dateOfBirth, sex) VALUES (?, ?, ?, ?, ?, ?, ?)"
        );

        if (pet.getFkParentId() == -1)
            statement.setNull(1, Types.INTEGER);
        else
            statement.setInt(1, pet.getFkBreedId());

        statement.setInt(2, pet.getFkOwnerId());
        statement.setInt(3, pet.getFkKindId());
        statement.setInt(4, pet.getFkBreedId());
        statement.setString(5, pet.getName());
        statement.setDate(6, new java.sql.Date(pet.getDateOfBirth().getTime()));
        statement.setBoolean(7, pet.getSex());

        statement.execute();

        return getLastInsertID();
    }

    @Override
    public void deletePet (Pet<Integer> pet) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM Pet WHERE petId = ?"
        );

        statement.setInt(1, pet.getPetId());

        lockTableWrite("Pet");

        statement.execute();

        unlockTables();
    }

    @Override
    public void updatePet (Pet<Integer> pet) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Pet SET fkParentId = ?, fkOwnerId = ?, fkKindId = ?, fkBreedId = ?, name = ?, dateOfBirth = ?, sex = ? WHERE petId = ?"
        );

        if (pet.getFkParentId().equals(-1))
            statement.setNull(1, Types.INTEGER);
        else
            statement.setInt(1, pet.getFkBreedId());

        statement.setInt(2, pet.getFkOwnerId());
        statement.setInt(3, pet.getFkKindId());
        statement.setInt(4, pet.getFkBreedId());
        statement.setString(5, pet.getName());
        statement.setDate(6, new java.sql.Date(pet.getDateOfBirth().getTime()));
        statement.setBoolean(7, pet.getSex());
        statement.setInt(8, pet.getPetId());

        lockTableWrite("Pet");

        statement.execute();

        unlockTables();
    }

    @Override
    public Integer createOwner(Owner<Integer> owner) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT Owner (firstName, lastName, phoneNumber, email, fkCityId)" +
                        "VALUES (?, ?, ?, ?, ?) "
        );

        statement.setString(1, owner.getFirstName());
        statement.setString(2, owner.getLastName());
        statement.setString(3, owner.getPhoneNumber());
        statement.setString(4, owner.getEmail());
        statement.setInt(5, owner.getFkCityId());

        statement.execute();

        return getLastInsertID();
    }

    @Override
    public void deleteOwner(Integer ownerId) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM Owner WHERE ownerId = ?"
        );

        statement.setInt(1, ownerId);

        statement.execute();
    }

    @Override
    public void updateOwner(Owner<Integer> owner) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Owner SET firstName = ?, lastName = ?, email = ?, phoneNumber = ?, fkCityId = ? WHERE ownerId = ?"
        );

        statement.setString(1, owner.getFirstName());
        statement.setString(2, owner.getLastName());
        statement.setString(3, owner.getEmail());
        statement.setString(4, owner.getPhoneNumber());
        statement.setInt(5, owner.getFkCityId());
        statement.setInt(6, owner.getOwnerId());

        lockTableWrite("Owner");

        statement.execute();

        unlockTables();
    }

    @Override
    public List<Owner<Integer>> searchOwner(OwnerSearchObject<Integer> ownerSearchObject) throws Exception {
        String searchQuery = "SELECT * FROM Owner ";
        boolean isFirst = true;
        List<String> order = new ArrayList<>();

        if (ownerSearchObject.ownerId != null) {
            if (isFirst) {
                searchQuery += " WHERE ownerId = ? ";
                isFirst = false;
            } else searchQuery += " AND ownerId = ? ";
            order.add("ownerId");
        }
        if (ownerSearchObject.email != null) {
            if (isFirst) {
                searchQuery += " WHERE email = ? ";
                isFirst = false;
            } else searchQuery += " AND email = ? ";
            order.add("email");
        }
        if (ownerSearchObject.firstName != null) {
            if (isFirst) {
                searchQuery += " WHERE firstName = ? ";
                isFirst = false;
            } else searchQuery += " AND firstName = ? ";
            order.add("firstName");
        }
        if (ownerSearchObject.lastName != null) {
            if (isFirst) {
                searchQuery += " WHERE lastName = ? ";
                isFirst = false;
            } else searchQuery += " AND lastName = ? ";
            order.add("lastName");
        }
        if (ownerSearchObject.fkCityId != null) {
            if (isFirst) {
                searchQuery += " WHERE fkCityId = ? ";
                isFirst = false;
            } else searchQuery += " AND fkCityId = ? ";
            order.add("fkCityId");
        }
        if (ownerSearchObject.phoneNumber != null) {
            if (isFirst) {
                searchQuery += " WHERE  phoneNumber = ? ";
                isFirst = false;
            } else searchQuery += " AND phoneNumber = ? ";
            order.add("phoneNumber");
        }
        searchQuery += ";";

        PreparedStatement statement = connection.prepareStatement(searchQuery);

        for (int i = 0; i < order.size(); i++) {
            switch (order.get(i)) {
                case "ownerId" -> statement.setInt(i + 1, ownerSearchObject.ownerId);
                case "firstName" -> statement.setString(i + 1, ownerSearchObject.firstName);
                case "lastName" -> statement.setString(i + 1, ownerSearchObject.lastName);
                case "phoneNumber" -> statement.setString(i + 1, ownerSearchObject.phoneNumber);
                case "email" -> statement.setString(i + 1, ownerSearchObject.email);
                case "fkCityId" -> statement.setInt(i +1, ownerSearchObject.fkCityId);
            }
        }

        ResultSet result = statement.executeQuery();

        return makeOwnersFromResult(result);
    }

    @Override
    public List<Pet<Integer>> getOwnerPets (Owner<Integer> owner) throws Exception {
        CallableStatement statement = connection.prepareCall("{CALL get_pets(?)}");

        statement.setInt(1, owner.getOwnerId());

        ResultSet result = statement.executeQuery();

        return makePetsFromResult(result);
    }

    private List<Pet<Integer>> makePetsFromResult(ResultSet result) throws Exception {
        List<Pet<Integer>> pets = new ArrayList<>();

        while (result.next()) {
            pets.add(makePetFromResultRow(result));
        }

        return pets;
    }

    private Pet<Integer> makePetFromResultRow(ResultSet result) throws Exception {
        return new Pet<Integer>(
                result.getInt("petId"),
                result.getInt("fkParentId"),
                result.getInt("fkOwnerId"),
                result.getInt("fkKindId"),
                result.getInt("fkBreedId"),
                result.getString("name"),
                result.getDate("dateOfBirth"),
                result.getBoolean("sex")
        );
    }

    private List<Owner<Integer>> makeOwnersFromResult(ResultSet result) throws Exception {
        List<Owner<Integer>> owners = new ArrayList<>();

        while (result.next()) {
            owners.add(makeOwnerFromResultRow(result));
        }

        return owners;
    }

    private Owner<Integer> makeOwnerFromResultRow(ResultSet result) throws Exception {
        return new Owner<Integer>(
                result.getInt("ownerId"),
                result.getString("firstName"),
                result.getString("lastName"),
                result.getString("phoneNumber"),
                result.getString("email"),
                result.getInt("fkCityId")
        );
    }


    private int getLastInsertID() throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
        resultSet.next();

        return resultSet.getInt(1);
    }

    private void lockTableWrite(String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("LOCK TABLES " + tableName + " WRITE;");
    }

    private void unlockTables() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("UNLOCK TABLES;");
    }

}


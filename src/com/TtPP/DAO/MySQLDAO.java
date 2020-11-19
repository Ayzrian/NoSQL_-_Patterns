package com.TtPP.DAO;

import com.TtPP.common.EventManager;
import com.TtPP.common.IEventListener;
import com.TtPP.entities.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton
 * @relatesTo Lab12
 */
public class MySQLDAO implements IDAO {
    private static final String url = "jdbc:mysql://localhost:3307/NoSQL?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "root";
    private static Connection connection = null;
    private static MySQLDAO instance = null;

    public static String PET_CHANGE = "PET_CHANGE";
    public static String NEW_PET = "NEW_PET";
    public static String DELETE_PET = "DELETE_PET";

    public static String BREED_CHANGE = "BREED_CHANGE";
    public static String NEW_BREED = "NEW_BREED";
    public static String DELETE_BREED = "DELETE_BREED";

    public static String KIND_CHANGE = "KIND_CHANGE";
    public static String NEW_KIND = "NEW_KIND";
    public static String DELETE_KIND = "DELETE_KIND";

    public static String OWNER_CHANGE = "OWNER_CHANGE";
    public static String NEW_OWNER = "NEW_OWNER";
    public static String DELETE_OWNER = "DELETE_OWNER";

    public static String CITY_CHANGE = "CITY_CHANGE";
    public static String NEW_CITY = "NEW_CITY";
    public static String DELETE_CITY = "DELETE_CITY";

    private EventManager events;

    private MySQLDAO() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        MySQLDAO.connection = DriverManager.getConnection(MySQLDAO.url, MySQLDAO.user, MySQLDAO.password);
        events = new EventManager();
    }

    @Override
    public void subscribe (String eventType, IEventListener listener) {
        this.events.subscribe(eventType, listener);
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
    public List<Pet> getAllPets () throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Pet;");

        ResultSet result = statement.executeQuery();

        return makePetsFromResult(result);
    }

    @Override
    public List<City> getAllCities () throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM City;");

        ResultSet result = statement.executeQuery();

        List<City> cities = new ArrayList<>();

        while (result.next()) {
            cities.add(new City(
                    result.getInt("cityId"),
                    result.getString("name")
            ));
        }

        return cities;
    }

    @Override
    public List<Breed> getAllBreeds () throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Breed;");

        ResultSet result = statement.executeQuery();

        List<Breed> breeds = new ArrayList<>();

        while (result.next()) {
            breeds.add(new Breed(
                    result.getInt("breedId"),
                    result.getString("name")
            ));
        }

        return breeds;
    }

    @Override
    public List<Kind> getAllKinds () throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Kind");

        ResultSet result = statement.executeQuery();

        List<Kind> kinds = new ArrayList<>();

        while (result.next()) {
            kinds.add(new Kind(
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
    public int createCity (City city) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT City(name) VALUES(?)"
        );

        statement.setString(1, city.getName());

        statement.execute();

        events.notify(NEW_CITY, this);

        return getLastInsertID();
    }

    @Override
    public void deleteCity (City city) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM City WHERE cityId = ?"
        );

        statement.setInt(1, city.getCityId());

        lockTableWrite("City");

        statement.execute();

        events.notify(DELETE_CITY, this);

        unlockTables();
    }

    @Override
    public void updateCity (City city) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE City SET name = ? WHERE cityId = ?"
        );

        statement.setString(1, city.getName());
        statement.setInt(2, city.getCityId());

        statement.execute();

        events.notify(CITY_CHANGE, this);
    }

    @Override
    public int createKind (Kind kind) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT Kind(name) VALUES(?)"
        );

        statement.setString(1, kind.getName());

        statement.execute();

        events.notify(NEW_KIND, this);

        return getLastInsertID();
    }

    @Override
    public int createBreed (Breed breed) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT Breed(name) VALUES(?)"
        );

        statement.setString(1, breed.getName());

        statement.execute();

        events.notify(NEW_BREED, this);

        return getLastInsertID();
    }

    @Override
    public int createPet (Pet pet) throws Exception {
        events.notify(NEW_PET, this);

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
        statement.setDate(6, new Date(pet.getDateOfBirth().getTime()));
        statement.setBoolean(7, pet.getSex());

        statement.execute();

        return getLastInsertID();
    }

    @Override
    public void deletePet (Pet pet) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM Pet WHERE petId = ?"
        );

        statement.setInt(1, pet.getPetId());

        lockTableWrite("Pet");

        statement.execute();

        events.notify(DELETE_PET, this);

        unlockTables();
    }

    @Override
    public void updatePet (Pet pet) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Pet SET fkParentId = ?, fkOwnerId = ?, fkKindId = ?, fkBreedId = ?, name = ?, dateOfBirth = ?, sex = ? WHERE petId = ?"
        );

        if (pet.getFkParentId() == -1)
            statement.setNull(1, Types.INTEGER);
        else
            statement.setInt(1, pet.getFkBreedId());

        statement.setInt(2, pet.getFkOwnerId());
        statement.setInt(3, pet.getFkKindId());
        statement.setInt(4, pet.getFkBreedId());
        statement.setString(5, pet.getName());
        statement.setDate(6, new Date(pet.getDateOfBirth().getTime()));
        statement.setBoolean(7, pet.getSex());
        statement.setInt(8, pet.getPetId());

        lockTableWrite("Pet");

        statement.execute();

        events.notify(PET_CHANGE, this);

        unlockTables();
    }

    @Override
    public int createOwner(Owner owner) throws Exception {
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

        events.notify(NEW_OWNER, this);

        return getLastInsertID();
    }

    @Override
    public void deleteOwner(int ownerId) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM Owner WHERE ownerId = ?"
        );

        statement.setInt(1, ownerId);

        events.notify(DELETE_OWNER, this);

        statement.execute();
    }

    @Override
    public void updateOwner(Owner owner) throws Exception {
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

        events.notify(OWNER_CHANGE, this);

        unlockTables();
    }

    @Override
    public List<Owner> searchOwner(OwnerSearchObject ownerSearchObject) throws Exception {
        String searchQuery = "SELECT * FROM Owner ";
        boolean isFirst = true;
        List<String> order = new ArrayList<>();

        if (ownerSearchObject.ownerId != -1) {
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
        if (ownerSearchObject.fkCityId != -1) {
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
    public List<Pet> getOwnerPets (Owner owner) throws Exception {
        CallableStatement statement = connection.prepareCall("{CALL get_pets(?)}");

        statement.setInt(1, owner.getOwnerId());

        ResultSet result = statement.executeQuery();

        return makePetsFromResult(result);
    }

    private List<Pet> makePetsFromResult(ResultSet result) throws Exception {
        List<Pet> pets = new ArrayList<>();

        while (result.next()) {
            pets.add(makePetFromResultRow(result));
        }

        return pets;
    }

    private Pet makePetFromResultRow(ResultSet result) throws Exception {
        return new Pet(
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

    private List<Owner> makeOwnersFromResult(ResultSet result) throws Exception {
        List<Owner> owners = new ArrayList<>();

        while (result.next()) {
            owners.add(makeOwnerFromResultRow(result));
        }

        return owners;
    }

    private Owner makeOwnerFromResultRow(ResultSet result) throws Exception {
        return new Owner(
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

    public int getKindIdByName (String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT kindId FROM Kind WHERE name = ?;");
        statement.setString(1, name);

        ResultSet result = statement.executeQuery();
        result.next();

        return result.getInt("kindId");
    }
}


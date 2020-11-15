package com.NoSQl;
import com.NoSQl.DAO.*;
import com.github.javafaker.Faker;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static DAOFactory factory = new DAOFactory();

    public static void main (String[] args) throws Exception {
        runAggregationTest();
    }

    /**
    * @relatesTo lab6
    */
    private static void runAggregationTest () throws Exception {
        DatabaseService<ObjectId> mongoDBService = new DatabaseService<>(factory.createDao(EDAOType.MongoDB), null);

        mongoDBService.generateSampleDataSet();

        AggregationTestService aggregationTestService = new AggregationTestService();

        aggregationTestService.aggregateNumberOfOwnersPerCity();
        aggregationTestService.calculateNumberOfOwnersPerCity();

        aggregationTestService.aggregatePetsWithoutChilds();
        aggregationTestService.calculatePetsWithoutChilds();

        aggregationTestService.aggregateNumberOfChildsPerPet();
        aggregationTestService.calculateNumberOfChildsPerPet();

        aggregationTestService.aggregateTheMostPopularKindsOfPets();
        aggregationTestService.calculateTheMostPopularKindsOfPets();

        aggregationTestService.aggregateTheMostOldPets();
        aggregationTestService.calculateTheMostOldPets();
    }

    /**
     * @relatesTo lab5
     */
    private static void runMongoDBReplicationTest () throws Exception {
        int numberOfDocumentsToGenerate = 10000;
        List<City<ObjectId>> cities = new ArrayList<>();
        IDAO<ObjectId> mongoDAO = factory.createDao(EDAOType.MongoDB);
        Faker faker = new Faker();

        long insertTimeStart = System.nanoTime();
        long sleepTime = 0;

        while (numberOfDocumentsToGenerate > 0 ) {
            if (numberOfDocumentsToGenerate == 5000) {
                sleepTime = System.nanoTime();
                System.out.println("Kill the mongod intance, I will wait 20 seconds");
                Thread.sleep(20000);
                sleepTime = System.nanoTime() - sleepTime;
            }

            City<ObjectId> generatedCity = new City<>(
                    null,
                    faker.address().city()
            );

            int numberOfRetriesBeforeQuit = 3;

            while (numberOfRetriesBeforeQuit > 0) {
                try {
                    generatedCity.setCityId(mongoDAO.createCity(generatedCity));

                    cities.add(generatedCity);

                    numberOfRetriesBeforeQuit = 0;
                } catch (Exception error) {
                    System.out.println(error);

                    if (numberOfRetriesBeforeQuit - 1 == 0) {
                        System.out.println("Failed to write a document. Exiting from the program.");
                        return;
                    }

                    Thread.sleep(1000);

                    numberOfRetriesBeforeQuit--;
                }
            }

            numberOfDocumentsToGenerate--;
        }

        long insertEndTime = System.nanoTime();

        System.out.println("Insert took " + ((insertEndTime - insertTimeStart) - sleepTime ));

        long selectStartTime = System.nanoTime();

        List<City<ObjectId>> mongoDBCities = mongoDAO.getAllCities();

        long selectEndTime = System.nanoTime();

        System.out.println("Select took " + (selectEndTime - selectStartTime));

        System.out.println("Received: "  + mongoDBCities.size() + ". Inserted: 10000");

        cities.forEach((City<ObjectId> city) -> {
            boolean foundTheSameCity = false;
            for (City<ObjectId> mongoDBCity: mongoDBCities) {
                if (mongoDBCity.getCityId().equals(city.getCityId())) {
                    foundTheSameCity = true;

                    if (!mongoDBCity.getName().equals(city.getName())) {
                        System.out.println("City with id = " + city.getCityId() + " has incorrect saved city name");
                    }

                    break;
                }
            }

            if (!foundTheSameCity) {
                System.out.println("Can not find the city with id = " + city.getCityId());
            }
        });

    }

    /**
     * @relatesTo Lab4
     */
    private static void runMigrationFromMySQL_ToMongoDB () throws Exception {
        IDAO<ObjectId> mongoDAO = factory.createDao(EDAOType.MongoDB);
        IDAO<Integer> mysqlDAO = factory.createDao(EDAOType.MySQL);

        MigrationService<Integer, ObjectId> migrationService = new MigrationService<>();
        DatabaseService<Integer> mysqlService = new DatabaseService<>(mysqlDAO, -1);

        mysqlService.generateSampleDataSet();

        migrationService.migrateData(
                mysqlDAO,
                mongoDAO,
                -1,
                null
        );
    }

    /**
     * @relatesTo Lab4
     */
    private static void runMigrationFromMongoDB_ToMySQL () throws Exception {
        IDAO<ObjectId> mongoDAO = factory.createDao(EDAOType.MongoDB);
        IDAO<Integer> mysqlDAO = factory.createDao(EDAOType.MySQL);

        MigrationService<ObjectId, Integer> migrationService = new MigrationService<>();
        DatabaseService<ObjectId> mongoDBService = new DatabaseService<>(mongoDAO, null);

        mongoDBService.generateSampleDataSet();

        migrationService.migrateData(
                mongoDAO,
                mysqlDAO,
                null,
                -1
        );
    }

    /**
     * @relatesTo Lab3
     */
    private static void runMongoDBTests() throws Exception {
        IDAO<ObjectId> mongoDAO = factory.createDao(EDAOType.MongoDB);
        DatabaseService<ObjectId> mongoDBService = new DatabaseService<>(mongoDAO, null);

        mongoDBService.runPerformanceTests();;
    }

    /**
     * @relatesTo Lab4
     */
    private static void runMySQLTests() throws Exception {
        IDAO<Integer> mysqlDAO = factory.createDao(EDAOType.MySQL);
        DatabaseService<Integer> mysqlService = new DatabaseService<>(mysqlDAO, -1);

        mysqlService.runPerformanceTests();
    }

    /**
     * @relatesTo Lab2
     */
    private static void runMongoDBLab2Code () throws Exception {
        IDAO<ObjectId> mongoDAO = factory.createDao(EDAOType.MongoDB);
        DatabaseService<ObjectId> mongoDBService = new DatabaseService<>(mongoDAO, null);

        mongoDBService.runSimpleTestCase();
    }

    /**
     * @relatesTo Lab1
     */
    private static void runMySQLCode () throws Exception {
        IDAO<Integer> mysqlDAO = factory.createDao(EDAOType.MySQL);
        DatabaseService<Integer> mysqlService = new DatabaseService<>(mysqlDAO, -1);

        mysqlService.runSimpleTestCase();
    }
}

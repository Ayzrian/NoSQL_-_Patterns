package com.NoSQl.DAO;

import com.NoSQl.*;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseService<IDType> {
    private IDAO<IDType> dao;
    private IDType initializationIdValue;

    public DatabaseService(IDAO<IDType> dao, IDType initializationIdValue) {
        this.dao = dao;
        this.initializationIdValue = initializationIdValue;
    }


    /**
     * @desc Generates sample data set for future usage.
     * @relatesTo Lab4
     */
    public void generateSampleDataSet() throws Exception {
        Faker faker = new Faker();

        for (int i = 0; i< 5000 ; i++) {
            dao.createCity(new City<>(
                    initializationIdValue,
                    faker.address().city()
            ));

            dao.createBreed(new Breed<>(
                    initializationIdValue,
                    faker.cat().breed()
            ));

            dao.createKind(new Kind<>(
                    initializationIdValue,
                    faker.pokemon().name()
            ));
        }

        List<City<IDType>> cities = dao.getAllCities();

        for (int i = 0; i < 40000; i++) {
            int cityIndex = faker.random().nextInt(cities.size());
            City<IDType> city = cities.get(cityIndex);

            dao.createOwner(
                    new Owner<>(
                            initializationIdValue,
                            faker.address().firstName(),
                            faker.address().lastName(),
                            faker.phoneNumber().cellPhone(),
                            faker.internet().emailAddress(),
                            city.getCityId()
                    )
            );
        }

        List<Breed<IDType>> breeds = dao.getAllBreeds();
        List<Kind<IDType>> kinds = dao.getAllKinds();
        List<Owner<IDType>> owners = dao.searchOwner(new OwnerSearchObject<>());

        for (int i = 0; i < 80000; i++) {
            Breed<IDType> breed = breeds.get(faker.random().nextInt(breeds.size()));
            Kind<IDType> kind = kinds.get(faker.random().nextInt(kinds.size()));
            Owner<IDType> owner = owners.get(faker.random().nextInt(owners.size()));

            dao.createPet(new Pet<>(
                    initializationIdValue,
                    initializationIdValue,
                    owner.getOwnerId(),
                    kind.getKindId(),
                    breed.getBreedId(),
                    faker.animal().name(),
                    faker.date().birthday(),
                    faker.random().nextBoolean()
            ));
        }

        List<Pet<IDType>> pets = dao.getAllPets();

        for (int i = 0; i < 30000; i++) {
            Breed<IDType> breed = breeds.get(faker.random().nextInt(breeds.size()));
            Kind<IDType> kind = kinds.get(faker.random().nextInt(kinds.size()));
            Owner<IDType> owner = owners.get(faker.random().nextInt(owners.size()));
            Pet<IDType> parentPet = pets.get(faker.random().nextInt(pets.size()));

            dao.createPet(new Pet<>(
                    initializationIdValue,
                    parentPet.getPetId(),
                    owner.getOwnerId(),
                    kind.getKindId(),
                    breed.getBreedId(),
                    faker.animal().name(),
                    faker.date().birthday(),
                    faker.random().nextBoolean()
            ));
        }
    }

    /**
     * @desc Implements test cases for Lab3
     * @relatesTo Lab3
     */
    public void runPerformanceTests () throws Exception {
        int[] tests = {100, 1000, 10000, 50000, 100000, 200000};

        Faker faker = new Faker();

        City<IDType> randomCity = new City<>(initializationIdValue, faker.address().cityName());

        randomCity.setCityId(dao.createCity(randomCity));

        OwnerSearchObject<IDType> searchObject = new OwnerSearchObjectBuilder<IDType>()
                .withEmail("ayzrian@gmail.com")
                .withFirstName("Alan")
                .build();

        List<TestResultsObject> results = new ArrayList<>();

        for (int numberOfEntries: tests) {
            TestResultsObject testResult = new TestResultsObject();
            testResult.numberOfEntries = numberOfEntries;

            long startTimeOfInsert = System.nanoTime();

            while (numberOfEntries > 0) {
                Owner<IDType> owner = new Owner<>(
                        initializationIdValue,
                        faker.address().firstName(),
                        faker.address().lastName(),
                        faker.phoneNumber().cellPhone(),
                        faker.internet().emailAddress(),
                        randomCity.getCityId()
                );

                dao.createOwner(owner);

                numberOfEntries--;
            }

            long endTimeOfInsert = System.nanoTime();
            testResult.timeToInsertMs = endTimeOfInsert - startTimeOfInsert;

            long startTimeOfSelect = System.nanoTime();
            dao.searchOwner(searchObject);
            long endTimeOfSelect = System.nanoTime();

            testResult.timeToSelectMs = endTimeOfSelect - startTimeOfSelect;

            results.add(testResult);

            dao.dropOwners();
        }


        for (TestResultsObject result: results) {
            System.out.println("===================================================");
            System.out.println("The insert of " + result.numberOfEntries + " took "
                    + result.timeToInsertMs + " the search took " + result.timeToSelectMs);
            System.out.println("===================================================");
        }
    }

    /**
     * @desc Implements basic example of DAO usage for the first two labs.
     * @relatesTo Lab1, Lab2
     */
    public void runSimpleTestCase () throws Exception {
        Kind<IDType> catKind = new Kind<>(
                initializationIdValue,
                "Cat"
        );

        catKind.setKindId(dao.createKind(catKind));

        Breed<IDType> myBreed1 = new Breed<>(
                initializationIdValue,
                "Breed1"
        );

        myBreed1.setBreedId(dao.createBreed(myBreed1));

        City<IDType> kharkov = new City<>(initializationIdValue, "Kharkov");
        City<IDType> kiev = new City<>(initializationIdValue, "Kiev");

        kharkov.setCityId(dao.createCity(kharkov));
        kiev.setCityId(dao.createCity(kiev));

        System.out.println(kharkov);
        System.out.println(kiev);


        Owner<IDType> owner1 = new Owner<>(
                initializationIdValue,
                "Nazar",
                "Romankiv",
                "0508513949",
                "nazarii.romankiv@nure.ua",
                kharkov.getCityId()
        );

        Owner<IDType> owner2 = new Owner<>(
                initializationIdValue,
                "Danil",
                "Dudnik",
                "0508513945",
                "danil.dudnik@nure.ua",
                kiev.getCityId()
        );

        owner1.setOwnerId(dao.createOwner(owner1));
        owner2.setOwnerId(dao.createOwner(owner2));

        System.out.println("====== Get Owners =====");

        OwnerSearchObjectBuilder<IDType> ownerSearchObjectBuilder = new OwnerSearchObjectBuilder<>();

        List<Owner<IDType>> owners = dao.searchOwner(
                ownerSearchObjectBuilder.build()
        );

        for (Owner<IDType> owner: owners) {
            System.out.println(owner);
        }

        System.out.println("====== Get Owners by city and name =====");

        List<Owner<IDType>> ownersByCityAndName = dao.searchOwner(
                ownerSearchObjectBuilder
                        .withFkCityId(kharkov.getCityId())
                        .withFirstName(owner1.getFirstName())
                        .build()
        );

        for (Owner<IDType> owner: ownersByCityAndName) {
            System.out.println(owner);
        }

        Pet<IDType> pet1 = new Pet<>(
                initializationIdValue,
                initializationIdValue,
                owner1.getOwnerId(),
                catKind.getKindId(),
                myBreed1.getBreedId(),
                "Murka",
                new Date(),
                false
        );

        pet1.setPetId(dao.createPet(pet1));


        Pet<IDType> pet2 = new Pet<>(
                initializationIdValue,
                pet1.getPetId(),
                owner1.getOwnerId(),
                catKind.getKindId(),
                myBreed1.getBreedId(),
                "Baracuda",
                new Date(),
                true
        );

        pet2.setPetId(dao.createPet(pet2));

        List<Pet<IDType>> owner1Pets = dao.getOwnerPets(owner1);

        System.out.println("==== GET OWNER PETS WITH STORED PROCEDURE ====");

        for (Pet<IDType> pet: owner1Pets) {
            System.out.println(pet);
        }


        pet2.setName("New NAME!");

        dao.updatePet(pet2);

        owner1Pets = dao.getOwnerPets(owner1);

        System.out.println("==== GET OWNER PETS WITH STORED PROCEDURE AFTER UPDATE ====");

        for (Pet<IDType> pet: owner1Pets) {
            System.out.println(pet);
        }


        dao.deletePet(pet1);

        owner1Pets = dao.getOwnerPets(owner1);

        System.out.println("==== GET OWNER PETS WITH STORED PROCEDURE AFTER DELETE ====");

        for (Pet<IDType> pet: owner1Pets) {
            System.out.println(pet);
        }
    }
}

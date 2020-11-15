package com.NoSQl;

import com.mongodb.client.*;
import org.bson.BsonArray;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.print.Doc;
import javax.xml.transform.Result;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Filters.*;

/**
 * @relatesTo Lab6
 */
public class AggregationTestService {
    MongoClient mongo;
    MongoDatabase db;
    MongoCollection<Document> owners;
    MongoCollection<Document> cities;
    MongoCollection<Document> pets;
    MongoCollection<Document> kinds;

    AggregationTestService () {
        this.mongo = MongoClients.create();
        this.db = mongo.getDatabase("app");
        this.owners = this.db.getCollection("Owners");
        this.cities = this.db.getCollection("Cities");
        this.pets = this.db.getCollection("Pets");
        this.kinds = this.db.getCollection("Kinds");
    }

    public void aggregateNumberOfOwnersPerCity () {
        System.out.println("----------------------------- aggregateNumberOfOwnersPerCity ------------------------------");

        long startTime = System.nanoTime();

        Bson group = group("$fkCityId", sum("ownersPerCity", 1));
        Bson lookup = lookup("Cities", "_id", "_id", "city");
        Bson unwind = unwind("city");
        Bson project = project(new Document().append("city", 1).append("ownersPerCity", 1));

        AggregateIterable<Document> result = this.owners.aggregate(Arrays.asList(
                group,
                lookup,
                unwind,
                project
        ));

        long endTime = System.nanoTime();

        System.out.println("Aggregation of owners per city took - " + (endTime - startTime));
    }

    public void calculateNumberOfOwnersPerCity () throws Exception {
        System.out.println("----------------------------- calculateNumberOfOwnersPerCity ------------------------------");
        long startTime = System.nanoTime();

        HashMap<ObjectId, Integer> ownerPerCityMap = new HashMap<>();

        this.cities.find().forEach((Document cityObject) -> {
            ownerPerCityMap.put(cityObject.getObjectId("_id"), 0);
        });

        this.owners.find().forEach((Document ownerDocument) -> {
            ObjectId cityId = ownerDocument.getObjectId("fkCityId");
            int currentNumberOfOwners = ownerPerCityMap.get(cityId);
            ownerPerCityMap.put(cityId, currentNumberOfOwners + 1);
        });

        long endTime = System.nanoTime();

        System.out.println("Calculation of number of owners per city took " + (endTime - startTime));
    }

    public void aggregatePetsWithoutChilds() throws Exception {
        System.out.println("----------------------------- aggregatePetsWithoutChilds ------------------------------");

        long startTime = System.nanoTime();

        Bson lookup = lookup("Pets", "_id", "fkParentId", "childs");
        Bson group = group("$_id", sum("numberOfChilds", new Document().append("$size", "$childs")));
        Bson match = match(eq("numberOfChilds", 0));
        Bson project = project(new Document().append("numberOfChilds", 1).append("_id", 1));

        AggregateIterable<Document> result = this.pets.aggregate(Arrays.asList(lookup, group, match, project));

        long endTime = System.nanoTime();

        System.out.println("Aggregation of pets without chilsd took - " + (endTime - startTime));
    }

    public void calculatePetsWithoutChilds() throws Exception {
        System.out.println("----------------------------- calculatePetsWithoutChilds ------------------------------");

        long startTime = System.nanoTime();

        HashMap<ObjectId, Integer> childsPerPetMap = new HashMap<>();

        this.pets.find().forEach((Document petDocument) -> {
            ObjectId id = petDocument.getObjectId("_id");
            ObjectId parentId = petDocument.getObjectId("fkParentId");

            if (!childsPerPetMap.containsKey(id))
                childsPerPetMap.put(id, 0);

            if (childsPerPetMap.containsKey(parentId)) {
                int childsNum = childsPerPetMap.get(parentId);
                childsPerPetMap.put(parentId, childsNum + 1);
            } else childsPerPetMap.put(parentId, 1);

        });

        List<ObjectId> petsWithoutChilds = new ArrayList<>();

        for(Map.Entry<ObjectId, Integer> result: childsPerPetMap.entrySet()) {
            if (result.getValue() == 0)
                petsWithoutChilds.add(result.getKey());
        }

        long endTime = System.nanoTime();

        System.out.println("Calculation of number of pets without childs took " + (endTime - startTime));

        System.out.println("Number of pet without parents " + petsWithoutChilds.size());
    }

    public void aggregateNumberOfChildsPerPet () throws Exception {
        System.out.println("----------------------------- aggregateNumberOfChildsPerPet ------------------------------");

        long startTime = System.nanoTime();

        Bson group = group("$fkParentId", sum("childs", 1));
        Bson lookup = lookup("Pets", "_id", "_id", "parent");
        Bson unwind = unwind("parent");
        Bson project = project(new Document().append("parent", 1).append("childs", 1));

        this.pets.aggregate(Arrays.asList(group, lookup, unwind, project));

        long endTime = System.nanoTime();

        System.out.println("Aggregation of number of childs per pet took " + (endTime - startTime));
    }

    public void calculateNumberOfChildsPerPet () throws Exception {
        System.out.println("----------------------------- calculateNumberOfChildsPerPet ------------------------------");

        long startTime = System.nanoTime();

        HashMap<ObjectId, Integer> childsPerPet = new HashMap<>();

        this.pets.find().forEach((Document petDocument) -> {
            ObjectId petId = petDocument.getObjectId("_id");
            ObjectId parentId = petDocument.getObjectId("fkParentId");

            if (!childsPerPet.containsKey(petId))
                childsPerPet.put(petId, 0);

            if (childsPerPet.containsKey(parentId)) {
                int numberOfChilds = childsPerPet.get(parentId);
                childsPerPet.put(parentId, numberOfChilds + 1);
            } else childsPerPet.put(parentId, 1);
        });

        long endTime = System.nanoTime();

        System.out.println("Calculation of number of childs per pet took " + (endTime - startTime));
    }

    public void aggregateTheMostPopularKindsOfPets () throws Exception {
        System.out.println("----------------------------- aggregateTheMostPopularKindsOfPets ------------------------------");


        long startTime = System.nanoTime();

        Bson group = group("$fkKindId", sum("numberOfPetsPerKind", 1));
        Bson sort = sort(eq("numberOfPetsPerKind", -1));
        Bson limit = limit(1);
        Bson lookup = lookup("Kinds", "_id", "_id", "kind" );
        Bson unwind = unwind("$kind");
        Bson replaceWith = replaceWith("$kind");

        AggregateIterable<Document> result = this.pets.aggregate(Arrays.asList(
                group, sort, limit, lookup, unwind, replaceWith
        ));

        long endTime = System.nanoTime();

        System.out.println("Calculation of the most popular kins took " + (endTime - startTime));

        for (Document kindInfo: result)
            System.out.println("One of the most popular kinds is - " + kindInfo.getString("name"));
    }

    public void calculateTheMostPopularKindsOfPets () throws Exception {
        System.out.println("----------------------------- calculateTheMostPopularKindsOfPets ------------------------------");

        long startTime = System.nanoTime();

        HashMap<ObjectId, Integer> petsPerKind = new HashMap<>();

        this.pets.find().forEach((Document petDocument) -> {
            ObjectId kindId = petDocument.getObjectId("fkKindId");
            if (petsPerKind.containsKey(kindId)) {
                int currentNumber = petsPerKind.get(kindId);
                petsPerKind.put(kindId, currentNumber + 1);
                return;
            }

            petsPerKind.put(kindId, 1);
        });

        Set<Map.Entry<ObjectId, Integer>> setOfResults = petsPerKind.entrySet();
        int max = -1;

        for(Map.Entry<ObjectId, Integer> result: setOfResults) {
            if (result.getValue() > max) {
                max = result.getValue();
            }
        }

        List<ObjectId> mostPopularKinds = new ArrayList<>();


        for(Map.Entry<ObjectId, Integer> result: setOfResults) {
            if (result.getValue() == max) {
                mostPopularKinds.add(result.getKey());
            }
        }

        long endTime = System.nanoTime();

        System.out.println("Calculation of the most popular kins took " + (endTime - startTime));

        for (ObjectId kindId : mostPopularKinds) {
            AtomicReference<String> kindName = new AtomicReference<>("");

            this.kinds
                    .find(new Document().append("_id", kindId))
                    .forEach((Document kind) -> {
                        kindName.set(kind.getString("name"));
                    });

            System.out.println("One of the most popular kinds is - " + kindName);
        }
    }

    public void aggregateTheMostOldPets () throws Exception {
        System.out.println("----------------------------- aggregateTheMostOldPets ------------------------------");

        long startTime = System.nanoTime();

        Bson group = group(null, min("theOldestBirthDate", "$dateOfBirth"));
        Bson lookup = lookup("Pets", "theOldestBirthDate", "dateOfBirth", "pet");
        Bson unwind = unwind("$pet");
        Bson replaceWith = replaceWith("$pet");


        AggregateIterable<Document> result = this.pets.aggregate(Arrays.asList(
                group, lookup, unwind, replaceWith
        ));

        long endTime = System.nanoTime();

        System.out.println("Aggregation of the most oldest pets took " + (endTime - startTime));
        for (Document petInfo: result)
            System.out.println("One of the most oldest pets id - " + petInfo.getObjectId("_id"));
    }

    public void calculateTheMostOldPets () throws Exception {
        System.out.println("----------------------------- calculateTheMostOldPets ------------------------------");

        long startTime = System.nanoTime();

        List<Document> pets = new ArrayList<>();

        this.pets.find().forEach(pets::add);

        Date theOldestBirthOfDate = new Date();

        for (Document pet: pets) {
            Date petDateOfBirth = pet.getDate("dateOfBirth");
            if (petDateOfBirth.before(theOldestBirthOfDate))
                theOldestBirthOfDate = petDateOfBirth;
        }

        List<ObjectId> petsIds = new ArrayList<>();

        for (Document pet: pets) {
            if (pet.getDate("dateOfBirth") == theOldestBirthOfDate)
                petsIds.add(pet.getObjectId("_id"));
        }

        long endTime = System.nanoTime();

        System.out.println("Calculation of the most old pets took " + (endTime - startTime));

        for (ObjectId id: petsIds)
            System.out.println("One of the most oldest pets id - " + id);

    }
}

package com.NoSQl.DAO;

import com.NoSQl.*;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MongoDAO implements IDAO<ObjectId> {
    private static MongoDAO instance = null;
    private MongoClient mongoClient = null;
    private MongoDatabase database = null;
    private MongoCollection<Document> ownersCollection = null;
    private MongoCollection<Document> kindsCollection = null;
    private MongoCollection<Document> breedsCollection = null;
    private MongoCollection<Document> petsCollection = null;
    private MongoCollection<Document> citiesCollection = null;

    private MongoDAO() {
//        lab 5 replica set settings
//        MongoClientSettings.Builder settings = MongoClientSettings.builder();
//        settings
//                .applyConnectionString(new ConnectionString("mongodb://localhost:27017,localhost:27018,localhost:27019"))
//                .writeConcern(WriteConcern.W1);
//        mongoClient = MongoClients.create(settings.build());

        mongoClient = MongoClients.create();
        database = mongoClient.getDatabase("app");
        ownersCollection = database.getCollection("Owners");
        kindsCollection = database.getCollection("Kinds");
        breedsCollection = database.getCollection("Breeds");
        petsCollection = database.getCollection("Pets");
        citiesCollection = database.getCollection("Cities");
    }

    public static synchronized MongoDAO getInstance () {
        if (instance == null)
            instance = new MongoDAO();

        return instance;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mongoClient.close();
    }

    @Override
    public List<Pet<ObjectId>> getAllPets () throws Exception {
        return mapResultToPets(petsCollection.find());
    }

    @Override
    public List<Kind<ObjectId>> getAllKinds () throws Exception {
        List<Kind<ObjectId>> kinds = new ArrayList<>();

        kindsCollection.find().forEach((Document cityDocument) -> {
            kinds.add(new Kind<>(
                    cityDocument.getObjectId("_id"),
                    cityDocument.getString("name")
            ));
        });

        return kinds;
    }

    @Override
    public List<Breed<ObjectId>> getAllBreeds () throws Exception {
        List<Breed<ObjectId>> breeds = new ArrayList<>();

        breedsCollection.find().forEach((Document breedDocument) -> {
            breeds.add(new Breed<>(
                    breedDocument.getObjectId("_id"),
                    breedDocument.getString("name")
            ));
        });

        return breeds;
    }

    @Override
    public List<City<ObjectId>> getAllCities () throws  Exception {
        List<City<ObjectId>> cities = new ArrayList<>();

        citiesCollection.find().forEach((Document cityDocument) -> {
            cities.add(new City<>(
                    cityDocument.getObjectId("_id"),
                    cityDocument.getString("name")
            ));
        });

        return cities;
    }

    @Override
    public void dropOwners() throws Exception {
        ownersCollection.deleteMany(new Document());
    }

    @Override
    public ObjectId createCity(City<ObjectId> city) throws Exception {
        InsertOneResult result = citiesCollection.insertOne(
                new Document().append("name", city.getName())
        );

        return getObjectId(result);
    }

    @Override
    public void deleteCity(City<ObjectId> city) throws Exception {
        citiesCollection.deleteOne(new Document().append("_id", city.getCityId()));
    }

    @Override
    public ObjectId createKind(Kind<ObjectId> kind) throws Exception {
        InsertOneResult result = kindsCollection.insertOne(new Document().append("name", kind.getName()));

        return getObjectId(result);
    }

    @Override
    public ObjectId createBreed(Breed<ObjectId> breed) throws Exception {
        InsertOneResult result = breedsCollection.insertOne(new Document().append("name", breed.getName()));

        return getObjectId(result);
    }

    @Override
    public ObjectId createPet(Pet<ObjectId> pet) throws Exception {
        InsertOneResult result = petsCollection.insertOne(
                new Document()
                        .append("fkParentId", pet.getFkParentId())
                        .append("fkOwnerId", pet.getFkOwnerId())
                        .append("fkKindId", pet.getFkKindId())
                        .append("fkBreedId", pet.getFkBreedId())
                        .append("name", pet.getName())
                        .append("dateOfBirth", pet.getDateOfBirth())
                        .append("sex", pet.getSex())
        );


        return getObjectId(result);
    }

    @Override
    public void updatePet(Pet<ObjectId> pet) throws Exception {
        petsCollection.updateOne(
                    new Document().append("_id", pet.getPetId()),
                    new Document()
                            .append("$set", new Document()
                                    .append("fkParentId", pet.getFkParentId())
                                    .append("fkOwnerId", pet.getFkOwnerId())
                                    .append("fkKindId", pet.getFkKindId())
                                    .append("fkBreedId", pet.getFkBreedId())
                                    .append("name", pet.getName())
                                    .append("dateOfBirth", pet.getDateOfBirth())
                                    .append("sex", pet.getSex())
                            )
        );
    }

    @Override
    public void deletePet(Pet<ObjectId> pet) throws Exception {
        petsCollection.deleteOne(new Document().append("_id", pet.getPetId()));
    }

    @Override
    public ObjectId createOwner(Owner<ObjectId> owner) throws Exception {
        InsertOneResult result = ownersCollection.insertOne(
                new Document()
                    .append("firstName", owner.getFirstName())
                    .append("lastName", owner.getLastName())
                    .append("phoneNumber", owner.getPhoneNumber())
                    .append("email", owner.getEmail())
                    .append("fkCityId", owner.getFkCityId())
        );

        return getObjectId(result);
    }

    @Override
    public void deleteOwner(ObjectId ownerId) throws Exception {
        ownersCollection.deleteOne(new Document().append("_id", ownerId));
    }

    @Override
    public void updateOwner(Owner<ObjectId> owner) throws Exception {
        ownersCollection.updateOne(
                new Document().append("_id", owner.getOwnerId()),
                new Document()
                    .append("$set", new Document()
                            .append("firstName", owner.getFirstName())
                            .append("lastName", owner.getLastName())
                            .append("phoneNumber", owner.getPhoneNumber())
                            .append("email", owner.getEmail())
                            .append("fkCityId", owner.getFkCityId())
                    )
        );
    }

    @Override
    public List<Owner<ObjectId>> searchOwner(OwnerSearchObject<ObjectId> ownerSearchObject) throws Exception {
        List<Owner<ObjectId>> list = new ArrayList<>();
        Document filter = new Document();

        if (ownerSearchObject.ownerId != null) filter.append("_id", ownerSearchObject.ownerId);
        if (ownerSearchObject.firstName != null) filter.append("firstName", ownerSearchObject.firstName);
        if (ownerSearchObject.lastName != null) filter.append("lastName", ownerSearchObject.lastName);
        if (ownerSearchObject.phoneNumber != null) filter.append("phoneNumber", ownerSearchObject.phoneNumber);
        if (ownerSearchObject.email != null) filter.append("email", ownerSearchObject.email);
        if (ownerSearchObject.fkCityId != null) filter.append("fkCityId", ownerSearchObject.fkCityId);

        ownersCollection.find(filter).map((Document ownerDocument) -> {
            return new Owner<ObjectId>(
                    ownerDocument.getObjectId("_id"),
                    ownerDocument.getString("firstName"),
                    ownerDocument.getString("lastName"),
                    ownerDocument.getString("phoneNumber"),
                    ownerDocument.getString("email"),
                    ownerDocument.getObjectId("fkCityId")
            );
        }).forEach(list::add);

        return list;
    }

    @Override
    public List<Pet<ObjectId>> getOwnerPets(Owner<ObjectId> owner) throws Exception {
        return mapResultToPets(
                petsCollection.find(
                        new Document().append("fkOwnerId", owner.getOwnerId()
                        )
                )
        );

    }

    private List<Pet<ObjectId>> mapResultToPets (FindIterable<Document> result) {
        List<Pet<ObjectId>> list = new ArrayList<>();

        result.map((Document pet)  -> {
            return new Pet<ObjectId>(
                    pet.getObjectId("_id"),
                    pet.getObjectId("fkParentId"),
                    pet.getObjectId("fkOwnerId"),
                    pet.getObjectId("fkKindId"),
                    pet.getObjectId("fkBreedId"),
                    pet.getString("name"),
                    pet.getDate("dateOfBirth"),
                    pet.getBoolean("sex")
            );
        }).forEach(list::add);

        return list;
    }

    private ObjectId getObjectId(InsertOneResult result ) {
        return Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue();
    }
}

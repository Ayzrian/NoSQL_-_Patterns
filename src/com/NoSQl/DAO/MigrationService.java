package com.NoSQl.DAO;

import com.NoSQl.*;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @desc Implements abstract migration flow in scope of Lab4.
 * @relatesTo Lab4
 * @param <FromDB_IDType>
 * @param <ToDB_IDType>
 */
public class MigrationService<FromDB_IDType, ToDB_IDType> {
    public void migrateData (
            IDAO<FromDB_IDType> fromDAO,
            IDAO<ToDB_IDType> toDAO,
            FromDB_IDType fromDAO_InitializationIdValue,
            ToDB_IDType toDAO_InitializationIdValue
    ) throws Exception {
        List<City<FromDB_IDType>> cities = fromDAO.getAllCities();
        HashMap<FromDB_IDType, ToDB_IDType> citiesIdsMap = new HashMap<>();

        for (City<FromDB_IDType> city : cities) {
            ToDB_IDType newId = toDAO.createCity(new City<>(
                    toDAO_InitializationIdValue,
                    city.getName()
            ));

            citiesIdsMap.put(city.getCityId(), newId);
        }

        List<Kind<FromDB_IDType>> kinds = fromDAO.getAllKinds();
        HashMap<FromDB_IDType, ToDB_IDType> kindsIdsMap = new HashMap<>();

        for (Kind<FromDB_IDType> kind : kinds) {
            ToDB_IDType newId = toDAO.createKind(new Kind<>(
                    toDAO_InitializationIdValue,
                    kind.getName()
            ));

            kindsIdsMap.put(kind.getKindId(), newId);
        }

        List<Breed<FromDB_IDType>> breeds = fromDAO.getAllBreeds();
        HashMap<FromDB_IDType, ToDB_IDType> breedsIdsMap = new HashMap<>();

        for (Breed<FromDB_IDType> breed : breeds) {
            ToDB_IDType newId = toDAO.createBreed(new Breed<>(
                    toDAO_InitializationIdValue,
                    breed.getName()
            ));

            breedsIdsMap.put(breed.getBreedId(), newId);
        }

        List<Owner<FromDB_IDType>> owners = fromDAO.searchOwner(new OwnerSearchObject<>());
        HashMap<FromDB_IDType, ToDB_IDType> ownersIdsMap = new HashMap<>();

        for (Owner<FromDB_IDType> owner: owners) {
            ToDB_IDType newId = toDAO.createOwner(
                    new Owner<>(
                            toDAO_InitializationIdValue,
                            owner.getFirstName(),
                            owner.getLastName(),
                            owner.getPhoneNumber(),
                            owner.getEmail(),
                            citiesIdsMap.get(owner.getFkCityId())
                    )
            );

            ownersIdsMap.put(owner.getOwnerId(), newId);
        }

        List<Pet<FromDB_IDType>> pets = fromDAO.getAllPets();
        // Contains Pet instance with id in toDAO database and ID of original fkParentId
        List<Pair<Pet<ToDB_IDType>, FromDB_IDType>> petsToSetNewParentId = new ArrayList<>();
        HashMap<FromDB_IDType, ToDB_IDType> petsIdsMap = new HashMap<>();

        for (Pet<FromDB_IDType> pet: pets) {
            Pet<ToDB_IDType> newPet = new Pet<>(
                    toDAO_InitializationIdValue,
                    toDAO_InitializationIdValue,
                    ownersIdsMap.get(pet.getFkOwnerId()),
                    kindsIdsMap.get(pet.getFkKindId()),
                    breedsIdsMap.get(pet.getFkBreedId()),
                    pet.getName(),
                    pet.getDateOfBirth(),
                    pet.getSex()
            );

            ToDB_IDType newId = toDAO.createPet(newPet);
            newPet.setPetId(newId);

            if (pet.getFkParentId() != fromDAO_InitializationIdValue) {
               petsToSetNewParentId.add(new Pair<>(newPet, pet.getFkParentId()));
            }

            petsIdsMap.put(pet.getPetId(), newId);
        }

        // Updating ids of fkParentId.
        for (Pair<Pet<ToDB_IDType>, FromDB_IDType> pair : petsToSetNewParentId) {
            Pet<ToDB_IDType> pet = pair.getValue0();
            FromDB_IDType originalFkParentId = pair.getValue1();
            pet.setFkParentId(petsIdsMap.get(originalFkParentId));

            toDAO.updatePet(pet);
        }

    }
}

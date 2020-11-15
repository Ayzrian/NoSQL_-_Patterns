package com.NoSQl.DAO;

import com.NoSQl.*;

import java.util.List;

public interface IDAO<IDType> {
    public IDType createCity (City<IDType> city) throws Exception;
    public void deleteCity (City<IDType> city) throws Exception;
    public IDType createKind (Kind<IDType> kind) throws Exception;
    public IDType createBreed (Breed<IDType> breed) throws Exception;
    public IDType createPet (Pet<IDType> pet) throws Exception;
    public void updatePet (Pet<IDType> pet) throws Exception;
    public void deletePet (Pet<IDType> pet) throws Exception;
    public IDType createOwner (Owner<IDType> owner) throws Exception;
    public void deleteOwner (IDType ownerId) throws Exception;
    public void updateOwner (Owner<IDType> owner) throws Exception;
    public List<Owner<IDType>> searchOwner (OwnerSearchObject<IDType> ownerSearchObject) throws Exception;
    public List<Pet<IDType>> getOwnerPets (Owner<IDType> owner) throws Exception;
    public void dropOwners () throws Exception;
    public List<Pet<IDType>> getAllPets () throws Exception;
    public List<City<IDType>> getAllCities () throws Exception;
    public List<Breed<IDType>> getAllBreeds () throws Exception;
    public List<Kind<IDType>> getAllKinds () throws Exception;
}

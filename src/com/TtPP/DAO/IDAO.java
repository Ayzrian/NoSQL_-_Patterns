package com.TtPP.DAO;

import com.TtPP.common.EventManager;
import com.TtPP.common.IEventListener;
import com.TtPP.entities.*;

import java.util.EventListener;
import java.util.List;

public interface IDAO {
    public void subscribe (String eventType, IEventListener eventListener);
    public int createCity (City city) throws Exception;
    public void deleteCity (City city) throws Exception;
    public void updateCity (City city) throws  Exception;
    public int createKind (Kind kind) throws Exception;
    public int createBreed (Breed breed) throws Exception;
    public int createPet (Pet pet) throws Exception;
    public void updatePet (Pet pet) throws Exception;
    public void deletePet (Pet pet) throws Exception;
    public int createOwner (Owner owner) throws Exception;
    public void deleteOwner (int ownerId) throws Exception;
    public void updateOwner (Owner owner) throws Exception;
    public List<Owner> searchOwner (OwnerSearchObject ownerSearchObject) throws Exception;
    public List<Pet> getOwnerPets (Owner owner) throws Exception;
    public int getKindIdByName (String name) throws Exception;
    public void dropOwners () throws Exception;
    public List<Pet> getAllPets () throws Exception;
    public List<City> getAllCities () throws Exception;
    public List<Breed> getAllBreeds () throws Exception;
    public List<Kind> getAllKinds () throws Exception;
}

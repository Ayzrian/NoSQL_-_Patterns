package com.TtPP.DAO;

import com.TtPP.common.EventManager;
import com.TtPP.common.IEventListener;
import com.TtPP.entities.*;

import java.util.EventListener;
import java.util.List;

public interface IDAO {
    public void subscribe (String eventType, IEventListener eventListener);
    public int createCity (City city, ERole userRole) throws Exception;
    public void deleteCity (City city, ERole userRole) throws Exception;
    public void updateCity (City city, ERole userRole) throws  Exception;
    public int createKind (Kind kind, ERole userRole) throws Exception;
    public int createBreed (Breed breed, ERole userRole) throws Exception;
    public int createPet (Pet pet, ERole userRole) throws Exception;
    public void updatePet (Pet pet, ERole userRole) throws Exception;
    public void deletePet (Pet pet, ERole userRole) throws Exception;
    public int createOwner (Owner owner, ERole userRole) throws Exception;
    public void deleteOwner (int ownerId, ERole userRole) throws Exception;
    public void updateOwner (Owner owner, ERole userRole) throws Exception;
    public User login (String login, String password) throws Exception;
    public User register (String login, String password, Role role) throws Exception;
    public List<Owner> searchOwner (OwnerSearchObject ownerSearchObject, ERole userRole) throws Exception;
    public List<Pet> getOwnerPets (Owner owner, ERole userRole) throws Exception;
    public int getKindIdByName (String name, ERole userRole) throws Exception;
    public void dropOwners (ERole userRole) throws Exception;
    public List<Pet> getAllPets (ERole userRole) throws Exception;
    public List<City> getAllCities (ERole userRole) throws Exception;
    public List<Breed> getAllBreeds (ERole userRole) throws Exception;
    public List<Kind> getAllKinds (ERole userRole) throws Exception;
}

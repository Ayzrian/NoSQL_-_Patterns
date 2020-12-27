package com.TtPP.DAO;

import com.TtPP.common.IEventListener;
import com.TtPP.entities.*;

import java.util.List;

public class DAO_Proxy implements IDAO {
    private IDAO dao;

    public DAO_Proxy(IDAO dao) {
        this.dao = dao;
    }

    @Override
    public void subscribe(String eventType, IEventListener eventListener) {
        this.dao.subscribe(eventType, eventListener);
    }

    @Override
    public int createCity(City city, ERole userRole) throws Exception {
        if (userRole == ERole.ADMIN)
            return this.dao.createCity(city, userRole);
        throw new NotEnoughPermissionsException();
    }

    @Override
    public void deleteCity(City city, ERole userRole) throws Exception {
        if (userRole == ERole.ADMIN)
            this.dao.deleteCity(city, userRole);
        throw new NotEnoughPermissionsException();
    }

    @Override
    public void updateCity(City city, ERole userRole) throws Exception {
        if (userRole == ERole.ADMIN)
            this.dao.updateCity(city, userRole);
        throw new NotEnoughPermissionsException();
    }

    @Override
    public int createKind(Kind kind, ERole userRole) throws Exception {
        if (userRole == ERole.ADMIN)
            return this.dao.createKind(kind, userRole);
        throw new NotEnoughPermissionsException();
    }

    @Override
    public int createBreed(Breed breed, ERole userRole) throws Exception {
        if (userRole == ERole.ADMIN)
            return this.dao.createBreed(breed, userRole);
        throw new NotEnoughPermissionsException();
    }

    @Override
    public int createPet(Pet pet, ERole userRole) throws Exception {
        if (userRole == ERole.ADMIN)
            return this.dao.createPet(pet, userRole);
        throw new NotEnoughPermissionsException();
    }

    @Override
    public void updatePet(Pet pet, ERole userRole) throws Exception {
        if (userRole == ERole.ADMIN)
            this.dao.updatePet(pet, userRole);
        throw new NotEnoughPermissionsException();
    }

    @Override
    public void deletePet(Pet pet, ERole userRole) throws Exception {
        if (userRole == ERole.ADMIN)
            this.dao.deletePet(pet, userRole);
        throw new NotEnoughPermissionsException();
    }

    @Override
    public int createOwner(Owner owner, ERole userRole) throws Exception {
        if (userRole == ERole.ADMIN)
            this.dao.createOwner(owner, userRole);
        throw new NotEnoughPermissionsException();
    }

    @Override
    public void deleteOwner(int ownerId, ERole userRole) throws Exception {
        if (userRole == ERole.ADMIN)
            this.dao.deleteOwner(ownerId, userRole);
        throw new NotEnoughPermissionsException();
    }

    @Override
    public void updateOwner(Owner owner, ERole userRole) throws Exception {
        if (userRole == ERole.ADMIN)
            this.dao.updateOwner(owner, userRole);
        throw new NotEnoughPermissionsException();
    }

    @Override
    public User login(String login, String password) throws Exception {
        return this.dao.login(login, password);
    }

    @Override
    public User register(String login, String password, Role role) throws Exception {
        return this.dao.register(login, password, role);
    }

    @Override
    public List<Owner> searchOwner(OwnerSearchObject ownerSearchObject, ERole userRole) throws Exception {
        return this.dao.searchOwner(ownerSearchObject, userRole);
    }

    @Override
    public List<Pet> getOwnerPets(Owner owner, ERole userRole) throws Exception {
        return this.dao.getOwnerPets(owner, userRole);
    }

    @Override
    public int getKindIdByName(String name, ERole userRole) throws Exception {
        return this.dao.getKindIdByName(name, userRole);
    }

    @Override
    public void dropOwners(ERole userRole) throws Exception {
        if (userRole == ERole.ADMIN)
            this.dao.dropOwners(userRole);
        throw new NotEnoughPermissionsException();
    }

    @Override
    public List<Pet> getAllPets(ERole userRole) throws Exception {
        return this.dao.getAllPets(userRole);
    }

    @Override
    public List<City> getAllCities(ERole userRole) throws Exception {
        return this.dao.getAllCities(userRole);
    }

    @Override
    public List<Breed> getAllBreeds(ERole userRole) throws Exception {
        return this.dao.getAllBreeds(userRole);
    }

    @Override
    public List<Kind> getAllKinds(ERole userRole) throws Exception {
        return this.dao.getAllKinds(userRole);
    }
}

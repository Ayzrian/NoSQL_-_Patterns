package com.TtPP;

import com.TtPP.DAO.DAOFactory;
import com.TtPP.DAO.EDAOType;
import com.TtPP.DAO.IDAO;
import com.TtPP.builders.BreedBuilder;
import com.TtPP.builders.CityBuilder;
import com.TtPP.builders.KindBuilder;
import com.TtPP.builders.OwnerBuilder;
import com.TtPP.common.IEventListener;
import com.TtPP.entities.*;
import com.TtPP.render.ApplicationFrame;
import com.TtPP.render.PetComponent;
import com.TtPP.render.SimpleApplication;

import javax.swing.*;

import static com.TtPP.DAO.MySQLDAO.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main (String[] args) throws Exception {
        runMementoApplication();
    }


    /**
     * @relatesTo Lab 5-6, 3-4
     */
    public static void runMementoApplication () throws Exception {
        SimpleApplication.createAndShowGUI();
    }

    /**
     * This one is legacy version of lab 3-4.
     * @relatesTo Lab 3-4
     */
    public static void runObserverTestCases () throws Exception {
        DAOFactory factory = new DAOFactory();
        IDAO mysql = factory.createDao(EDAOType.MySQL);

        ApplicationFrame frame = new ApplicationFrame();

        IEventListener renderPets = (IDAO dao) -> {
            Graphics g = frame.getGraphics();
            Graphics2D graphics2D = (Graphics2D) g;

            Rectangle rectangle = new Rectangle();
            rectangle.setBounds(0, 0, frame.getWidth(), frame.getHeight());

            graphics2D.setColor(Color.WHITE);
            graphics2D.fill(rectangle);

            try {
                Point2D nextPoint = new Point(40, 40);
                for (Pet pet: dao.getAllPets()) {
                    PetComponent component = new PetComponent(pet, nextPoint);

                    component.paint(g);

                    nextPoint.setLocation(nextPoint.getX() + 50, nextPoint.getY() + 50);
                }

                System.out.println("Waiting 10 seconds.");
                Thread.sleep(10000);
            } catch (Exception e) {
                System.out.println(e);
            }

        };

        mysql.subscribe(NEW_PET, renderPets);
        mysql.subscribe(DELETE_PET, renderPets);
        mysql.subscribe(PET_CHANGE, renderPets);

        DogCreator dogCreator = new DogCreator(mysql);
        CatCreator catCreator = new CatCreator(mysql);

        Pet randomDog = dogCreator.create();
        randomDog.setName("Dog 4");
        Date date = new Date();
        date.setYear(2018);
        randomDog.setDateOfBirth(date);
        randomDog.setFkOwnerId(1);
        randomDog.setFkBreedId(2);

        Pet randomCat = catCreator.create();
        randomCat.setName("Cat ___");
        randomCat.setDateOfBirth(new Date());
        randomCat.setFkOwnerId(1);
        randomCat.setFkBreedId(1);

        randomDog.setPetId(mysql.createPet(randomDog));
        randomCat.setPetId(mysql.createPet(randomCat));


        date.setTime(2017);
        randomDog.setDateOfBirth(date);

        mysql.updatePet(randomDog);

        mysql.deletePet(randomDog);
    }
    /**`
     * @relatesTo Lab 1-2
     */
    public static void runSimpleTestCases () throws Exception {
        DAOFactory factory = new DAOFactory();
        IDAO mysql = factory.createDao(EDAOType.MySQL);

        KindBuilder kindBuilder = new KindBuilder();

        Kind cat = kindBuilder
                .withName("cat")
                .build();

        Kind dog = kindBuilder
                .withName("dog")
                .build();

        mysql.createKind(cat);
        mysql.createKind(dog);

        BreedBuilder breedBuilder = new BreedBuilder();

        Breed labrador = breedBuilder
                .withName("labrador")
                .build();

        Breed abyssinian = breedBuilder
                .withName("abyssinian")
                .build();

        labrador.setBreedId(mysql.createBreed(labrador));
        abyssinian.setBreedId(mysql.createBreed(abyssinian));

        CityBuilder cityBuilder = new CityBuilder();

        City kharkiv = cityBuilder.withName("Kharkiv").build();

        kharkiv.setCityId(mysql.createCity(kharkiv));

        System.out.println("================ Get Breeds =================");
        for (Breed dbBreed: mysql.getAllBreeds())
            System.out.println(dbBreed);

        System.out.println("================ Get Kinds =================");
        for (Kind dbKind: mysql.getAllKinds())
            System.out.println(dbKind);

        System.out.println("================ Get Cities =================");
        for (City dbCity: mysql.getAllCities())
            System.out.println(dbCity);

        OwnerBuilder ownerBuilder = new OwnerBuilder();

        Owner owner = ownerBuilder
                .withEmail("nazarii.romankiv@nure.ua")
                .withFirstName("Nazarii")
                .withLastName("Romankiv")
                .withPhoneNumber("132456789")
                .withFkCityId(kharkiv.getCityId())
                .build();

        owner.setOwnerId(mysql.createOwner(owner));

        System.out.println("================ Get Owners =================");
        for (Owner dbOwner: mysql.searchOwner(new OwnerSearchObject()))
            System.out.println(dbOwner);

        CatCreator catCreator = new CatCreator(mysql);

        Pet catPet = catCreator.create();
        catPet.setFkBreedId(abyssinian.getBreedId());
        catPet.setFkOwnerId(owner.getOwnerId());
        catPet.setDateOfBirth(new Date());
        catPet.setSex(true);
        catPet.setName("random cat");

        mysql.createPet(catPet);

        DogCreator dogCreator = new DogCreator(mysql);
        Pet dogPet = dogCreator.create();
        dogPet.setFkBreedId(labrador.getBreedId());
        dogPet.setFkOwnerId(owner.getOwnerId());
        dogPet.setDateOfBirth(new Date());
        catPet.setName("random dog");

        mysql.createPet(dogPet);


        System.out.println("============= Get Pets ===============");
        for (Pet dbPet: mysql.getAllPets())
            System.out.println(dbPet);
    }
}

package com.TtPP.render;

import com.TtPP.entities.Pet;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PetComponent extends Component {
    private Pet pet;
    private Font font;
    private Point2D formerPoint;


    public PetComponent (Pet pet, Point2D point2D) {
        this.pet = pet;
        this.font = new Font("Serif", Font.PLAIN, 16);
        this.formerPoint = point2D;
    }

    public void paint (Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;

        int age = new Date().getYear() - pet.getDateOfBirth().getYear() + 1;

        Ellipse2D elipse = new Ellipse2D.Double(formerPoint.getX(), formerPoint.getY(), age * 10, age * 10);

        switch (pet.getFkKindId()) {
            case -1 -> graphics2D.setColor(Color.BLACK);
            case 0 -> graphics2D.setColor(Color.BLUE);
            case 1 -> graphics2D.setColor(Color.ORANGE);
            case 2 -> graphics2D.setColor(Color.GREEN);
        }

        graphics2D.fill(elipse);

        graphics2D.setColor(Color.RED);
        graphics2D.setFont(font);
        graphics2D.drawString(pet.getName(), (float) (formerPoint.getX() + age * 5), (float) formerPoint.getY() + (age * 10 + 10));
    }
}

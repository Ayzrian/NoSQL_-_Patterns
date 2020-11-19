package com.TtPP.render;

import com.TtPP.DAO.DAOFactory;
import com.TtPP.DAO.EDAOType;
import com.TtPP.DAO.IDAO;
import com.TtPP.common.IEventListener;
import com.TtPP.entities.City;
import com.TtPP.memento.CitiesHistory;
import com.TtPP.memento.CityMemento;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static com.TtPP.DAO.MySQLDAO.*;

public class SimpleApplication extends JPanel {
    JPanel citiesPanel;
    JPanel addCityPanel;
    IDAO mysql;
    JFrame frame;
    CitiesHistory history = new CitiesHistory();

    public SimpleApplication (JFrame frame) throws Exception {
        super (new GridLayout(1, 0));
        this.frame = frame;

        DAOFactory factory = new DAOFactory();
        mysql = factory.createDao(EDAOType.MySQL);

        Border paneEdge = BorderFactory.createEmptyBorder(0, 10, 10, 10);

        citiesPanel = new JPanel();
        citiesPanel.setBorder(paneEdge);
        citiesPanel.setLayout(new BoxLayout(citiesPanel, BoxLayout.Y_AXIS));

        IEventListener renderCities = (IDAO mysql) -> {
            try {
                citiesPanel.removeAll();
                renderAllCities();
            } catch (Exception error) {
                System.out.println(error);
            }

        };

        mysql.subscribe(NEW_CITY, renderCities);
        mysql.subscribe(DELETE_CITY, renderCities);
        mysql.subscribe(CITY_CHANGE, renderCities);

        renderAllCities();

        addCityPanel = new JPanel();
        renderAddCityPanel();


        renderTabs();
    }

    private void renderAllCities () throws Exception {
        for (City city: mysql.getAllCities())
            renderCityComponent(citiesPanel, city);
    }


    private void renderCityComponent (JPanel target, City city) {
        JPanel cityComponent = new JPanel(new GridLayout(1, 4), false);
        JLabel cityName = new JLabel(city.getName(), JLabel.CENTER);

        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        JButton revertButton = new JButton("Revert");

        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                try {
                    mysql.deleteCity(city);
                    history.clearHistoryForCity(city.getCityId());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        updateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String newCityName =  (String) JOptionPane.showInputDialog(
                        frame,
                        "Enter new city name",
                        "Edit City",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        city.getName()
                );

                if (newCityName != null && newCityName.length() > 0) {
                    try {
                        history.rememberState(city.getCityId(), new CityMemento(city));
                        city.setName(newCityName);
                        mysql.updateCity(city);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        revertButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    City restoredCity = history.revert(city.getCityId());
                    if (restoredCity != null)
                        mysql.updateCity(restoredCity);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        cityComponent.add(cityName);
        cityComponent.add(revertButton);
        cityComponent.add(deleteButton);
        cityComponent.add(updateButton);
        cityComponent.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        target.add(Box.createRigidArea(new Dimension(0, 10)));
        target.add(cityComponent);
    }

    private void renderAddCityPanel () {
        addCityPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        addCityPanel.setLayout(new BoxLayout(addCityPanel, BoxLayout.Y_AXIS));

        TextField cityNameInput = new TextField();
        cityNameInput.setText("Enter city name!");

        Button create = new Button("Create");

        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                try {
                    mysql.createCity(new City(-1, cityNameInput.getText()));
                    cityNameInput.setText("");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        addCityPanel.add(cityNameInput);
        addCityPanel.add(create);


    }

    private void renderTabs () {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Cities", null, citiesPanel, "Use this panel to observe cities");
        tabbedPane.addTab("Add City", null, addCityPanel, "Use this panel to add city");


        tabbedPane.setSelectedIndex(0);

        add(tabbedPane);
    }


    public static void createAndShowGUI () throws Exception {
        JFrame frame = new JFrame("Simple Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SimpleApplication app = new SimpleApplication(frame);
        app.setOpaque(true);

        frame.setContentPane(app);

        frame.pack();
        frame.setVisible(true);
    }

}

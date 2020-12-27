package com.TtPP.render;

import com.TtPP.DAO.*;
import com.TtPP.common.IEventListener;
import com.TtPP.entities.City;
import com.TtPP.entities.Role;
import com.TtPP.entities.User;
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
    JPanel loginPanel;
    JPanel registerPanel;
    JPanel addCityPanel;
    JTabbedPane tabbedPane = null;
    IDAO mysql;
    JFrame frame;
    CitiesHistory history = new CitiesHistory();
    User currentUser = null;

    public SimpleApplication (JFrame frame) throws Exception {
        super (new GridLayout(1, 0));
        this.frame = frame;

        DAOFactory factory = new DAOFactory();
        mysql = new DAO_Proxy(factory.createDao(EDAOType.MySQL));

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
        renderAddCityPanel();
        renderLoginPanel();
        renderRegisterPanel();


        renderTabs();
    }

    private void renderAllCities () throws Exception {
        for (City city: mysql.getAllCities(ERole.ADMIN))
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
                    mysql.deleteCity(city, getUserRole());
                    history.clearHistoryForCity(city.getCityId());
                }
                catch (NotEnoughPermissionsException exception)
                {
                    JOptionPane.showMessageDialog(loginPanel, "У вас недостаточно привилегий для этого действия.", "", JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception exception) {
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
                        mysql.updateCity(city, getUserRole());
                    }
                    catch (NotEnoughPermissionsException exception)
                    {
                        JOptionPane.showMessageDialog(loginPanel, "У вас недостаточно привилегий для этого действия.", "", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (Exception exception) {
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
                        mysql.updateCity(restoredCity, getUserRole());
                }
                catch (NotEnoughPermissionsException exception)
                {
                    JOptionPane.showMessageDialog(loginPanel, "У вас недостаточно привилегий для этого действия.", "", JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception exception) {
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
        addCityPanel = new JPanel();

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
                    mysql.createCity(new City(-1, cityNameInput.getText()), getUserRole());
                    cityNameInput.setText("");
                }
                catch (NotEnoughPermissionsException exception)
                {
                    JOptionPane.showMessageDialog(loginPanel, "У вас недостаточно привилегий для этого действия.", "", JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        addCityPanel.add(cityNameInput);
        addCityPanel.add(create);
    }

    private void renderLoginPanel () {
        loginPanel = new JPanel();
        loginPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        TextField login = new TextField();
        login.setText("Enter login");

        TextField password = new TextField();
        password.setText("Enter password");

        Button create = new Button("Login");

        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                try {
                    currentUser = mysql.login(login.getText(), password.getText());

                    renderTabs();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(loginPanel, "Login error. Пользователь или не существует или неправильный login and password", "", JOptionPane.ERROR_MESSAGE);

                    exception.printStackTrace();
                }
            }
        });

        loginPanel.add(login);
        loginPanel.add(password);
        loginPanel.add(create);
    }

    private void renderRegisterPanel() {
        registerPanel = new JPanel();
        registerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));

        TextField login = new TextField();
        login.setText("Enter login");

        TextField password = new TextField();
        password.setText("Enter password");

        JComboBox<String> selectRole = new JComboBox<String>(new String[]{"ADMIN", "USER"});

        Button create = new Button("Register");

        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                try {
                    Role role = new Role();
                    role.setRoleId(selectRole.getSelectedIndex() + 1);

                    currentUser = mysql.register(login.getText(), password.getText(), role);

                    renderTabs();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(loginPanel, "Register error. Пользователь или существует или неправильный login and password", "", JOptionPane.ERROR_MESSAGE);

                    exception.printStackTrace();
                }
            }
        });

        registerPanel.add(login);
        registerPanel.add(password);
        registerPanel.add(selectRole);
        registerPanel.add(create);
    }

    private ERole getUserRole () {
        return currentUser.getFkRoleId() == 1 ? ERole.ADMIN : ERole.USER;
     }

    private void renderTabs () {
        if (tabbedPane != null)
            remove(tabbedPane);

        tabbedPane = new JTabbedPane();

        if (isLogined())
        {
            tabbedPane.addTab("Cities", null, citiesPanel, "Use this panel to observe cities");
            tabbedPane.addTab("Add City", null, addCityPanel, "Use this panel to add city");
        }
        else
        {
            tabbedPane.addTab("Login", null, loginPanel, "Login here");
            tabbedPane.addTab("Register", null, registerPanel, "Register here");
        }


        tabbedPane.setSelectedIndex(0);

        add(tabbedPane);

        revalidate();
    }

    private boolean isLogined () {
        return currentUser != null;
    }


    public static void createAndShowGUI () throws Exception {
        JFrame frame = new JFrame("Simple Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SimpleApplication app = new SimpleApplication(frame);
        app.setOpaque(true);

        frame.setContentPane(app);

        frame.setSize(200, 200);
        frame.pack();
        frame.setVisible(true);
    }

}

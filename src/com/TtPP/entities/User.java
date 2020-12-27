package com.TtPP.entities;

public class User {
    private int userId;
    private int fkRoleId;
    private String login;
    private String password;

    public int getFkRoleId() {
        return fkRoleId;
    }

    public void setFkRoleId(int fkRoleId) {
        this.fkRoleId = fkRoleId;
    }

    public int getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

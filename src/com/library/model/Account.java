package com.library.model;

public class Account {
    protected String username;
    protected String password;
    protected String role; // Admin, Staff, Reader

    public Account(String username, String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean login(String user, String pass){
        return this.username.equals(user) && this.password.equals(pass);
    }

    public String getRole(){
        return role;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}

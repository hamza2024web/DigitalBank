package com.hamza.digitalbank.domain;

import java.util.UUID;

public class User {
    private UUID id;
    private String fullname;
    private String email;
    private String adresse;
    private String password;

    public User (String fullname,String email,String adresse,String password){
        this.id = UUID.randomUUID();
        this.fullname = fullname;
        this.email = email;
        this.adresse = adresse;
        this.password = password;
    }

    public UUID getId(){
        return id;
    }

    public String getFullName(){
        return fullname;
    }

    public String getEmail(){
        return email;
    }

    public String getAdresse(){
        return adresse;
    }

    public String getPassword(){
        return password;
    }
    public void setFullName(String fullname){
        this.fullname = fullname;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public void setAdresse(String adresse){
        this.adresse = adresse;
    }

    public void setPassword(String password){
        this.password = password;
    }
}

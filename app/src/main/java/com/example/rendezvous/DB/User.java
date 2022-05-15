package com.example.rendezvous.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true) // come funziona ?
    @NonNull
    private Integer UID;

    @NonNull
    private String userName;

    @NonNull
    private String PSW;

    private String nome;

    private String cognome;

    @NonNull
    private boolean isActive;


//    Empty constructor to remove error Entities and POJOs must have a usable
//    public constructor. You can have an empty constructor or a constructor
//    whose parameters match the fields (by name and type).
    public User() {}

    public User(final String nome, final String cognome, final String userName, final String psw) {
        this.nome = nome;
        this.cognome = cognome;
        this.userName = userName;
        this.PSW = psw;
        this.isActive = false;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPSW() {
        return PSW;
    }

    public void setPSW(String PSW) {
        this.PSW = PSW;
    }

    public void setUID(Integer uid){
        this.UID = uid;
    }

    public Integer getUID(){
        return this.UID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

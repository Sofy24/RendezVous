package com.example.rendezvous.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    private int UID;

    @NonNull
    private String userName;

    @NonNull
    private String PSW;

    private String nome;

    private String cognome;


//    Empty constructor to remove error Entities and POJOs must have a usable
//    public constructor. You can have an empty constructor or a constructor
//    whose parameters match the fields (by name and type).
    public User() {}

    public User(final int uid, final String nome, final String cognome, final String userName, final String psw) {
        UID = uid;
        this.nome = nome;
        this.cognome = cognome;
        this.userName = userName;
        PSW = psw;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
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
}

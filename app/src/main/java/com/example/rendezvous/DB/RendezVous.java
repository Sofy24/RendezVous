package com.example.rendezvous.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RendezVous {
    @NonNull
    private String R_circleName;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer R_ID;


}

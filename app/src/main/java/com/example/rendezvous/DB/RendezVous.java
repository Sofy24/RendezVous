package com.example.rendezvous.DB;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.NO_ACTION;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class RendezVous {
    @NonNull
    private String R_circleName;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer R_ID;

    @NonNull
    private long R_DataI;

    @NonNull
    private long R_DataF;

    @NonNull
    private Integer R_infoID;

    @NonNull
    private Integer R_authorID;

    public RendezVous(){};

    public RendezVous(@NonNull String r_circleName, long r_dataI, long r_dataF, @NonNull Integer r_infoID, @NonNull Integer r_authorID) {
        R_circleName = r_circleName;
        R_DataI = r_dataI;
        R_DataF = r_dataF;
        R_infoID = r_infoID;
        R_authorID = r_authorID;
    }

    @NonNull
    public String getR_circleName() {
        return R_circleName;
    }

    public void setR_circleName(@NonNull String r_circleName) {
        R_circleName = r_circleName;
    }

    public long getR_DataI() {
        return R_DataI;
    }

    public void setR_DataI(long r_DataI) {
        R_DataI = r_DataI;
    }

    public Integer getR_ID(){
        return this.R_ID;
    }

    public void setR_ID(@NonNull Integer r_ID) {
        R_ID = r_ID;
    }

    public long getR_DataF() {
        return R_DataF;
    }

    public void setR_DataF(long r_DataF) {
        R_DataF = r_DataF;
    }

    @NonNull
    public Integer getR_infoID() {
        return R_infoID;
    }

    public void setR_infoID(@NonNull Integer r_infoID) {
        R_infoID = r_infoID;
    }

    @Override
    public String toString() {
        return "RendezVous{" +
                "R_circleName='" + R_circleName + '\'' +
                ", R_ID=" + R_ID +
                ", R_DataI=" + R_DataI +
                ", R_DataF=" + R_DataF +
                ", R_infoID=" + R_infoID +
                '}';
    }

    @NonNull
    public Integer getR_authorID() {
        return R_authorID;
    }

    public void setR_authorID(@NonNull Integer r_authorID) {
        R_authorID = r_authorID;
    }
}
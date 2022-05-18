package com.example.rendezvous.DB;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Circle.class,
                parentColumns = "C_name",
                childColumns = "R_circleName",
                onDelete = CASCADE)
})
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

    public RendezVous(){};

    public RendezVous(@NonNull String r_circleName, long r_dataI, long r_dataF, @NonNull Integer r_infoID) {
        R_circleName = r_circleName;
        R_DataI = r_dataI;
        R_DataF = r_dataF;
        R_infoID = r_infoID;
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
}
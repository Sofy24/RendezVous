package com.example.rendezvous.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"R_ID", "C_date"},
    foreignKeys = {
            @ForeignKey(entity = RendezVous.class,
                    parentColumns = "R_ID",
                    childColumns = "R_ID"),
            @ForeignKey(entity = User.class,
                    parentColumns = "UID",
                    childColumns = "C_attendant")
    }
)
public class ConfirmedRendezvous {
    @NonNull
    private Integer R_ID;

    @NonNull
    private long C_date;

    @NonNull
    private Integer C_attendant;

    public ConfirmedRendezvous() {}

    public ConfirmedRendezvous(@NonNull Integer r_id, long c_date, @NonNull Integer c_attendant) {
        R_ID = r_id;
        C_date = c_date;
        C_attendant = c_attendant;
    }

    @NonNull
    public Integer getR_ID() {
        return R_ID;
    }

    public void setR_ID(@NonNull Integer r_ID) {
        R_ID = r_ID;
    }

    public long getC_date() {
        return C_date;
    }

    public void setC_date(long c_date) {
        C_date = c_date;
    }

    @NonNull
    public Integer getC_attendant() {
        return C_attendant;
    }

    public void setC_attendant(@NonNull Integer c_attendant) {
        C_attendant = c_attendant;
    }
}

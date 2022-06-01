package com.example.rendezvous.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    foreignKeys = {
            @ForeignKey(entity = Info.class,
                    parentColumns = "I_ID",
                    childColumns = "CI_ID"),
            @ForeignKey(entity = User.class,
                    parentColumns = "UID",
                    childColumns = "C_attendant")
    }
)
public class ConfirmedRendezvous {
    @NonNull
    private Integer CI_ID;

    @NonNull
    private long C_date;

    @PrimaryKey
    @NonNull
    private Integer C_attendant;

    public ConfirmedRendezvous() {}

    public ConfirmedRendezvous(@NonNull Integer ci_id, long c_date, @NonNull Integer c_attendant) {
        CI_ID = ci_id;
        C_date = c_date;
        C_attendant = c_attendant;
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

    @NonNull
    public Integer getCI_ID() {
        return CI_ID;
    }

    public void setCI_ID(@NonNull Integer CI_ID) {
        this.CI_ID = CI_ID;
    }
}

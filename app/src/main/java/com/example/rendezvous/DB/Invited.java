package com.example.rendezvous.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"IR_ID", "IU_ID"}, foreignKeys = {
        @ForeignKey(entity = RendezVous.class,
                parentColumns = "R_ID",
                childColumns = "IR_ID"),
        @ForeignKey(entity = User.class,
                parentColumns = "UID",
                childColumns = "IU_ID")
})
public class Invited {
    @NonNull
    private Integer IR_ID;

    @NonNull
    private Integer IU_ID;

    @NonNull
    private String I_state;

    private long I_date;

    public Invited() {}

    public Invited(@NonNull Integer ir_id, @NonNull Integer u_id, @NonNull String i_state) {
        IR_ID = ir_id;
        IU_ID = u_id;
        I_state = i_state;
    }

    @NonNull
    public Integer getIR_ID() {
        return IR_ID;
    }

    public void setIR_ID(@NonNull Integer IR_ID) {
        this.IR_ID = IR_ID;
    }

    @NonNull
    public String getI_state() {
        return I_state;
    }

    public void setI_state(@NonNull String i_state) {
        I_state = i_state;
    }

    public long getI_date() {
        return I_date;
    }

    public void setI_date(long i_date) {
        I_date = i_date;
    }

    @NonNull
    public Integer getIU_ID() {
        return IU_ID;
    }

    public void setIU_ID(@NonNull Integer IU_ID) {
        this.IU_ID = IU_ID;
    }
}

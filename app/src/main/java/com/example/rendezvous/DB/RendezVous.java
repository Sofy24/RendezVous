package com.example.rendezvous.DB;

import androidx.room.Embedded;
import androidx.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"RID", "beginDate", "endDate"})
public class RendezVous {

    private final int RID;

    private final Date beginDate;

    private final Date endDate;

    @Embedded public Info info;

    public RendezVous(final int rid, final Date beginDate, final Date endDate) {
        RID = rid;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public int getRID() {
        return RID;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}

package com.example.rendezvous.DB;

import static androidx.room.ForeignKey.CASCADE;

import android.icu.number.IntegerWidth;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Relation;

import java.util.List;

@Entity(foreignKeys = {@ForeignKey(entity = Circle.class,
        parentColumns = "C_name",
        childColumns = "COF_C_name",
        onDelete = CASCADE), @ForeignKey(entity = User.class,
        parentColumns = "UID",
        childColumns = "COF_UID",
        onDelete = CASCADE)}, primaryKeys = { "COF_C_name", "COF_UID"})
public class CircleOfFriends {
//    @Embedded User user;
//
//    @Relation(
//            parentColumn = "UID",
//            entityColumn = "C_ID",
//            entity = User.class
//    )
//    public List<User> usersInACircle;

//    @NonNull
//    public User user;
//
//    @NonNull
//    public Circle circle;

    @NonNull
    private String COF_C_name;

    @NonNull
    private Integer COF_UID;

    public CircleOfFriends(){}

    public CircleOfFriends(@NonNull String COF_C_name, @NonNull Integer COF_UID){
        this.COF_UID = COF_UID;
        this.COF_C_name = COF_C_name;
    }

    @NonNull
    public Integer getCOF_UID() {
        return COF_UID;
    }

    public void setCOF_UID(@NonNull Integer COF_UID) {
        this.COF_UID = COF_UID;
    }

    @NonNull
    public String getCOF_C_name() {
        return COF_C_name;
    }

    public void setCOF_C_name(@NonNull String COF_C_name) {
        this.COF_C_name = COF_C_name;
    }
}

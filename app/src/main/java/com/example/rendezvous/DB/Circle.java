package com.example.rendezvous.DB;

import android.printservice.CustomPrinterIconCallback;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "circle", indices = {
        @Index(value = "C_ID"),
        @Index(value = "C_name", unique = true)
})
public class Circle {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer C_ID;

    @NonNull
    private String C_name;

    @NonNull
    private String C_color;


    public Circle(){}

    public Circle(@NonNull String c_name, @NonNull String c_color){
       this.C_name = c_name;
       this.C_color = c_color;
    }


    @NonNull
    public String getC_name() {
        return C_name;
    }

    public void setC_name(@NonNull String c_name) {
        C_name = c_name;
    }

    @NonNull
    public String getC_color() {
        return C_color;
    }

    public void setC_color(@NonNull String c_color) {
        C_color = c_color;
    }

    public Integer getC_ID() {
        return C_ID;
    }

    public void setC_ID(Integer c_ID) {
        C_ID = c_ID;
    }


}

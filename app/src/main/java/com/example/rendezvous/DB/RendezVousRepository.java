package com.example.rendezvous.DB;

import android.app.Application;

public class RendezVousRepository {
    private RendezVousDB rendezVousDB;

    private DatabaseDAO databaseDAO;

    public RendezVousRepository(Application application) {
        rendezVousDB = RendezVousDB.getInstance(application);
        databaseDAO =  rendezVousDB.databaseDAO();
    }

//    public void addUser(final String UID, final String username, final String psw){
//        this.rendezVousDB.executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                databaseDAO.insertUser(UID, username, psw);
//            }
//        });
//    }

//    public void addCircle(final String C_ID, final String C_name, final String C_color){
//        this.rendezVousDB.executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                databaseDAO.insertCircle(C_ID, C_name, C_color);
//            }
//        });
//    }
}

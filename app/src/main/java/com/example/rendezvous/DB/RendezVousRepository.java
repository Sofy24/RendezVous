package com.example.rendezvous.DB;

import android.app.Application;

public class RendezVousRepository {
    private RendezVousDB rendezVousDB;

    private DatabaseDAO databaseDAO;

    public RendezVousRepository(Application application) {
        rendezVousDB = RendezVousDB.getInstance(application);
        databaseDAO =  rendezVousDB.databaseDAO();
    }

    public void addUser(final int UID, final String username, final String psw){
        this.rendezVousDB.executor.execute(new Runnable() {
            @Override
            public void run() {
                databaseDAO.insertUser(UID, username, psw);
            }
        });
    }
}

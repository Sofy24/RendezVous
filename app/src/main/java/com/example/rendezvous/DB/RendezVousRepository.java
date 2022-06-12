package com.example.rendezvous.DB;

import android.app.Application;

public class RendezVousRepository {
    private final RendezVousDB rendezVousDB;

    private final DatabaseDAO databaseDAO;

    public RendezVousRepository(Application application) {
        rendezVousDB = RendezVousDB.getInstance(application);
        databaseDAO =  rendezVousDB.databaseDAO();
    }

}

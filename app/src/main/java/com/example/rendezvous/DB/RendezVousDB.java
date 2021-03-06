package com.example.rendezvous.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Circle.class, CircleOfFriends.class, RendezVous.class ,Info.class, Invited.class, ConfirmedRendezvous.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class RendezVousDB extends RoomDatabase {

    public abstract DatabaseDAO databaseDAO();

    private static volatile RendezVousDB INSTANCE;

    public static final int N_THREADS = 4;

    static final ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);

    public static synchronized RendezVousDB getInstance(final Context context) {
        if (INSTANCE == null) {

            synchronized (RendezVousDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RendezVousDB.class, "rendevouz_database")
                            //. .addTypeConverter() nel caso non funzioni
                            .build();

                }
            }

        }
        return INSTANCE;
    }
}

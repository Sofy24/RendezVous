package com.example.rendezvous.DB;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface DatabaseDAO {
    // Insert User
    @Query("INSERT INTO User VALUES(:UID, :username, :psw, null, null)")
    public void insertUser(int UID, String username, String psw);

}

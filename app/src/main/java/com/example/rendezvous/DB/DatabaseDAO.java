package com.example.rendezvous.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface DatabaseDAO {
    // Insert User
//    @Query("INSERT INTO User VALUES(:UID, :username, :psw, null, null)")
//    public void insertUser(String UID, String username, String psw);

    @Insert
    void insertUser(User ...user);

//    @Query("INSERT INTO Circle VALUES(:C_ID, :C_name, :C_color)")
//    public void insertCircle(String C_ID, String C_name, String C_color);

    @Insert
    void insertCircle(Circle ...circle);

    @Query("INSERT INTO CircleOfFriends VALUES(:gruopName, :user_ID)")
    void insertCircleOfFriends(String gruopName, Integer user_ID);

    @Query("SELECT UID FROM User WHERE userName = :userName")
    Integer getUID(String userName);

    @Query("UPDATE User SET isActive = 1 where UID = :id")
    void setUserActive(Integer id);

    @Query("SELECT * from User where isActive = 1")
    User getActiveUser();

    @Query("UPDATE User SET isActive = 0 where UID = :id")
    void setUserLoggedOut(Integer id);

    @Insert
    void insertRendezvous(RendezVous ...rendezVous);
    //Drop database
//    @Query("DELETE TABLE User")
//    public void nukeTables();

}

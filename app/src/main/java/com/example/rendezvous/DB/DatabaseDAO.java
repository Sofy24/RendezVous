package com.example.rendezvous.DB;

import android.net.Uri;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DatabaseDAO {
    // Insert User
//    @Query("INSERT INTO User VALUES(:UID, :username, :psw, null, null)")
//    public void insertUser(String UID, String username, String psw);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User ...user);

//    @Query("INSERT INTO Circle VALUES(:C_ID, :C_name, :C_color)")
//    public void insertCircle(String C_ID, String C_name, String C_color);


    //@Query("INSERT INTO Info VALUES(:I_title, :I_description, :I_imageURL)")
    //void insertInfo(String I_title, String I_description, String I_imageURL);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertInfo(Info ...info);

    //@Query("")

    @Insert(onConflict =  OnConflictStrategy.IGNORE)
    void insertCircle(Circle ...circle);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCircleOfFriends(CircleOfFriends ...circleOfFriends);

    @Query("DELETE from CircleOfFriends where COF_UID = :id and COF_C_name = :groupName")
    void removeFromCircle(Integer id, String groupName);

    @Query("INSERT INTO CircleOfFriends VALUES(:gruopName, :user_ID)")
    void insertCircleOfFriends(String gruopName, Integer user_ID);

    @Query("SELECT UID FROM User WHERE userName = :userName")
    Integer getUID(String userName);

    @Query("UPDATE User SET isActive = 1 where UID = :id")
    void setUserActive(Integer id);

    @Query("SELECT * from User where isActive = 1")
    User getActiveUser();

    @Query("UPDATE User SET isActive = 0")
    void setUserLoggedOut();

    @Query("UPDATE User set nome = :name, cognome = :surname where UID = :id")
    void updateUser(Integer id, String name, String surname); //, String image);

    @Query("UPDATE User set URIavatar = :uri where userName = :username")
    void updateUserAvatar(String uri, String username);

    @Query("SELECT * from circle")
    List<Circle> getCircles();


    @Query("SELECT C_name from circle join circleoffriends on (C_name = COF_C_name) where COF_UID = :id")
    List<String> getUserCircles(Integer id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRendezvous(RendezVous ...rendezVous);
    //Drop database
//    @Query("DELETE TABLE User")
//    public void nukeTables();

}

package com.example.rendezvous.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface DatabaseDAO {
    // Insert User
//    @Query("INSERT INTO User VALUES(:UID, :username, :psw, null, null)")
//    public void insertUser(String UID, String username, String psw);

    @Insert
    void insertUser(User ...user);

//    @Query("INSERT INTO Circle VALUES(:C_ID, :C_name, :C_color)")
//    public void insertCircle(String C_ID, String C_name, String C_color);


    //@Query("INSERT INTO Info VALUES(:I_title, :I_description, :I_imageURL)")
    //void insertInfo(String I_title, String I_description, String I_imageURL);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertInfo(Info ...info);

    //@Query("")

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

    @Query("UPDATE User set nome = :name, cognome = :surname where UID = :id")
    void updateUser(Integer id, String name, String surname); //, String image);
    @Insert
    void insertRendezvous(RendezVous ...rendezVous);
    //Drop database
//    @Query("DELETE TABLE User")
//    public void nukeTables();

}

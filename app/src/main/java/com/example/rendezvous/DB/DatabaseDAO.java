package com.example.rendezvous.DB;

import androidx.core.util.Pair;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
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



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertInfo(Info ...info);

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

    @Query("SELECT u.* FROM circleoffriends join User u on (COF_UID = u.UID) where COF_C_name = :circleName")
    List<User> getUsersInCircle(String circleName);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRendezvous(RendezVous ...rendezVous);

    @Query("SELECT DISTINCT o.* FROM RendezVous join Info o on (R_infoID = I_ID) join CircleOfFriends on (R_circleName = COF_C_name) join User on (COF_UID = UID) where isActive = 1 ;")
    List<Info> getListCardsForActiveUser();

    @Query("SELECT I_ID from Info where title = :title")
    Integer getInfoID(String title);

    @Query("SELECT * from Info where title = :title")
    Info getInfo(String title);

    @Query("SELECT * from RendezVous where R_DataI = :firstDay and R_DataF = :endDay and R_infoID = :info_id;")
//    @Query("SELECT R_circleName,R_ID from RendezVous where R_DataI = :firstDay and R_DataF = :endDay and R_infoID = :info_id;")
    List<RendezVous> getRendezVous(long firstDay, long endDay, Integer info_id);

    @Query("SELECT * from RendezVous where R_infoID = :info_id;")
    RendezVous getRendezVousFromInfo(Integer info_id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertInvited(Invited ...inviteds);

    @Query("UPDATE invited SET I_date = :dateToTimestamp, I_state = \"partecipa\" where IU_ID = (select UID from User where isActive = 1)")
    void updateInvited(Long dateToTimestamp);

    @Query("UPDATE invited SET I_state = \"Busy\" where IU_ID = (select UID from User where isActive = 1)")
    void setBusy();

    @Query("SELECT I_state from invited where IU_ID = (select UID from User where isActive = 1)")
    String getInvitedState();

    @Query("SELECT COUNT(*)" +
            "FROM invited " +
            "WHERE IR_ID in (SELECT R_ID FROM rendezVous where R_infoID = :infoID)")
    int getTotalNumOfPartecipants(Integer infoID);

    @Query("SELECT COUNT(*)" +
            "FROM invited " +
            "WHERE I_state != \"Received\"" +
            "and IR_ID in (SELECT R_ID FROM rendezVous where R_infoID = :infoID)")
    int getNumOfPartecipants(Integer infoID);

// Drop database
//    @Query("DELETE TABLE User")
//    public void nukeTables();

}

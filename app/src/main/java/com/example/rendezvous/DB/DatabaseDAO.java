package com.example.rendezvous.DB;

import androidx.core.util.Pair;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Query("select * from User where UID = :uid")
    User getUser(Integer uid);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    //@Update
    void insertRendezvous(RendezVous ...rendezVous);

    //TODO modificare questa e confirmedRendezvous per il foreign constraint di cacca
    // SELECT DISTINCT o.* FROM RendezVous join Info o on (R_infoID = I_ID) join CircleOfFriends on (R_circleName = COF_C_name) join User on (COF_UID = UID) where isActive = 1 and o.I_ID not in (select * from ConfirmedRendezvous where C_infoID = o.I_ID)
    @Query("SELECT DISTINCT o.* FROM RendezVous join Info o on (R_infoID = I_ID) join CircleOfFriends on (R_circleName = COF_C_name) join User on (COF_UID = UID) where isActive = 1 and o.I_ID not in (select C_infoID from ConfirmedRendezvous where C_infoID = o.I_ID)")
    List<Info> getListCardsForActiveUser();

    @Query("SELECT I_ID from Info where title = :title")
    Integer getInfoID(String title);

    @Query("SELECT * from Info where title = :title")
    Info getInfo(String title);

    @Query("SELECT * from RendezVous where R_DataI = :firstDay and R_DataF = :endDay and R_infoID = :info_id;")
//    @Query("SELECT R_circleName,R_ID from RendezVous where R_DataI = :firstDay and R_DataF = :endDay and R_infoID = :info_id;")
    List<RendezVous> getRendezVous(long firstDay, long endDay, Integer info_id);

    @Query("SELECT * from RendezVous where R_infoID = :info_id;")
    List<RendezVous> getRendezVousFromInfo(Integer info_id);

//    @Query("DELETE from RendezVous where R_infoID = :RID")
//void deleteRV(Integer RID);
    @Delete
    void deleteRV(RendezVous rendezVous);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertInvited(Invited ...inviteds);

    @Query("UPDATE invited SET I_date = :dateToTimestamp, I_state = \"partecipa\" where IU_ID = (select UID from User where isActive = 1)")
    void updateInvited(Long dateToTimestamp);

    @Query("UPDATE invited SET I_state = \"Busy\" where IU_ID = (select UID from User where isActive = 1)")
    void setBusy();

    @Query("SELECT I_state from invited where IU_ID = (select UID from User where isActive = 1) and IR_ID = :IR_ID")
    String getInvitedState(Integer IR_ID);


    @Query("SELECT * from invited join User on (IU_ID = UID) WHERE IR_ID in (SELECT R_ID FROM rendezVous where R_infoID = :infoID)")
    List<Invited> getInvited(Integer infoID);

    @Query("SELECT * FROM User where UID = :id")
    User getGuestNameSurname(Integer id);

    @Query("SELECT COUNT(*)" +
            "FROM invited " +
            "WHERE IR_ID in (SELECT R_ID FROM rendezVous where R_infoID = :infoID)")
    int getTotalNumOfPartecipants(Integer infoID);

    @Query("SELECT COUNT(*)" +
            "FROM invited " +
            "WHERE I_state != \"Received\"" +
            "and IR_ID in (SELECT R_ID FROM rendezVous where R_infoID = :infoID)")
    int getNumOfPartecipants(Integer infoID);


    @Query("SELECT IU_ID FROM invited WHERE IR_ID in (SELECT R_ID FROM rendezVous where R_infoID = :i_id) and I_state = \"partecipa\" ")
    List<Integer> getPartecipantsId(Integer i_id);

    @Query("SELECT I_date " +
            "FROM invited " +
            "WHERE IR_ID in (SELECT R_ID FROM rendezVous where R_infoID = :i_id) " +
            "group by I_date " +
            " order by count(*) DESC " +
            "limit 1")
    long getConfirmedDate(Integer i_id);

    @Insert
    void insertConfirmedRendezvous(ConfirmedRendezvous ...confirmedRendezvous);

    @Query("SELECT * from RendezVous where R_infoID = :i_id;")
    RendezVous getSingleRendezVousFromInfo(Integer i_id);
// Drop database
//    @Query("DELETE TABLE User")
//    public void nukeTables();

}

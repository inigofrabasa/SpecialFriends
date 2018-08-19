package mx.inigofr.specialfriends.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.List;
import mx.inigofr.specialfriends.utilities.DateConverter;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by inigo on 08/08/18.
 */

@Dao
@TypeConverters(DateConverter.class)
public interface UserModelDao {
    @Query("select * from UserModel")
    LiveData<List<UserModel>> getAllUserItems();

    @Query("select * from UserModel where lower(_personName) LIKE lower(:request)")
    LiveData<List<UserModel>> getUserItemsByRequest(String request);

    @Query("select * from UserModel where _id = :id")
    UserModel getUserbyId(String id);

    @Insert(onConflict = REPLACE)
    void addUser(UserModel userModel);

    @Delete
    void deleteUser(UserModel userModel);

    @Update
    void updateUser(UserModel userModel);
}

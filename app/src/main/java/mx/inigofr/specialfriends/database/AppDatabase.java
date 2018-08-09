package mx.inigofr.specialfriends.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import mx.inigofr.specialfriends.model.UserModel;
import mx.inigofr.specialfriends.model.UserModelDao;

/**
 * Created by inigo on 08/08/18.
 */

@Database(entities = {UserModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "special_friends_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract UserModelDao itemAndPersonModel();
}

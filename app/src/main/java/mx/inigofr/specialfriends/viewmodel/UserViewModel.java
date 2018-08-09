package mx.inigofr.specialfriends.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutionException;

import mx.inigofr.specialfriends.database.AppDatabase;
import mx.inigofr.specialfriends.database.DataBaseHandler;
import mx.inigofr.specialfriends.model.UserModel;

/**
 * Created by inigo on 09/08/18.
 */

public class UserViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
    private ResultUserById resultUserById;
    private UserModel person;

    public UserViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
    }

    public void setOnGetUserById(ResultUserById resultUserById, String id){
        this.resultUserById = resultUserById;

        try {
            person = new DataBaseHandler.getUserAsyncTask(appDatabase).execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            resultUserById.onGetUserById(null);
        } catch (ExecutionException e) {
            resultUserById.onGetUserById(null);
        }
        resultUserById.onGetUserById(person);
    }

    public interface ResultUserById{
        void onGetUserById(UserModel userModel);
    }

    public void updateUser(final UserModel userModel) {
        new DataBaseHandler.updateAsyncTask(appDatabase).execute(userModel);
    }
}

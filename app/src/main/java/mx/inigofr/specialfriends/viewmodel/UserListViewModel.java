package mx.inigofr.specialfriends.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import mx.inigofr.specialfriends.database.AppDatabase;
import mx.inigofr.specialfriends.database.DataBaseHandler;
import mx.inigofr.specialfriends.model.UserModel;
import mx.inigofr.specialfriends.utilities.DownloadImage;

/**
 * Created by inigo on 08/08/18.
 */

public class UserListViewModel extends AndroidViewModel {

    private final String linkimage_chunk_1 = "https://graph.facebook.com/";
    private final String linkimage_chunk_2 = "/picture?type=large";

    private final LiveData<List<UserModel>> itemAndPersonList;

    private AppDatabase appDatabase;

    private LoadFromFacebook loadFromFacebook;

    public UserListViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        itemAndPersonList = appDatabase.itemAndPersonModel().getAllUserItems();
        getFriendList();
    }

    public LiveData<List<UserModel>> getItemAndPersonList() {
        return itemAndPersonList;
    }

    public void deleteUser(UserModel userModel) {
        new DataBaseHandler.deleteAsyncTask(appDatabase).execute(userModel);
    }

    public void addUser(final UserModel userModel) {
        new DataBaseHandler.addAsyncTask(appDatabase).execute(userModel);
    }

    public void updateUser(final UserModel userModel) {
        new DataBaseHandler.updateAsyncTask(appDatabase).execute(userModel);
    }

    public void getFriendList(){
        GraphRequestBatch batch = new GraphRequestBatch(
                GraphRequest.newMyFriendsRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(
                                    JSONArray jsonArray,
                                    GraphResponse response) {
                                if(jsonArray != null){
                                    try {
                                        for(int i=0; i < jsonArray.length();i++){
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            if(object != null){

                                                String id = object.getString("id");
                                                String name = object.getString("name");
                                                URL url = new URL(linkimage_chunk_1+id+linkimage_chunk_2);

                                                UserModel usermodel = new UserModel();
                                                usermodel.set_id(id);
                                                usermodel.set_personName(name);

                                                Bitmap image = new DownloadImage().execute(url.toString()).get();
                                                if(image != null){
                                                    usermodel.set_imgURL(url.toString());
                                                }

                                                addUser(usermodel);
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }

                                    loadFromFacebook.loadFromFacebookListener(true);
                                }
                            }
                        })
        );

        batch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch graphRequests) {
                // Application code for when the batch finishes
            }
        });
        batch.executeAsync();
    }

    public void setOnLoadFromFacebook(LoadFromFacebook loadFromFacebook){
        this.loadFromFacebook = loadFromFacebook;
    }

    public interface LoadFromFacebook{
        void loadFromFacebookListener(boolean result);
    }
}

package mx.inigofr.specialfriends.database;

import android.os.AsyncTask;

import mx.inigofr.specialfriends.model.UserModel;

/**
 * Created by inigo on 08/08/18.
 */

public class DataBaseHandler {
    public static class addAsyncTask extends AsyncTask<UserModel, Void, Void> {

        private AppDatabase db;

        public addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final UserModel... params) {
            db.itemAndPersonModel().addUser(params[0]);
            return null;
        }
    }

    public static class deleteAsyncTask extends AsyncTask<UserModel, Void, Void> {

        private AppDatabase db;

        public deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final UserModel... params) {
            db.itemAndPersonModel().deleteUser(params[0]);
            return null;
        }

    }

    public static class updateAsyncTask extends AsyncTask<UserModel, Void, Void> {

        private AppDatabase db;

        public updateAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final UserModel... params) {
            db.itemAndPersonModel().updateUser(params[0]);
            return null;
        }

    }

    public static class getUserAsyncTask extends AsyncTask<String, Void, UserModel> {

        private AppDatabase db;

        public getUserAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected UserModel doInBackground(final String... params) {
            try{
            return db.itemAndPersonModel().getUserbyId(params[0]);
            } catch (NullPointerException ex){ }
            return null;
        }

    }
}

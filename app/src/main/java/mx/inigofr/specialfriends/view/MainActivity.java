package mx.inigofr.specialfriends.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import mx.inigofr.specialfriends.R;
import mx.inigofr.specialfriends.adapter.FavoritesAdapter;
import mx.inigofr.specialfriends.adapter.UsersAdapter;
import mx.inigofr.specialfriends.model.UserModel;
import mx.inigofr.specialfriends.viewmodel.UserListViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private UserListViewModel viewModel;
    private UsersAdapter usersAdapter;
    private FavoritesAdapter favoritesAdapter;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.indeterminateBar);

        progressBar.setVisibility(View.VISIBLE);
        viewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        viewModel.getItemAndPersonList().observe(MainActivity.this, new Observer<List<UserModel>>() {
            @Override
            public void onChanged(@Nullable List<UserModel> itemAndPeople) {

                if(itemAndPeople.size() == 0){
                    viewModel.getFriendList();
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerViewFavorites.setVisibility(View.VISIBLE);
                }

                usersAdapter.addItems(itemAndPeople);
                favoritesAdapter.addItems(itemAndPeople);
            }
        });

        viewModel.setOnLoadFromFacebook(new UserListViewModel.LoadFromFacebook() {
            @Override
            public void loadFromFacebookListener(boolean result) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewFavorites.setVisibility(View.VISIBLE);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewFavorites = (RecyclerView) findViewById(R.id.recyclerViewFavorites);

        recyclerView.setVisibility(View.GONE);
        recyclerViewFavorites.setVisibility(View.GONE);

        usersAdapter = new UsersAdapter(new ArrayList<UserModel>(), this, this);
        favoritesAdapter = new FavoritesAdapter(new ArrayList<UserModel>(), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(usersAdapter);

        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFavorites.setAdapter(favoritesAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getClass().equals(AppCompatImageView.class)){
            UserModel model = (UserModel)v.getTag();
            model.set_isFavorite(!model.get_isFavorite());
            viewModel.updateUser(model);
        } else {
            Intent intent = new Intent(this, UserActivity.class);
            UserModel model = (UserModel)v.getTag();
            intent.putExtra("user_id", model._id);
            startActivity(intent);
        }
    }

    public void onSearch(View view) {

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

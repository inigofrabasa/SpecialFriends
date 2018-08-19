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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private TextView userNameHeader;
    private TextView textToSearch;
    private ImageView deleteSearch;

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewFavorites;
    private Observer<List<UserModel>> mainObserver;
    private Observer<List<UserModel>> searchObserver;
    private String request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.indeterminateBar);

        showToolbar("", false);
        userNameHeader = (TextView) findViewById(R.id.tv_name_user);
        userNameHeader.setText(getIntent().getStringExtra("name_user"));

        progressBar.setVisibility(View.VISIBLE);
        viewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        viewModel.getItemAndPersonList().observe(MainActivity.this, mainObserver = new Observer<List<UserModel>>() {
            @Override
            public void onChanged(@Nullable List<UserModel> itemAndPeople) {
                if(itemAndPeople == null)
                    return;

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

        textToSearch = (TextView)findViewById(R.id.et_searech_term);
        deleteSearch = (ImageView)findViewById(R.id.delete_search_button);
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

        if(textToSearch.getText() != null)
            searchUser(textToSearch.getText().toString());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar)findViewById(R.id.w_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public void searchUser(String request){

        this.request = request;
        if(request.equals(""))
            deleteSearch.setVisibility(View.INVISIBLE);
        else
            deleteSearch.setVisibility(View.VISIBLE);

        viewModel.getItemAndPersonList().removeObservers(MainActivity.this);
        viewModel.getItemAndPersonListBySearch(this.request).observe(MainActivity.this, searchObserver = new Observer<List<UserModel>>() {
            @Override
            public void onChanged(@Nullable List<UserModel> itemAndPeople) {
                if(itemAndPeople == null)
                    return;

                progressBar.setVisibility(View.GONE);

                usersAdapter.addItems(itemAndPeople);
                favoritesAdapter.addItems(itemAndPeople);
            }
        });
    }

    public void onDeleteSearch(View view) {

        textToSearch.setText("");
        deleteSearch.setVisibility(View.INVISIBLE);

        viewModel.getItemAndPersonList().removeObservers(MainActivity.this);
        viewModel.getAllPeopleDB();
        viewModel.getItemAndPersonList().observe(MainActivity.this, mainObserver = new Observer<List<UserModel>>() {
            @Override
            public void onChanged(@Nullable List<UserModel> itemAndPeople) {
                if(itemAndPeople == null)
                    return;

                progressBar.setVisibility(View.GONE);

                usersAdapter.addItems(itemAndPeople);
                favoritesAdapter.addItems(itemAndPeople);
            }
        });
    }
}

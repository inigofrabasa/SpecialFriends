package mx.inigofr.specialfriends.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mx.inigofr.specialfriends.R;
import mx.inigofr.specialfriends.model.UserModel;
import mx.inigofr.specialfriends.viewmodel.UserViewModel;

public class UserActivity extends AppCompatActivity {

    Button userSave;

    TextView userName;
    EditText userBirthday;
    EditText userPhone;
    EditText userNotes;
    ImageView favoriteButton;

    private UserViewModel viewModel;
    private UserModel _userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userName = (TextView) findViewById(R.id.tv_username);

        userBirthday = (EditText)findViewById(R.id.et_birthday);
        userPhone = (EditText)findViewById(R.id.et_phonenumber);
        userNotes = (EditText)findViewById(R.id.et_notes);

        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        viewModel.setOnGetUserById(new UserViewModel.ResultUserById() {
            @Override
            public void onGetUserById(UserModel userModel) {
                if(userModel != null) {
                    _userModel = userModel;
                    userName.setText(userModel.get_personName());

                    showToolbar("", true);

                    try{userBirthday.setText(userModel.get_birthday().toString() != null ? userModel.get_birthday().toString() : "");} catch(NullPointerException ex){}
                    try{userPhone.setText(userModel.get_phoneNumber().toString() != null ? userModel.get_phoneNumber().toString() : "");} catch(NullPointerException ex){}
                    try{userNotes.setText(userModel.get_Notes().toString() != null ? userModel.get_Notes().toString() : "");} catch(NullPointerException ex){}
                }
            }
        }, getIntent().getStringExtra("user_id"));

        userSave = (Button)findViewById(R.id.bt_user_save);
        userSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_userModel != null){
                    String phone = userPhone.getText().toString();
                    if(!phone.equals(""))
                        _userModel.set_phoneNumber(phone);

                    String notes = userNotes.getText().toString();
                    if(!notes.equals(""))
                        _userModel.set_Notes(notes);

                    String birthday = userBirthday.getText().toString();
                    if(!birthday.equals(""))
                        _userModel.set_birthday(birthday);

                    viewModel.updateUser(_userModel);

                    Toast.makeText(UserActivity.this, "Friend saved!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar)findViewById(R.id.w_toolbar_user);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

        favoriteButton = findViewById(R.id.iv_image_favorite);
        favoriteButton.setVisibility(View.VISIBLE);

        if(_userModel.get_isFavorite()) {
            favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_white));
        } else {
            favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_white));
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _userModel.set_isFavorite(!_userModel.get_isFavorite());
                viewModel.updateUser(_userModel);

                if(_userModel.get_isFavorite()) {
                    favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_white));
                    Toast.makeText(UserActivity.this, "added to Favorites", Toast.LENGTH_LONG).show();
                } else {
                    favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_white));
                    Toast.makeText(UserActivity.this, "deleted from Favorites", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
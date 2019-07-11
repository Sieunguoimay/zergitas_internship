package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.LoginActivityMVP;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.LoginActivityPresenter;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.LoginModel;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.R;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.UserRepository;

public class LoginActivity extends AppCompatActivity implements LoginActivityMVP.View {

    LoginActivityMVP.Presenter presenter;
    private EditText etFirstName;
    private EditText etLastName;
    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginActivityPresenter(new LoginModel(new UserRepository(this)));

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        btLogin = findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(presenter.loginButtonClicked()){
                    Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected  void onResume(){
        super.onResume();
        presenter.setView(this);
        presenter.getCurrentUser();
    }

    @Override
    public String getFirstName() {
        return etFirstName.getText().toString();
    }

    @Override
    public String getLastName() {
        return etLastName.getText().toString();
    }

    @Override
    public void showInputError() {
        Toast.makeText(this,"First name or Last name cannot be empty", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFirstName(String firstName) {
        this.etFirstName.setText(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        this.etLastName.setText(lastName);
    }

    @Override
    public void showUserSavedMessage() {
        Toast.makeText(this,"User saved successfully", Toast.LENGTH_SHORT).show();
    }
}

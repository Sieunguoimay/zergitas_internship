package com.sieunguoimay.vuduydu.mvp_pattern_loginapp;

import android.content.Intent;
import android.support.annotation.Nullable;

public class LoginActivityPresenter implements LoginActivityMVP.Presenter {
    @Nullable
    private LoginActivityMVP.View view;
    private LoginActivityMVP.Model model;

    public LoginActivityPresenter(LoginActivityMVP.Model model){
        this.model = model;
    }

    @Override
    public void setView(LoginActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public boolean loginButtonClicked() {
        if(view!=null){
            if(view.getFirstName().trim().equals("")||view.getLastName().trim().equals("")){
                view.showInputError();
                return false;
            }
            else{
                model.createUser(view.getFirstName(),view.getLastName());
                view.showUserSavedMessage();
                return true;
            }
        }
        return false;
    }

    @Override
    public void getCurrentUser() {
        User user = model.getUser();
        if(user!=null){
            if(view != null){
                view.setFirstName(user.getFirstName());
                view.setLastName(user.getLastName());
            }
        }
    }
}

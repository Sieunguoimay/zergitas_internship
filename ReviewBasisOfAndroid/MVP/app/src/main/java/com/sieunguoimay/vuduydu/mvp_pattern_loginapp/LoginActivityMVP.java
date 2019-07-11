package com.sieunguoimay.vuduydu.mvp_pattern_loginapp;

public interface LoginActivityMVP {

    interface View{
        String getFirstName();
        String getLastName();
        void showInputError();
        void setFirstName(String firstName);
        void setLastName(String lastName);
        void showUserSavedMessage();
    }
    interface Presenter{
        void setView(View view);
        boolean loginButtonClicked();
        void getCurrentUser();
    }
    interface Model{
        void createUser(String name, String lastName);
        User getUser();
    }
}

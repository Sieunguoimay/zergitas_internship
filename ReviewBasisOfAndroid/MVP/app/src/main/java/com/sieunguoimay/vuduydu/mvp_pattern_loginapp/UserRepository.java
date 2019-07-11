package com.sieunguoimay.vuduydu.mvp_pattern_loginapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class UserRepository implements LoginRepository {
    //private User user;
    private Context context;
    public UserRepository(Context context){
        this.context = context;
    }
    @Override
    public User getUser() {

//        if(user == null){
//            User user= new User("Vu Duy","Du");
//            user.setId(0);
//            return user;
//        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String  firstName = sharedPreferences.getString("first_name", "Vu Duy") ;
        String  lastName = sharedPreferences.getString("last_name", "Du") ;

        Toast.makeText(context,firstName, Toast.LENGTH_LONG).show();

        return new User(firstName,lastName);
    }

    @Override
    public void saveUser(User user) {
        if(user == null){
            user = getUser();
        }
        //this.user = user;

        //save to shared preferences is good
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("first_name",user.getFirstName());
        editor.putString("last_name",user.getLastName());
        editor.commit();

    }
}

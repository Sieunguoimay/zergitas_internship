package com.sieunguoimay.vuduydu.mvp_pattern_loginapp;

public interface LoginRepository {
    User getUser();
    void saveUser(User user);
}

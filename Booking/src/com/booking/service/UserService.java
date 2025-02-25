package com.booking.service;

import com.booking.dto.User;

public interface UserService {
    User login(String ID, String passwd);
    boolean checkIDDuplicate(String ID);
    void register();
    void changeUserName(String ID);
    void changeUserEmail(String ID);
    void changeUserPW(String ID);
    void deleteUser(String ID);
    void checkUserGrade(String ID);
    void chargeAccount(String ID, int money);
}
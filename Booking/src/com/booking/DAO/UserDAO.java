package com.booking.DAO;

import java.util.ArrayList;
import java.util.List;

import com.booking.dto.Coupon;
import com.booking.dto.User;

public interface UserDAO {
    User login(String ID, String passwd);
    boolean checkIDDuplicate(String ID);
    boolean register(String ID, String passwd, String name, String email);
    boolean changeUserName(String ID, String name);
    boolean changeUserEmail(String ID, String email);
    boolean changeUserPW(String ID, String passwd);
    boolean deleteUser(String ID);
    String checkUserGrade(String ID);
    boolean chargeAccount(String ID, int money);
	List<List<String>> getCouponList(String iD);
}

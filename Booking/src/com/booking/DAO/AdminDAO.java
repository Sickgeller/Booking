package com.booking.DAO;

import com.booking.dto.Admin;

public interface AdminDAO {
	Admin adminLogin(String ID, String passwd);
}		

package com.booking.service.impl;

import com.booking.DAO.AdminDAO;
import com.booking.DAO.impl.AdminDAOImpl;
import com.booking.dto.Admin;
import com.booking.service.AdminService;

public class AdminServiceImpl implements AdminService{
	private AdminDAO adminDAO;
	
	public AdminServiceImpl(){
		adminDAO = new AdminDAOImpl();
	}

	@Override
	public Admin login(String id, String passwd) {
		return adminDAO.adminLogin(id, passwd);
	}


}

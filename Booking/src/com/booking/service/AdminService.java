package com.booking.service;

import com.booking.dto.Admin;

public interface AdminService {
	Admin login(String id, String passwd);
}

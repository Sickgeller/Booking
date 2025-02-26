package com.booking.service;

import com.booking.dto.Admin;

public interface QnAService {

	void answerToQNA(Admin admin);
	void updateQNA(Admin admin);
	void showAllQNA();
	void questionQNA();
	void checkMyQNA();
	void updateMyQNA();
	
	
}

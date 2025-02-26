package com.booking.DAO;

import java.util.List;

import com.booking.dto.Accommodation;
import com.booking.dto.Admin;

public interface AccoMgmtDAO {
	
	boolean accommodation_resume(int accommodation_id , Admin admin , String reason);
	boolean accommodation_suspension(int accommodation_id, Admin admin , String reason);
	int checkSuspension(int accommodation_id);
	boolean isValidAccmNum(int accm_num);
	List<Accommodation> getSusAcco();
	List<Accommodation> getNormalAcco();
}

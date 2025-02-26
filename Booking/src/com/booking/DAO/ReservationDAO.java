package com.booking.DAO;

import java.time.LocalDate;

import com.booking.dto.User;

public interface ReservationDAO {

	boolean overeas_reservation(int acco_id, User user, LocalDate s_date, LocalDate e_date, int reservation_number);
	boolean domestic_reservation(int num, User user, LocalDate s_date, LocalDate e_date, int reservation_number);
	

}

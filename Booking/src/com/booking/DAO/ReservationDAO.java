package com.booking.DAO;

import java.time.LocalDate;
import java.util.List;

import com.booking.dto.Reservation;

public interface ReservationDAO {

	List<Integer> getDateRangeReservedNum(int acco_id, LocalDate s_date, LocalDate e_date);
	int getAllowedNum(int acco_id);
	int getAccommodationStatus(int acco_id);
	int getPrice(int acco_id);
	boolean reservate(String id, int acco_id, LocalDate sDate, LocalDate eDate, int i, int mem);
	List<Reservation> getReservationList(String id);
	boolean deleteReservation(int reservationID);

}

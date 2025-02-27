package com.booking.service;

import com.booking.dto.Reservation;

public interface ReservationService {

	void doReservate(boolean isDomestic);
	void suggest_accommodation(String location_name);
	Reservation showAndSelectReservation();
	void deleteReservation();
}

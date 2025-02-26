package com.booking.service;

public interface ReservationService {

	void doReservate(boolean isDomestic);
	void suggest_accommodation(String location_name);
	void showReservation();
	void deleteReservation();
}

package com.booking.service;

public interface ReservationService {

	void domestic_reservation();
	void overseas_reservation();
	void suggest_accommodation(String location_name);
}

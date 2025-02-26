package com.booking.dto;

import java.time.LocalDate;

public class Reservation {
	
	int reservation_id;
	String user_id;
	int accomodation_id;
	LocalDate reservation_start_date;
	LocalDate reservation_end_date;
	int reservation_price;
	int reservation_number;
	int price;
	
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public Reservation() {
	}
	public Reservation(int reservation_id, String user_id, int accomodation_id, LocalDate reservation_start_date,
			LocalDate reservation_end_date, int reservation_price, int reservation_number) {
		super();
		this.reservation_id = reservation_id;
		this.user_id = user_id;
		this.accomodation_id = accomodation_id;
		this.reservation_start_date = reservation_start_date;
		this.reservation_end_date = reservation_end_date;
		this.reservation_price = reservation_price;
		this.reservation_number = reservation_number;
	}
	public int getReservation_id() {
		return reservation_id;
	}
	public void setReservation_id(int reservation_id) {
		this.reservation_id = reservation_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getAccomodation_id() {
		return accomodation_id;
	}
	public void setAccomodation_id(int accomodation_id) {
		this.accomodation_id = accomodation_id;
	}
	public LocalDate getReservation_start_date() {
		return reservation_start_date;
	}
	public void setReservation_start_date(LocalDate reservation_start_date) {
		this.reservation_start_date = reservation_start_date;
	}
	public LocalDate getReservation_end_date() {
		return reservation_end_date;
	}
	public void setReservation_end_date(LocalDate reservation_end_date) {
		this.reservation_end_date = reservation_end_date;
	}
	public int getReservation_price() {
		return reservation_price;
	}
	public void setReservation_price(int reservation_price) {
		this.reservation_price = reservation_price;
	}
	public int getReservation_number() {
		return reservation_number;
	}
	public void setReservation_number(int reservation_number) {
		this.reservation_number = reservation_number;
	}

}

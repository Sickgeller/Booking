package com.booking.dto;

import java.util.Date;

public class Payment {
	
	int payment_id; // 결제 ID
	String user_id; // 사용자 ID
	int reservation_id; // 예약 아이디
	int payment_used_cash; // 사용된 ID
	public Payment(int payment_id, String user_id, int reservation_id, int payment_used_cash, int payment_used_point,
			int payment_total_price, Date payment_date, int payment_method) {
		super();
		this.payment_id = payment_id;
		this.user_id = user_id;
		this.reservation_id = reservation_id;
		this.payment_used_cash = payment_used_cash;
		this.payment_used_point = payment_used_point;
		this.payment_total_price = payment_total_price;
		this.payment_date = payment_date;
		this.payment_method = payment_method;
	}
	int payment_used_point; //  사용한 ID
	int payment_total_price; // 전체금약
	Date payment_date; // 구매한 날짜
	int payment_method;  // 1 = 전액현금 , 2= 현금+포인트 , 3= 현금 + 쿠폰 4. 현금+포인트 + 쿠폰
	
	public int getPayment_id() {
		return payment_id;
	}
	public void setPayment_id(int payment_id) {
		this.payment_id = payment_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getReservation_id() {
		return reservation_id;
	}
	public void setReservation_id(int reservation_id) {
		this.reservation_id = reservation_id;
	}
	public int getPayment_used_cash() {
		return payment_used_cash;
	}
	public void setPayment_used_cash(int payment_used_cash) {
		this.payment_used_cash = payment_used_cash;
	}
	public int getPayment_used_point() {
		return payment_used_point;
	}
	public void setPayment_used_point(int payment_used_point) {
		this.payment_used_point = payment_used_point;
	}
	public int getPayment_total_price() {
		return payment_total_price;
	}
	public void setPayment_total_price(int payment_total_price) {
		this.payment_total_price = payment_total_price;
	}
	public Date getPayment_date() {
		return payment_date;
	}
	public void setPayment_date(Date payment_date) {
		this.payment_date = payment_date;
	}
	public int getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(int payment_method) {
		this.payment_method = payment_method;
	}

	
}

	

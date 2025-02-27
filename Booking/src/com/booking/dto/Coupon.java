package com.booking.dto;

import java.util.Date;


public class Coupon {
	
	int coupon_ID;
	String admin_ID;
	String coupon_code;
	Date coupon_issuance_date;
	Date coupon_expired_date;
	int coupon_discount;
	
	
	public Coupon(int coupon_ID, String admin_ID, String coupon_code, Date coupon_issuance_date, Date coupon_expired_date,
			int coupon_discount) {
		super();
		this.coupon_ID = coupon_ID;
		this.admin_ID = admin_ID;
		this.coupon_code = coupon_code;
		this.coupon_issuance_date = coupon_issuance_date;
		this.coupon_expired_date = coupon_expired_date;
		this.coupon_discount = coupon_discount;
	}
	public Coupon() {
		// TODO Auto-generated constructor stub
	}
	

	public int getCoupon_ID() {
		return coupon_ID;
	}
	public void setCoupon_ID(int coupon_ID) {
		this.coupon_ID = coupon_ID;
	}
	public String getAdmin_ID() {
		return admin_ID;
	}
	public void setAdmin_ID(String admin_ID) {
		this.admin_ID = admin_ID;
	}
	public String getCoupon_code() {
		return coupon_code;
	}
	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}
	public Date getCoupon_issuance_date() {
		return coupon_issuance_date;
	}
	public void setCoupon_issuance_date(Date coupon_issuance_date) {
		this.coupon_issuance_date = coupon_issuance_date;
	}
	public Date getCoupon_expired_date() {
		return coupon_expired_date;
	}
	public void setCoupon_expired_date(Date coupon_expired_date) {
		this.coupon_expired_date = coupon_expired_date;
	}
	public int getCoupon_discount() {
		return coupon_discount;
	}
	public void setCoupon_discount(int coupon_discount) {
		this.coupon_discount = coupon_discount;
	}
	
}

package com.booking.dto;

import java.util.Date;

public class Review {

	int review_ID;
	String ID; // 유저 아이디
	int accomodation_ID; //  아이디
	Date review_date; // 작성일
	String review_content; // 리뷰 작성 내용
	int review_rating; // 리뷰 평점 
	
	public Review(int review_ID, String ID, int accomodation_ID, Date review_date, String review_content,
			int review_rating) {
		
		super();
		this.review_ID = review_ID;
		this.ID = ID;
		this.accomodation_ID = accomodation_ID;
		this.review_date = review_date;
		this.review_content = review_content;
		this.review_rating = review_rating;
		
	}
	public Review() {
	}
	public int getReview_ID() {
		return review_ID;
	}
	public void setReview_ID(int review_ID) {
		this.review_ID = review_ID;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public int getAccomodation_ID() {
		return accomodation_ID;
	}
	public void setAccomodation_ID(int accomodation_ID) {
		this.accomodation_ID = accomodation_ID;
	}
	public Date getReview_date() {
		return review_date;
	}
	public void setReview_date(Date review_date) {
		this.review_date = review_date;
	}
	public String getReview_content() {
		return review_content;
	}
	public void setReview_content(String review_content) {
		this.review_content = review_content;
	}
	public int getReview_rating() {
		return review_rating;
	}
	public void setReview_rating(int review_rating) {
		this.review_rating = review_rating;
	}
	
}

package com.booking.service;

import com.booking.dto.User;

public interface ReviewService {

	void updateReview(User user); // 리뷰수정하는 메서드
	void deleteReview(User user); // 리뷰 삭제하는 메서드
	void selectdetailReview(int accomodation_ID);
}

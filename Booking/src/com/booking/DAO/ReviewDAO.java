package com.booking.DAO;

import java.util.List;

import com.booking.dto.Review;
import com.booking.dto.User;

public interface ReviewDAO {
	
    void insertReview(Review review);
    boolean updateReview(int reviewID , String review_content);
    boolean deleteReview(int reviewID);
    Review getReviewByID(int reviewID);
    List<Review> getReviewsByAccommodationID(int accommodationID);
    List<Review> getReviewByUserID(User user);
    
}

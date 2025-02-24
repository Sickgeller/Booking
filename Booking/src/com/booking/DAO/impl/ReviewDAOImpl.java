package com.booking.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.booking.DAO.ReviewDAO;
import com.booking.dto.Review;
import com.booking.dto.User;
import com.dbutil.DBUtil;

public class ReviewDAOImpl implements ReviewDAO {

	@Override
	public void insertReview(Review review) {
		String sql = "INSERT INTO REVIEW (USER_ID, ACCOMMODATION_ID, REVIEW_CONTENT, REVIEW_RATING, REVIEW_DATE) VALUES (?, ?, ?, ?, SYSDATE)";
		try (Connection conn = DBUtil.getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, review.getID());
			pstmt.setInt(2, review.getAccomodation_ID());
			pstmt.setString(3, review.getReview_content());
			pstmt.setInt(4, review.getReview_rating());

			pstmt.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean updateReview(int reviewID , String review_content) {
		String sql = "UPDATE REVIEW SET REVIEW_CONTENT = ? WHERE REVIEW_ID = ?";
		int num = Integer.MIN_VALUE;
		try (Connection conn = DBUtil.getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, review_content);
			pstmt.setInt(2, reviewID);
			num = pstmt.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if(num == 1) {
			return true; // update 개수가 1개일 떄 성공
		}else {
			return false; // 1개 이외일때 업데이트 실패
		}
	}

	@Override
	public boolean deleteReview(int reviewID) {
		String sql = "DELETE FROM REVIEW WHERE REVIEW_ID = ?";
		int result = Integer.MIN_VALUE;
		try (Connection conn = DBUtil.getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, reviewID);
			result = pstmt.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result == 1 ? true : false;
	}

	@Override
	public List<Review> getReviewByUserID(User user) {
		Connection conn = null;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<Review> reviewList = new ArrayList<Review>();
		
		try {
			conn = DBUtil.getConnection(); 
			sql = "SELECT * FROM REVIEW WHERE USER_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getID());
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				do {
					Review review = new Review();
					review.setReview_ID(rs.getInt("REVIEW_ID"));
					review.setID(rs.getString("USER_ID"));
					review.setAccomodation_ID(rs.getInt("ACCOMODATION_ID"));
					review.setReview_content(rs.getString("REVIEW_CONTENT"));
					review.setReview_rating(rs.getInt("REVIEW_RATING"));
					review.setReview_date(rs.getDate("REVIEW_DATE"));
					reviewList.add(review);
				}while(rs.next());
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return reviewList;
	}

	@Override
	public Review getReviewByID(int reviewID) {
		String sql = "SELECT * FROM REVIEW WHERE REVIEW_ID = ?";
		try (Connection conn = DBUtil.getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, reviewID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				Review review = new Review();
				review.setReview_ID(rs.getInt("REVIEW_ID"));
				review.setID(rs.getString("USER_ID"));
				review.setAccomodation_ID(rs.getInt("ACCOMMODATION_ID"));
				review.setReview_content(rs.getString("REVIEW_CONTENT"));
				review.setReview_rating(rs.getInt("REVIEW_RATING"));
				review.setReview_date(rs.getDate("REVIEW_DATE"));
				return review;
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Review> getReviewsByAccommodationID(int accommodationID) {
		List<Review> reviews = new ArrayList<>();
		String sql = "SELECT * FROM REVIEW WHERE ACCOMMODATION_ID = ?";
		try (Connection conn = DBUtil.getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, accommodationID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Review review = new Review();
				review.setReview_ID(rs.getInt("REVIEW_ID"));
				review.setID(rs.getString("USER_ID"));
				review.setAccomodation_ID(rs.getInt("ACCOMMODATION_ID"));
				review.setReview_content(rs.getString("REVIEW_CONTENT"));
				review.setReview_rating(rs.getInt("REVIEW_RATING"));
				review.setReview_date(rs.getDate("REVIEW_DATE"));
				reviews.add(review);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return reviews;
	}
}

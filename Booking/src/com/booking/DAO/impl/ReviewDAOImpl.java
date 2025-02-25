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
import com.util.Util;

public class ReviewDAOImpl implements ReviewDAO {

	@Override
	public void insertReview(Review review) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection(); 
			sql = "INSERT INTO REVIEW (USER_ID, ACCOMMODATION_ID, REVIEW_CONTENT, REVIEW_RATING, REVIEW_DATE) VALUES (?, ?, ?, ?, SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, review.getID());
			pstmt.setInt(2, review.getAccomodation_ID());
			pstmt.setString(3, review.getReview_content());
			pstmt.setInt(4, review.getReview_rating());
			int result = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, result);
		} catch (SQLException | ClassNotFoundException e) {
			if(conn != null) try {conn.rollback();}catch(Exception e1) {}
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	@Override
	public boolean updateReview(int reviewID , String review_content) {
		int num = Integer.MIN_VALUE;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection(); 
			sql = "UPDATE REVIEW SET REVIEW_CONTENT = ? WHERE REVIEW_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, review_content);
			pstmt.setInt(2, reviewID);
			num = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, num);
		} catch (SQLException | ClassNotFoundException e) {
			if(conn != null) try {conn.rollback();}catch(Exception e1) {}
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return num == 1;
	}

	@Override
	public boolean deleteReview(int reviewID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = Integer.MIN_VALUE;
		try {
			conn = DBUtil.getConnection(); 
			sql = "DELETE FROM REVIEW WHERE REVIEW_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reviewID);
			result = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, result);
		} catch (SQLException | ClassNotFoundException e) {
			if(conn != null) try {conn.rollback();}catch(Exception e1) {}
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return result == 1;
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
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection(); 
			sql = "SELECT * FROM REVIEW WHERE REVIEW_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reviewID);
			rs = pstmt.executeQuery();
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
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
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

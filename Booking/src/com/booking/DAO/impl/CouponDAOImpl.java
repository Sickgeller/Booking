package com.booking.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.booking.DAO.CouponDAO;
import com.booking.dto.Coupon;
import com.dbutil.DBUtil;
import com.util.Util;




public class CouponDAOImpl implements CouponDAO{

	@Override
	public List<Coupon> getEveryCoupon() { // 모든 쿠폰 가져오는 메서드
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		List<Coupon> couponList = new ArrayList<>();
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM COUPON";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					int couponID = rs.getInt(1);
					String adminID = rs.getString(2);
					String couponCode = rs.getString(3);
					Date coupon_issuance_date = rs.getDate(4);
					Date coupon_expired_date = rs.getDate(5);
					int couponDiscount = rs.getInt(6);
					couponList.add(new Coupon(couponID,adminID,couponCode, coupon_issuance_date, coupon_expired_date , couponDiscount));
				}while(rs.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return couponList;
	}


	@Override
	public boolean giveCouponUser(int coupon_id, String user_ID) {
	    Connection conn = null;
	    PreparedStatement select_pstmt = null;
	    PreparedStatement insert_pstmt = null;
	    PreparedStatement update_pstmt = null;
	    ResultSet rs = null;
	    int rowsUpdated = Integer.MIN_VALUE;
	    int rowsInserted = Integer.MIN_VALUE;
	    

	    try {
	        conn = DBUtil.getConnection();
	        // 유저가 이미 쿠폰을 가지고 있는지 확인
	        String checkExistenceSql = "SELECT COUPON_ID, USER_ID FROM CP_POSSESS WHERE COUPON_ID = ? AND USER_ID = ?";
	        select_pstmt = conn.prepareStatement(checkExistenceSql);
	        select_pstmt.setInt(1, coupon_id);
	        select_pstmt.setString(2, user_ID);
	        rs = select_pstmt.executeQuery();

	        if (rs.next()) {
	            // 유저가 이미 쿠폰을 가지고 있으므로, 쿠폰 개수를 업데이트
	            String updateSql = "UPDATE CP_POSSESS SET COUPON_COUNT = COUPON_COUNT + 1 WHERE COUPON_ID = ? AND USER_ID = ?";
	            update_pstmt = conn.prepareStatement(updateSql);
	            update_pstmt.setInt(1, coupon_id);
	            update_pstmt.setString(2, user_ID);
	            rowsUpdated = update_pstmt.executeUpdate();
	            Util.doCommitOrRollback(conn, rowsUpdated);
	        } else {
	            // 유저가 쿠폰을 가지고 있지 않으므로, 새 쿠폰을 삽입
	            String insertSql = "INSERT INTO CP_POSSESS (COUPON_ID, USER_ID, COUPON_COUNT) VALUES (?, ?, 1)";
	            insert_pstmt = conn.prepareStatement(insertSql);
	            insert_pstmt.setInt(1, coupon_id);
	            insert_pstmt.setString(2, user_ID);
	            rowsInserted = insert_pstmt.executeUpdate();
	            Util.doCommitOrRollback(conn, rowsInserted);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        try { if (conn != null)conn.rollback();} catch (SQLException se) {}
	    } finally {
	        // 3단계: 리소스 정리
	        DBUtil.executeClose(rs, select_pstmt, conn);
	        DBUtil.executeClose(null, insert_pstmt, conn);
	        DBUtil.executeClose(null, update_pstmt, conn);
	    }
	    return true; // 쿠폰 삽입 또는 업데이트가 성공적으로 완료됨
	}



	@Override
	public List<Coupon> getUserCoupon(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<Coupon> couponList = new ArrayList<>();
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * " +
					"FROM CP_POSSESS cp " +
					"LEFT JOIN coupon c ON cp.coupon_id = c.coupon_id " + 
					"WHERE cp.user_id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					int couponID = rs.getInt(1);
					String adminID = rs.getString(2);
					String couponCode = rs.getString(3);
					Date coupon_issuance_date = rs.getDate(4);
					Date coupon_expired_date = rs.getDate(5);
					int couponDiscount = rs.getInt(6);
					couponList.add(new Coupon(couponID,adminID,couponCode, coupon_issuance_date, coupon_expired_date , couponDiscount));
				}while(rs.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return couponList;
	}


	@Override
	public boolean reg_coupon(String coupon_code, String adminID , int coupon_discount) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int update = Integer.MIN_VALUE;
		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO COUPON (COUPON_ID, ADMIN_ID, COUPON_CODE, COUPON_ISSUANCE_DATE, COUPON_EXPIRED_DATE, COUPON_DISCOUNT) " +
					"VALUES (COUPON_SEQ.NEXTVAL, ?, ?, SYSDATE, SYSDATE + 30, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, adminID);
			pstmt.setString(2, coupon_code);
			pstmt.setInt(3, coupon_discount);
			update = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, update);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return update == 1;
	}
	@Override
	public boolean isCouponExists(String coupon_code) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM COUPON WHERE COUPON_CODE = ?";
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, coupon_code);
			rs = pstmt.executeQuery();

			if (rs.next() && rs.getInt(1) > 0) {
				return true;  // 중복된 쿠폰 코드가 있음
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}

		return false;  // 중복된 쿠폰 코드 없음
	}

}

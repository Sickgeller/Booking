package com.booking.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	public boolean giveCouponUser(int coupon_id,String User_ID) {
		Connection conn = null;
		PreparedStatement select_pstmt = null;
		PreparedStatement insert_pstmt = null;
		PreparedStatement update_pstmt = null;
		String insert_sql = null;
		String update_sql = null;
		ResultSet rs = null;

		try {
			conn = DBUtil.getConnection();

			String select_sql = "SELECT COUPON_ID, COUPON_CODE,COUPON_DISCOUNT FROM COUPON";
			rs = select_pstmt.executeQuery();

			List<Integer> couponList = new ArrayList<>();
			while (rs.next()) {
				couponList.add(rs.getInt("COUPON_ID"));
			}

			if(!isDupUserCoupon(coupon_id,User_ID)) {

				insert_sql = "INSERT INTO CP_POSSESS (COUPON_ID, USER_ID,COUPON_COUNT)" +
						"SELECT ?,U.USER_ID, 1" +
						"FROM \"USER\" U WHERE TRUNC(U.REG_DATE) = TRUNC(SYSDATE)";


				insert_pstmt.setInt(1, coupon_id);
				insert_pstmt.setString(2, User_ID);
				int update = insert_pstmt.executeUpdate();
				if(update == 1) {

					conn.commit();
					System.out.println("새 쿠폰 지급 완료");
				}else {
					conn.rollback();
					System.out.println("쿠폰을 지급하지 못했습니다.");
				}

			}else {
				update_sql =  "UPDATE CP_POSSESS SET COUPON_COUNT = COUPON_COUNT + 1 WHERE COUPON_ID = ? AND USER_ID=?";
				update_pstmt = conn.prepareStatement(update_sql);

				update_pstmt.setInt(1, coupon_id);
				update_pstmt.setString(1, User_ID);
				int update= update_pstmt.executeUpdate();

				if (update > 0) {
					conn.commit();
					System.out.println(" 기존 쿠폰 업데이트 완료 (보유 수 증가)");
				} else {
					conn.rollback();
					System.out.println(" 쿠폰 업데이트 실패");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, update_pstmt, conn);
			DBUtil.executeClose(rs, insert_pstmt, conn);
			DBUtil.executeClose(rs, select_pstmt,conn);
		}
		return false;
	}

	private boolean isDupUserCoupon(int coupon_ID, String user_ID) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM COUPON WHERE COUPON_ID = ?";
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, coupon_ID);
			pstmt.setString(2,user_ID);
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

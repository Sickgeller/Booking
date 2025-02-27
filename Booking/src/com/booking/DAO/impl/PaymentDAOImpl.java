package com.booking.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.booking.DAO.PaymentDAO;
import com.booking.dto.Payment;
import com.booking.dto.User;
import com.dbutil.DBUtil;
import com.util.Util;

public class PaymentDAOImpl implements PaymentDAO{

	private User user;

	public PaymentDAOImpl(User user) {
		this.user = user;
	}

	@Override
	public boolean updateCashPayment(int cash) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE \"USER\" SET CASH = ? WHERE USER_ID = ?";
		int update = Integer.MIN_VALUE;

		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cash);
			pstmt.setString(2, user.getID());
			update = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, update);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return update == 1;
	}

	@Override
	public List<Payment> getPaymentHistory() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		List<Payment> result = new ArrayList<>();

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM PAYMENT WHERE USER_ID = ? ORDER BY PAYMENT_ID desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				do {
					result.add(new Payment(
							rs.getInt("PAYMENT_ID"),
							rs.getString("USER_ID"),
							rs.getInt("RESERVATION_ID"),
							rs.getInt("PAYMENT_USED_CASH"),
							rs.getInt("PAYMENT_USED_POINT"),
							rs.getInt("PAYMENT_TOTAL_PRICE"),
							rs.getDate("PAYMENT_DATE"),
							rs.getInt("PAYMENT_METHOD")
							));
				}while(rs.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs,pstmt, conn);
		}
		return result;
	}

}

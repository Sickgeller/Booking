package com.booking.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import com.booking.DAO.ReservationDAO;
import com.booking.DAO.UserDAO;
import com.booking.dto.User;
import com.dbutil.DBUtil;
import com.util.Util;

public class ReservationDAOImpl implements ReservationDAO{

	@Override
	public boolean overeas_reservation(int acco_id, User user, LocalDate s_date, LocalDate e_date, int reservation_number) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement price_pstmt = null;
		String i_sql = null;
		String s_sql_prcie = "SELECT ACCOMMODATION_PRICE FROM ACCOMMODATION WHERE ACCOMMODATION_ID=?";
		ResultSet rs = null;
		int price = 0;
		int update = 0;
		try {
			String user_ID = user.getID();


			conn = DBUtil.getConnection();

			i_sql = "INSERT INTO RESERVATION ("
					+ "RESERVATION_ID, "  // PRIMARY KEY (시퀀스로 자동 증가)
					+ "USER_ID, "
					+ "ACCOMMODATION_ID, "
					+ "RESERVATION_START_DATE, "
					+ "RESERVATION_END_DATE, "
					+ "RESERVATION_PRICE, "
					+ "RESERVATION_NUMBER)"
					+ "VALUES (RESERVATION_SEQ.NEXTVAL, ?, ?, ?, ?, ?,?)";

			pstmt = conn.prepareStatement(i_sql);
			price_pstmt= conn.prepareStatement(s_sql_prcie);
			price_pstmt.setInt(1,acco_id);
			rs = price_pstmt.executeQuery();

			if(rs.next()) {
				price = rs.getInt("ACCOMMODATION_PRICE");
			}



			pstmt.setString(1, user_ID);
			pstmt.setInt(2, acco_id);
			pstmt.setDate(3, java.sql.Date.valueOf(s_date));
			pstmt.setDate(4, java.sql.Date.valueOf(e_date));
			pstmt.setInt(5, price);
			pstmt.setInt(6, reservation_number);

			update = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, update);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return update == 1;
	}

	@Override
	public boolean domestic_reservation(int num, User user, LocalDate s_date, LocalDate e_date,
			int reservation_number) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement price_pstmt = null;
		String i_sql = null;
		String s_sql_prcie = "SELECT ACCOMMODATION_PRICE FROM ACCOMMODATION WHERE ACCOMMODATION_ID=?";
		ResultSet rs = null;
		int update = 0;
		int price = 0;
		try {
			String user_ID = user.getID();
			conn = DBUtil.getConnection();

			i_sql = "INSERT INTO RESERVATION ("
					+ "RESERVATION_ID, "  // ✅ PRIMARY KEY (시퀀스로 자동 증가)
					+ "USER_ID, "
					+ "ACCOMMODATION_ID, "
					+ "RESERVATION_START_DATE, "
					+ "RESERVATION_END_DATE, "
					+ "RESERVATION_PRICE, "
					+ "RESERVATION_NUMBER)"
					+ "VALUES (RESERVATION_SEQ.NEXTVAL, ?, ?, ?, ?, ?,?)";

			pstmt = conn.prepareStatement(i_sql);
			price_pstmt= conn.prepareStatement(s_sql_prcie);
			price_pstmt.setInt(1,num);
			rs = price_pstmt.executeQuery();

			if(rs.next()) {
				price = rs.getInt("ACCOMMODATION_PRICE");
			}


			pstmt.setString(1, user_ID);
			pstmt.setInt(2, num);
			pstmt.setDate(3, java.sql.Date.valueOf(s_date));
			pstmt.setDate(4, java.sql.Date.valueOf(e_date));
			pstmt.setInt(5, price);
			pstmt.setInt(6, reservation_number);

			update = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, update);

		}catch(Exception e) {
			e.printStackTrace();
		}
		return update == 1;
	}

}

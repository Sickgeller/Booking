package com.booking.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.booking.DAO.AdminDAO;
import com.booking.dto.Admin;
import com.dbutil.DBUtil;

public class AdminDAOImpl implements AdminDAO {


	public Admin adminLogin(String ID, String passwd) {

		Connection conn = null;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Admin result = null;
		
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM ADMIN WHERE ADMIN_ID = ? AND ADMIN_PASSWORD = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			pstmt.setString(2, passwd);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String email = rs.getString("ADMIN_EMAIL");
				String name = rs.getString("ADMIN_NAME");
				result = new Admin(ID,passwd,email,name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return result;
	}
}

package com.booking.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import com.booking.DAO.UserDAO;
import com.booking.dto.User;
import com.dbutil.DBUtil;
import com.util.Util;

public class UserDAOImpl implements UserDAO{

	@Override
	public User login(String ID, String passwd) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT * FROM \"USER\" WHERE USER_ID = ? AND PASSWORD = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			pstmt.setString(2, passwd);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String name = rs.getString("NAME");
				String email = rs.getString("EMAIL");
				int point = rs.getInt("POINT");
				int cash = rs.getInt("CASH");
				Date reg_date = rs.getDate("REG_DATE");
				return new User(ID, email, passwd, name, null, point, cash, reg_date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return null;
	}

	@Override
	public boolean checkIDDuplicate(String ID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT COUNT(USER_ID) FROM \"USER\"  WHERE USER_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			rs = pstmt.executeQuery();
			return rs.next() && rs.getInt(1) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return false;
	}

	@Override
	public boolean register(String ID, String passwd, String name, String email) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "INSERT INTO \"USER\" (USER_ID, PASSWORD, NAME, EMAIL) VALUES (?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			pstmt.setString(2, passwd);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			int result = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, result);
		} catch (Exception e) { 
			e.printStackTrace();
			if(conn != null) try {conn.rollback();}catch(Exception e1) {}
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return false;
	}

	@Override
	public boolean changeUserName(String ID, String name) {
		return executeUpdate("UPDATE \"USER\" SET NAME = ? WHERE USER_ID = ?", name, ID);
	}

	@Override
	public boolean changeUserEmail(String ID, String email) {
		return executeUpdate("UPDATE \"USER\" SET EMAIL = ? WHERE USER_ID = ?", email, ID);
	}

	@Override
	public boolean changeUserPW(String ID, String passwd) {
		return executeUpdate("UPDATE \"USER\" SET PASSWORD = ? WHERE USER_ID = ?", passwd, ID);
	}

	@Override
	public boolean deleteUser(String ID) {
		return executeUpdate("DELETE FROM \"USER\" WHERE USER_ID = ?", ID);
	}
	@Override
	public String checkUserGrade(String ID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			String sql = ("SELECT USER_GRADE FROM \"USER\" WHERE USER_ID = ?");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			rs = pstmt.executeQuery();
			return rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return null;
	}


	// 코드들 간략화를위한 내부메서드
	private boolean executeUpdate(String sql, String... params) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = Integer.MIN_VALUE;
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				pstmt.setString(i + 1, params[i]);
			}
			result = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return result == 1;
	}

	@Override
	public boolean chargeAccount(String ID, int money) {
		Connection conn = null;
		String sql = null;
		PreparedStatement pstmt = null;
		int result = Integer.MIN_VALUE;
		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE \"USER\" SET CASH = ? WHERE USER_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, money);
			pstmt.setString(2,ID);
			result = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, result);
		} catch (Exception e) {
			if(conn != null)try {conn.rollback();}catch(Exception e1) {}
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return result == 1;
	}
}

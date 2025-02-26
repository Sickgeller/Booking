package com.booking.DAO.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.booking.DAO.AccoMgmtDAO;
import com.booking.dto.Accommodation;
import com.booking.dto.Admin;
import com.dbutil.DBUtil;

public class AccoMgmtDAOImpl implements AccoMgmtDAO{


	@Override
	public boolean accommodation_suspension(int accommodation_id, Admin admin, String reason) {
		Connection conn = null;
		PreparedStatement pstmtI = null;
		PreparedStatement pstmtU = null;
		String sqlI = null;
		String sqlU = null;
		int insert = Integer.MIN_VALUE;
		int update = Integer.MIN_VALUE;
		try {
			conn = DBUtil.getConnection();
			sqlI = "INSERT INTO AMMD_MGMT (AMMD_MGMT_ID, ADMIN_ID, ACCOMMODATION_ID, MGMT_REASON, MGMT_DETAILS)"
					+ " VALUES(AMMD_MGMT_SEQ.NEXTVAL, ? , ?, ?, '영업정지')";
			sqlU = "UPDATE ACCOMMODATION SET ACCOMMODATION_STATUS = 0 WHERE ACCOMMODATION_ID = ?";	
			pstmtI = conn.prepareStatement(sqlI);
			pstmtI.setString(1, admin.getID());
			pstmtI.setInt(2, accommodation_id);
			pstmtI.setString(3, reason);

			pstmtU = conn.prepareStatement(sqlU);
			pstmtU.setInt(1, accommodation_id);

			insert = pstmtI.executeUpdate();
			update = pstmtU.executeUpdate();

			if(insert != 1 || update != 1 ) {
				try {conn.rollback();} catch (SQLException e) {}
			}else {
				try {conn.commit();} catch (SQLException e) {}
			}

		} catch (ClassNotFoundException | SQLException e) {
			try {conn.rollback();} catch (SQLException e1) {}
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(null, pstmtU, conn);
		}
		return insert == 1 && update == 1;
	}

	@Override
	public int checkSuspension(int accommodation_id) { // 입력된 숙소의 ID가 정지상태인지 확인하는 메서드 true
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT ACCOMMODATION_STATUS FROM ACCOMMODATION WHERE ACCOMMODATION_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, accommodation_id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt("ACCOMMODATION_STATUS");
			}else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return -1;
	}

	@Override
	public boolean isValidAccmNum(int accm_num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT ACCOMMODATION_STATUS FROM ACCOMMODATION WHERE ACCOMMODATION_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, accm_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return false;
	}

	@Override
	public List<Accommodation> getSusAcco() { // 영업정지상태의 숙소 리스트 반환
		List<Accommodation> result = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM ACCOMMODATION WHERE ACCOMMODATION_STATUS = 0";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					int cnt = 1;
					int accommodatin_id = rs.getInt(cnt++);
					String accommodation_name = rs.getString(cnt++);
					String accommodation_address = rs.getString(cnt++);
					String accommodation_description = rs.getString(cnt++);
					int accommodation_price = rs.getInt(cnt++);
					String location_name = rs.getString(cnt++);
					String recommendation_season = rs.getString(cnt++);
					int accommodation_status = rs.getInt(cnt++);
					int allowed_number = rs.getInt(cnt++);
					Accommodation accm = new Accommodation(accommodatin_id, accommodation_name, accommodation_address,
							accommodation_description, accommodation_price, location_name,
							recommendation_season, accommodation_status, allowed_number);
					result.add(accm);
				}while(rs.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return result;
	}

	@Override
	public boolean accommodation_resume(int accommodation_id, Admin admin, String reason) {
		Connection conn = null;
		PreparedStatement pstmtI = null;
		PreparedStatement pstmtU = null;
		String sqlI = null;
		String sqlU = null;
		int insert = Integer.MIN_VALUE;
		int update = Integer.MIN_VALUE;
		try {
			conn = DBUtil.getConnection();
			sqlI = "INSERT INTO AMMD_MGMT (AMMD_MGMT_ID, ADMIN_ID, ACCOMMODATION_ID, MGMT_REASON, MGMT_DETAILS)"
					+ " VALUES(AMMD_MGMT_SEQ.NEXTVAL, ? , ?, ?, '영업재개')";
			sqlU = "UPDATE ACCOMMODATION SET ACCOMMODATION_STATUS = 1 WHERE ACCOMMODATION_ID = ?";	
			pstmtI = conn.prepareStatement(sqlI);
			pstmtU = conn.prepareStatement(sqlU);
			pstmtI.setString(1, admin.getID());
			pstmtI.setInt(2, accommodation_id);
			pstmtI.setString(3, reason);

			pstmtU.setInt(1, accommodation_id);

			insert = pstmtI.executeUpdate();
			update = pstmtU.executeUpdate();

			if(insert != 1 || update != 1 ) {
				try {conn.rollback();} catch (SQLException e) {}
			}else {
				try {conn.commit();} catch (SQLException e) {}
			}

		} catch (ClassNotFoundException | SQLException e) {
			try {conn.rollback();} catch (SQLException e1) {}
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(null, pstmtU, conn);
		}
		return insert == 1 && update == 1;
	}

	@Override
	public List<Accommodation> getNormalAcco() {
		List<Accommodation> result = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM ACCOMMODATION WHERE ACCOMMODATION_STATUS = 0";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					int cnt = 1;
					int accommodatin_id = rs.getInt(cnt++);
					String accommodation_name = rs.getString(cnt++);
					String accommodation_address = rs.getString(cnt++);
					String accommodation_description = rs.getString(cnt++);
					int accommodation_price = rs.getInt(cnt++);
					String location_name = rs.getString(cnt++);
					String recommendation_season = rs.getString(cnt++);
					int accommodation_status = rs.getInt(cnt++);
					int allowed_number = rs.getInt(cnt++);
					Accommodation accm = new Accommodation(accommodatin_id, accommodation_name, accommodation_address,
							accommodation_description, accommodation_price, location_name,
							recommendation_season, accommodation_status, allowed_number);
					result.add(accm);
				}while(rs.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return result;
	}


}

package com.booking.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.booking.DAO.AccommodationDAO;
import com.booking.dto.Accommodation;
import com.dbutil.DBUtil;

public class AccommodationDAOImpl implements AccommodationDAO{

	@Override
	public List<Accommodation> getOverseasAccommodation() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		List<Accommodation> result = new ArrayList<>();
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM ACCOMMODATION WHERE LOCATION_NAME = '해외' ORDER BY ACCOMMODATION_ID";
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
	public List<Accommodation> getDomesticAccommodation(String local_name) { // 지정된 지역이름만반환
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		List<Accommodation> result = new ArrayList<>();
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM ACCOMMODATION WHERE LOCATION_NAME = ? ORDER BY ACCOMMODATION_ID";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, local_name);
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
	public List<Accommodation> getDomesticAccommodation() { // 국내 전체 반환
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		List<Accommodation> result = new ArrayList<>();
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM ACCOMMODATION WHERE LOCATION_NAME != '해외' ORDER BY ACCOMMODATION_ID";
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
	public boolean insertAccommodation(String accommodation_name, String accommodation_address,
			String accommodation_description, int accommodation_price, String location_name,
			String recommendation_season, int accommodation_status, int allowed_number) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;
		int count = Integer.MIN_VALUE;
		try {
			// JDBC 수행 1
			conn = DBUtil.getConnection();
			// sql문 작성
			sql = "insert into accommodation(accommodation_id,accommodation_name,accommodation_address,accommodation_description,"
					+ "accommodation_price, location_name, recommendation_season, accommodation_status,allowed_number)"
					+ " values(accommodation_seq.nextval,?,?,?,?,?,?,?,?)";
			// 3단계
			pstmt = conn.prepareStatement(sql);
			// 바인딩
			pstmt.setString(++cnt,accommodation_name);
			pstmt.setString(++cnt,accommodation_address);
			pstmt.setString(++cnt,accommodation_description);
			pstmt.setInt(++cnt,accommodation_price);
			pstmt.setString(++cnt,location_name);
			pstmt.setString(++cnt,recommendation_season);
			pstmt.setInt(++cnt,accommodation_status);
			pstmt.setInt(++cnt,allowed_number);

			// 4단계
			count = pstmt.executeUpdate();
			if(count == 1) {
				conn.commit();
			}else {
				conn.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//자원정리
			DBUtil.executeClose(null, pstmt, conn);
		}
		return count == 1;
	}


	@Override
	public List<Accommodation> getEveryAccommodation() { // 모든 숙소의 리스트 반환
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		List<Accommodation> result = new ArrayList<>();
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM ACCOMMODATION ORDER BY ACCOMMODATION_ID";
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
	public Accommodation getAccommodationInfo(int accommodation_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		Accommodation result = null;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM ACCOMMODATION WHERE ACCOMMODATION_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, accommodation_id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
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
				result = new Accommodation(accommodatin_id, accommodation_name, accommodation_address,
						accommodation_description, accommodation_price, location_name,
						recommendation_season, accommodation_status, allowed_number);
			}while(rs.next());
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return result;
	}

}

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
	public Accommodation suggest_accommodation(String local_name, String season) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public int getAllowedMem(int acco_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int allowedMem = -1; // 기본값 (-1: 오류 또는 숙소 없음)

		String sql = "SELECT ALLOWED_NUMBER FROM ACCOMMODATION WHERE ACCOMMODATION_ID = ?";

		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, acco_id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				allowedMem = rs.getInt("ALLOWED_NUMBER");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return allowedMem; // 허용 인원 반환
	}

	@Override
	public boolean memCheck(int num,int allow_mem, int mem) {
		Connection conn = null;
		String s_sql = null;
		PreparedStatement s_pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT ALLOWED_NUMBER FROM ACCOMMODATION WHERE ACCOMMODATION_ID = ?";

		try {
			conn = DBUtil.getConnection();


			s_pstmt = conn.prepareStatement(sql);
			s_pstmt.setInt(1, num);
			rs = s_pstmt.executeQuery();

			if (rs.next()) {
				int allowedNumber = rs.getInt("ALLOWED_NUMBER");
				System.out.println("해당 숙소의 허용 인원: " + allowedNumber + "명");
				if(mem > allowedNumber) {
					System.out.println("인원 초과로 예약이 불과합니다");
					return false;
				}
				else {
					System.out.println("예약 가능");
					return true;


				}
			} else {
				System.out.println("해당 숙소가 존재하지 않습니다.");
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			DBUtil.executeClose(rs, s_pstmt, conn);
		}
		return false;
	}

	@Override
	public boolean openCheck(Accommodation accommodationInfo) {
		// 만약 숙소 인원이 다 차면 숙소 상태 0으로 만들기
		Connection conn = null;
		String sql = null;
		PreparedStatement pstmt = null;
		PreparedStatement i_pstmt = null;
		ResultSet rs = null;
		sql = "SELECT ACCOMMODATION_STATUS FROM ACCOMMODATION WHERE ACCOMMODATION_ID=?";
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, accommodationInfo.getAccommodatin_id());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getInt(1) == 1) {

					System.out.println("운영 가능");
					return true;
				}
				else {
					System.out.println("운영 불가");
					return false;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return false;
	}

}

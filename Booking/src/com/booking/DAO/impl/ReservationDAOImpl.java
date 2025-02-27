package com.booking.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.booking.DAO.ReservationDAO;
import com.booking.dto.Accommodation;
import com.booking.dto.Reservation;
import com.dbutil.DBUtil;
import com.util.Util;

public class ReservationDAOImpl implements ReservationDAO{
	// 시작날짜 ~ 종료날짜까지 예약되어있는 인원수를 반환함 
	@Override
	public List<Integer> getDateRangeReservedNum(int accoId, LocalDate sDate, LocalDate eDate) {
	    List<Integer> result = new ArrayList<>();
	    String sql = "WITH DATE_RANGE AS ( " +
	            "    SELECT R.RESERVATION_START_DATE + LEVEL - 1 AS RESERVATION_DATE, " +
	            "           R.RESERVATION_NUMBER " +
	            "    FROM RESERVATION R " +
	            "    WHERE R.ACCOMMODATION_ID = ? " +
	            "    AND R.RESERVATION_START_DATE <= ? " +
	            "    AND R.RESERVATION_END_DATE >= ? " +
	            "    CONNECT BY LEVEL <= R.RESERVATION_END_DATE - R.RESERVATION_START_DATE + 1 " +
	            "    AND PRIOR R.RESERVATION_ID = R.RESERVATION_ID " +
	            "    AND PRIOR SYS_GUID() IS NOT NULL " +
	            ") " +
	            "SELECT RESERVATION_DATE, SUM(RESERVATION_NUMBER) AS TOTAL_RESERVATION_NUMBER " +
	            "FROM DATE_RANGE " +
	            "WHERE RESERVATION_DATE BETWEEN ? AND ? " +
	            "GROUP BY RESERVATION_DATE " +
	            "ORDER BY RESERVATION_DATE";

	    // 날짜 범위의 시작일과 종료일 사이의 모든 날짜를 순회하기 위한 코드
	    LocalDate startDate = sDate;
	    LocalDate endDate = eDate;

	    // 날짜별 예약 데이터를 저장할 맵
	    Map<LocalDate, Integer> reservationMap = new HashMap<>();

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, accoId);
	        pstmt.setDate(2, java.sql.Date.valueOf(endDate));
	        pstmt.setDate(3, java.sql.Date.valueOf(startDate));
	        pstmt.setDate(4, java.sql.Date.valueOf(startDate));
	        pstmt.setDate(5, java.sql.Date.valueOf(endDate));

	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            LocalDate reservationDate = rs.getDate("RESERVATION_DATE").toLocalDate();
	            int totalReservationNumber = rs.getInt("TOTAL_RESERVATION_NUMBER");
	            reservationMap.put(reservationDate, totalReservationNumber);
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }

	    // 날짜 범위 내에 예약 정보가 없는 날짜에는 0을 추가
	    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
	        result.add(reservationMap.getOrDefault(date, 0));  // 예약 정보가 없으면 0을 추가
	    }

	    return result;
	}



	@Override // 숙소 최대 허용인원
	public int getAllowedNum(int acco_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT ALLOWED_NUMBER FROM ACCOMMODATION "
				+ "WHERE ACCOMMODATION_ID = ?";
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, acco_id);
			rs = pstmt.executeQuery();
			return rs.next() ? rs.getInt(1): -1; 	

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return -1;
	}

	@Override
	public int getAccommodationStatus(int acco_id) {
		Connection conn = null;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql = "SELECT ACCOMMODATION_STATUS FROM ACCOMMODATION WHERE ACCOMMODATION_ID=?";
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, acco_id);
			rs = pstmt.executeQuery();
			pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}else {
				return -1;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return -1;
	}


	@Override
	public int getPrice(int acco_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT ACCOMMODATION_PRICE FROM ACCOMMODATION WHERE ACCOMMODATION_ID=?";
		ResultSet rs = null;
		int price = Integer.MIN_VALUE;

		try {
			conn = DBUtil.getConnection();
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1,acco_id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				price = rs.getInt("ACCOMMODATION_PRICE");
				return price;
			}else {
				price = -1;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return price;
	}

	@Override
	public boolean reservate(String id, int acco_id, LocalDate sDate, LocalDate eDate, int price, int mem) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		int update = Integer.MIN_VALUE;
		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO RESERVATION ("
					+ "RESERVATION_ID, "  // PRIMARY KEY (시퀀스로 자동 증가)
					+ "USER_ID, "
					+ "ACCOMMODATION_ID, "
					+ "RESERVATION_START_DATE, "
					+ "RESERVATION_END_DATE, "
					+ "RESERVATION_PRICE, "
					+ "RESERVATION_NUMBER)"
					+ "VALUES (RESERVATION_SEQ.NEXTVAL, ?, ?, ?, ?, ?,?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setInt(2, acco_id);
			pstmt.setDate(3, java.sql.Date.valueOf(sDate));
			pstmt.setDate(4, java.sql.Date.valueOf(eDate));
			pstmt.setInt(5, price);
			pstmt.setInt(6, mem);

			update = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, update);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return update == 1;
	}


	@Override
	public List<Reservation> getReservationList(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM RESERVATION WHERE USER_ID = ?";
		ResultSet rs = null;
		List<Reservation> result = new ArrayList<>();
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					int reservationId = rs.getInt("RESERVATION_ID");
					String userId = rs.getString("USER_ID");
					int accommodationId = rs.getInt("ACCOMMODATION_ID");
					LocalDate startDate = rs.getDate("RESERVATION_START_DATE").toLocalDate();
					LocalDate endDate = rs.getDate("RESERVATION_END_DATE").toLocalDate();
					int price = rs.getInt("RESERVATION_PRICE");
					System.out.println(price);
					int reservationNumber = rs.getInt("RESERVATION_NUMBER");
					result.add(new Reservation(reservationId, userId, accommodationId, startDate, endDate, price, reservationNumber));
				}while(rs.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	@Override
	public boolean deleteReservation(int reservationID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int update = Integer.MIN_VALUE;
		try {
			conn = DBUtil.getConnection();
			sql = "DELETE RESERVATION WHERE RESERVATION_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reservationID);
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
	public Accommodation suggestAcco(String location_name, String rcmd_season) {
		Connection conn;
		PreparedStatement pstmt;
		String sql;
		ResultSet rs;
		Accommodation result = null;;
		try {

			conn = DBUtil.getConnection();
			sql = "SELECT * FROM ACCOMMODATION WHERE LOCATION_NAME = ? AND RECOMMENDATION_SEASON = ?";
			pstmt = conn.prepareStatement(sql , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, location_name);
			pstmt.setString(2, rcmd_season);
			rs = pstmt.executeQuery();
			int size = 0;
			while(rs.next()) {
				size++;
			}

			if(size == 0) {
				return null;
			}
			int colNum = new Random().nextInt(size)+1;
			rs.absolute(colNum);
			int cnt = 1;
			int accommodatin_id = rs.getInt(cnt++);
			String accommodation_name = rs.getString(cnt++);
			String accommodation_address = rs.getString(cnt++);
			String accommodation_description = rs.getString(cnt++);
			int accommodation_price = rs.getInt(cnt++);
			String location = rs.getString(cnt++);
			String recommendation_season = rs.getString(cnt++);
			int accommodation_status = rs.getInt(cnt++);
			int allowed_number = rs.getInt(cnt++);
			result = new Accommodation(accommodatin_id, accommodation_name, accommodation_address,
					accommodation_description, accommodation_price, location,
					recommendation_season, accommodation_status, allowed_number);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}
}

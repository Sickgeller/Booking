package com.booking.DAO;

import java.io.BufferedReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.booking.dto.Admin;
import com.dbutil.DBUtil;

//import oracle.sql.NUMBER;

public class AccommodationDAO {

	static AdminDAO adminDAO;

	// 숙소 넣기(관리) - 예진

	public void InsertAccommodation(String accommodation_name, String accommodation_address, String accommodation_description,
		    int accommodation_price, String location_name, String recommendation_season, int accommodation_status, int allowed_number) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;
		
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
			int count = pstmt.executeUpdate();
			System.out.println(count + "개의 행을 삽입했습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//자원정리
			DBUtil.executeClose(null, pstmt, conn);
		}
	}//public

	public int selectAccommodation(BufferedReader br) { // 숙소 목록을 띄우는 메서드

		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		int answer = Integer.MIN_VALUE;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT ACCOMMODATION_ID, ACCOMMODATION_NAME FROM ACCOMMODATION ORDER BY ACCOMMODATION_ID";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				List<Integer> idList = new ArrayList<>();
				do {
					int accommodation_id = rs.getInt("ACCOMMODATION_ID");
					idList.add(accommodation_id);
					String accommodation_name = rs.getString("ACCOMMODATION_NAME");
					System.out.printf("숙소번호 : %d , 숙소이름 : %s\n" , accommodation_id, accommodation_name);
				}while(rs.next());
				while(true) {
					try {
						answer = Integer.parseInt(br.readLine());
						if(!idList.contains(answer)) {
							System.out.println("잘못된 입력입니다");
							continue;
						}else {
							break;
						}
					} catch (Exception e) {
						System.out.println("잘못된 입력입니다");
						continue;
					}
				}
			}else {
				System.out.println("등록된 숙소가 없습니다.");
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return answer;
	}


	private boolean checkSuspension(int accommodation_id) { // 입력된 숙소의 ID가 정지상태인지 확인하는 메서드 true
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM ACCOMMODATION WHERE ACCOMMODATION_ID = ? AND ACCOMMODATION_STATUS = 0";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, accommodation_id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true; // 영업정지 상태라면 true 반환
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

	public void accommodation_management(BufferedReader br, Admin admin) {


		int accommodation_id = Integer.MIN_VALUE;

		while(true) {
			System.out.println("관리를 희망하는 숙소를 선택해주세요");
			accommodation_id = selectAccommodation(br);
			if(accommodation_id != 0 || accommodation_id != -1) {
				break;
			}else {
				System.out.println("잘못된 숙소번호입니다.");
				return;
			}
		}


		int answer = Integer.MIN_VALUE;

		while(true) {
			System.out.println("희망하는 처분을 골라주세요");
			System.out.println("1.숙소 영업 정지");
			System.out.println("2.숙소 영업 정지 해제");
			System.out.println("0.뒤로가기");

			try {
				answer = Integer.parseInt(br.readLine());
				if(answer != 1 && answer != 2 && answer != 0) {
					System.out.println("잘못된 입력입니다.");
					continue;
				}else {
					break;
				}
			} catch (Exception e) {
				System.out.println("잘못된 입력입니다.");
				continue;
			}
		}
		if(answer == 0) {
			return;
		}else if(answer == 1) {
			accommodation_suspension(accommodation_id, br, admin);
		}else if(answer == 2) {
			accommodation_resume(accommodation_id,br,admin);
		}

	}

	private void accommodation_resume(int accommodation_id, BufferedReader br, Admin admin) {

		Connection conn = null;
		PreparedStatement pstmtI = null;
		PreparedStatement pstmtU = null;
		String sqlI = null;
		String sqlU = null;

		if(!checkSuspension(accommodation_id)) {
			System.out.println("해당 숙소는 이미 영업가능 상태입니다.");
			return;
		}

		try {
			System.out.println("재개 사유를 입력해주세요");
			String reason = br.readLine();

			conn = DBUtil.getConnection();
			sqlI = "INSERT INTO AMMD_MGMT (AMMD_MGMT_ID, ADMIN_ID, ACCOMMODATION_ID, MGMT_REASON, MGMT_DETAILS)"
					+ " VALUES(AMMD_MGMT_SEQ.NEXTVAL, ? , ?, ?, '영업재개')";
			sqlU = "UPDATE ACCOMMODATION SET ACCOMMODATION_STATUS = 1 WHERE ACCOMMODATION_ID = ?";	
			pstmtI = conn.prepareStatement(sqlI);
			pstmtI.setString(1, admin.getID());
			pstmtI.setInt(2, accommodation_id);
			pstmtI.setString(3, reason);
			int insert = pstmtI.executeUpdate();

			pstmtU = conn.prepareStatement(sqlU);
			pstmtU.setInt(1, accommodation_id);
			int update = pstmtU.executeUpdate();

		} catch (SQLException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
			try {conn.rollback();} catch (SQLException e1) {}
		} finally {
			DBUtil.executeClose(null, pstmtU, conn);
			try {conn.commit();} catch (SQLException e1) {}
			System.out.println(accommodation_id + "번 숙소 영업재개완료");
		}


	}
	private void accommodation_suspension(int accommodation_id, BufferedReader br, Admin admin) { 
		//영업정지 메서드

		Connection conn = null;
		PreparedStatement pstmtI = null;
		PreparedStatement pstmtU = null;
		String sqlI = null;
		String sqlU = null;
		try {
			if(checkSuspension(accommodation_id)) {
				System.out.println("해당 숙소는 이미 영업정지 상태입니다.");
				return;
			}
			System.out.println(accommodation_id+"번 숙소 영업 정지 메뉴입니다");
			System.out.println("영업정지 사유를 입력해주세요");
			String reason = br.readLine();

			conn = DBUtil.getConnection();
			sqlI = "INSERT INTO AMMD_MGMT (AMMD_MGMT_ID, ADMIN_ID, ACCOMMODATION_ID, MGMT_REASON, MGMT_DETAILS)"
					+ " VALUES(AMMD_MGMT_SEQ.NEXTVAL, ? , ?, ?, '영업정지')";
			sqlU = "UPDATE ACCOMMODATION SET ACCOMMODATION_STATUS = 0 WHERE ACCOMMODATION_ID = ?";	
			pstmtI = conn.prepareStatement(sqlI);
			pstmtU = conn.prepareStatement(sqlU);
			pstmtI.setString(1, admin.getID());
			pstmtI.setInt(2, accommodation_id);
			pstmtI.setString(3, reason);
			pstmtU.setInt(1, accommodation_id);

			int insert = pstmtI.executeUpdate();
			int update = pstmtU.executeUpdate();

			if(insert != 1 || update != 1 ) {
				System.out.println("오류가 발생했습니다.");
				try {conn.rollback();} catch (SQLException e) {}
			}else {
				System.out.println(accommodation_id + "번 숙소 영업정지완료");
				try {conn.commit();} catch (SQLException e) {}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		} catch (SQLException e) {
			try {conn.rollback();} catch (SQLException e1) {}
		} finally {
			DBUtil.executeClose(null, pstmtU, conn);
		}


	}
	
public void suggest_accommodation(BufferedReader br, String location_name) throws IOException { // 해외지역에서 호출시 
		
	
	String[] locationNameArr = {
			"서울",
			"경기",
			"전라",
			"강원",
			"충청",
			"경상",
			"제주"
	};
	String[] seasonArr = {"봄","여름","가을","겨울"};
	
	List<String> location_list = new ArrayList<>(Arrays.asList(locationNameArr));
	List<String> season_list = new ArrayList<>(Arrays.asList(seasonArr));
	
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		
		
		if(location_name == null) { // 원하는 지역 입력받는단
			while(true) {
				System.out.println("숙소 추천 입니다.");
				System.out.println("원하시는 국내 지역을 입력해주세요");
				for(String location : location_list) {
					System.out.println(location);
				}
				String tmp = br.readLine();
				if(location_list.contains(tmp)) {
					location_name = tmp;
					break;
				}else {
					System.out.println("유효하지않은 지역입력입니다.");
					continue;
				}
			}
		}
		
		String rcmd_season;
		while(true) {
			System.out.println("추천을 원하시는 계절을 입력해주세요");
			for(String season : season_list) {
				System.out.printf("%s ", season);
			}
			String tmp = br.readLine();
			if(season_list.contains(tmp)) {
				rcmd_season = tmp;
				break;
			}else {
				System.out.println("유효하지않은 입력입니다.");
				continue;
			}
		}
		
		
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
				System.out.println("선택한 조건의 숙소가 없습니다.");
				return;
			}
			int colNum = new Random().nextInt(size)+1;
			rs.absolute(colNum);
			int accd_id = rs.getInt(1);
			String accd_name = rs.getString(2);
			String accd_address = rs.getString(3);
			String accd_description = rs.getString(4);
			int accd_price = rs.getInt(5);
			
			System.out.println("추천 결과입니다.");
			System.out.printf("번호 : %d번 , 숙소이름 : %s , 숙소주소 : %s\n" , accd_id,accd_name,accd_address);
			System.out.println(accd_description);
			System.out.println("가격 : " + accd_price);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

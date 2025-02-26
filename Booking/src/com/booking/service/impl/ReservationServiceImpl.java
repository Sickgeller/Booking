package com.booking.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.booking.DAO.AccommodationDAO;
import com.booking.DAO.ReservationDAO;
import com.booking.DAO.impl.AccommodationDAOImpl;
import com.booking.DAO.impl.ReservationDAOImpl;
import com.booking.dto.Accommodation;
import com.booking.dto.User;
import com.booking.service.ReservationService;
import com.dbutil.DBUtil;

public class ReservationServiceImpl implements ReservationService{

	private BufferedReader br;
	private User user;
	private AccommodationDAO accommodationDAO = new AccommodationDAOImpl();
	private ReservationDAO reservationDAO = new ReservationDAOImpl();
	private int reservation_number = Integer.MIN_VALUE;

	public ReservationServiceImpl(BufferedReader br, User user) {
		super();
		this.br = br;
		this.user = user;
	}

	@Override
	public void overseas_reservation() {
		List<Accommodation> overseaList = accommodationDAO.getOverseasAccommodation();
		List<Integer> idList = new ArrayList<>();
		System.out.println("숙소번호\t숙소이름\t지역\t주소\t운영상태\t예약 가능 인원");
		for(Accommodation accommodation : overseaList) {
			idList.add(accommodation.getAccommodatin_id());
			System.out.print(accommodation.getAccommodatin_id());
			System.out.print("\t");
			System.out.print(accommodation.getAccommodation_name());
			System.out.print("\t");
			System.out.print(accommodation.getLocation_name());
			System.out.print("\t");
			System.out.print(accommodation.getAccommodation_address());
			System.out.print("\t");
			System.out.print(accommodation.getAccommodation_status() == 1 ? "운영중" : "운영정지");
			System.out.print("\t");
			System.out.print(accommodation.getAllowed_number());
			System.out.println();
		}

		int acco_id = Integer.MIN_VALUE;

		while(true) {
			System.out.println("에약하고싶은 숙소의 번호를 고르시오");
			try {
				acco_id = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
			if(idList.contains(acco_id)) {
				break;
			}
		}

		select_overseas(br,"해외",acco_id);


	}

	private boolean select_domestic(int num) {
		LocalDate s_date = null;
		LocalDate e_date = null;

		try {

			// 날짜 체크
			System.out.println("예약하고 싶은 시작 날짜를 입력하세요 (yyyy-MM-dd):");
			LocalDate today = LocalDate.now();
			String input_s_date = br.readLine().trim();
			s_date = LocalDate.parse(input_s_date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			System.out.println("예약하고 싶은 종료 날짜를 입력하세요 (yyyy-MM-dd):");
			String input_e_date = br.readLine().trim();
			e_date = LocalDate.parse(input_e_date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			boolean isDateValid = posDate(s_date, e_date, today); // 날짜 유효성 체크

			// 인원 수 체크
			System.out.println("예약하고 싶은 인원 수를 입력하세요:");
			int mem = Integer.parseInt(br.readLine());
			int allowedMem = accommodationDAO.getAllowedMem(mem);
			boolean isMemValid = accommodationDAO.memCheck(num, mem, allowedMem); // 인원 수 체크


			reservation_number = mem;
			// 운영 여부 체크
			boolean isOpen = accommodationDAO.openCheck(accommodationDAO.getAccommodationInfo(num));

			// 예약 가능 여부 확인
			if (isMemValid && isDateValid && isOpen) {
				System.out.println("예약 가능");
				boolean result = reservationDAO.domestic_reservation(num ,user,s_date,e_date,reservation_number);
				return true;
			} else {
				System.out.println("예약 불가");
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; 
	}

	private boolean select_overseas(BufferedReader br, String string, int acco_id) {


		try {
			// 날짜 체크
			System.out.println("예약하고 싶은 시작 날짜를 입력하세요 (yyyy-MM-dd):");
			LocalDate today = LocalDate.now();
			String input_s_date = br.readLine().trim();
			LocalDate s_date = LocalDate.parse(input_s_date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));


			System.out.println("예약하고 싶은 종료 날짜를 입력하세요 (yyyy-MM-dd):");
			String input_e_date = br.readLine().trim();
			LocalDate e_date = LocalDate.parse(input_e_date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			boolean isDateValid = posDate(s_date, e_date, today); // 날짜 유효성 체크

			// 인원 수 체크
			System.out.println("예약하고 싶은 인원 수를 입력하세요:");
			int mem = Integer.parseInt(br.readLine());
			int allowedMem = accommodationDAO.getAllowedMem(acco_id);
			boolean isMemValid = accommodationDAO.memCheck(acco_id, mem, allowedMem); // 인원 수 체크

			// 운영 여부 체크

			boolean isOpen = accommodationDAO.openCheck(accommodationDAO.getAccommodationInfo(acco_id));

			reservation_number = mem;

			// 예약 가능 여부 확인
			if (isMemValid && isDateValid && isOpen) {
				System.out.println("예약 가능");
				boolean result = reservationDAO.overeas_reservation(acco_id,user,s_date,e_date,reservation_number);
				if(result) {
					System.out.println("예약이 완료되었습니다!");
					return true;
				}else {
					System.out.println("예약 실패 ! ! !");
				}
			} else {
				System.out.println("예약 불가");
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; 
	}


	private boolean posDate(LocalDate s_date, LocalDate e_date,LocalDate today) {

		if (!e_date.isAfter(s_date)) {
			System.out.println("종료 날짜는 시작 날짜(" + s_date + ") 이후여야 합니다. 다시 입력하세요.");
			return false;
		}

		if(!s_date .isAfter(today)) {
			System.out.println(today + "이후 날짜만 예약 가능합니다.");
			return false;

		}

		return true;
	}

	@Override
	public void domestic_reservation() {
		List<Accommodation> domesticAccommodation = accommodationDAO.getDomesticAccommodation();
		System.out.println("숙소번호\t숙소이름\t지역\t주소\t운영상태\t예약 가능 인원");
		List<Integer> idList = new ArrayList<>();
		for(Accommodation accommodation : domesticAccommodation) {
			idList.add(accommodation.getAccommodatin_id());
			System.out.print(accommodation.getAccommodatin_id());
			System.out.print("\t");
			System.out.print(accommodation.getAccommodation_name());
			System.out.print("\t");
			System.out.print(accommodation.getLocation_name());
			System.out.print("\t");
			System.out.print(accommodation.getAccommodation_address());
			System.out.print("\t");
			System.out.print(accommodation.getAccommodation_status() == 1 ? "운영중" : "운영정지");
			System.out.print("\t");
			System.out.print(accommodation.getAllowed_number());
			System.out.println();
		}

		int num = Integer.MIN_VALUE;

		while(true) {
			System.out.println("예약하고 싶은 숙소 번호를 입력하세요:");
			try {
				num = Integer.parseInt(br.readLine());
				if(idList.contains(num)) break;
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
			}
		}

		boolean result = select_domestic(num);

		if(result) {
			System.out.println("예약이 완료되었습니다!");
		}else {
			System.out.println("예약 실패 ! ! !");
		}

	}

	@Override
	public void suggest_accommodation(String location_name) {
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
				String tmp = null;
				try {
					tmp = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
			String tmp = null;
			try {
				tmp = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

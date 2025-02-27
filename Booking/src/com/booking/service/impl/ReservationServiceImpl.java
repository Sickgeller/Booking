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
import com.booking.dto.Reservation;
import com.booking.dto.User;
import com.booking.service.ReservationService;
import com.dbutil.DBUtil;

public class ReservationServiceImpl implements ReservationService{

	private BufferedReader br;
	private User user;
	private AccommodationDAO accommodationDAO = new AccommodationDAOImpl();
	private ReservationDAO reservationDAO = new ReservationDAOImpl();

	public ReservationServiceImpl(BufferedReader br, User user) {
		super();
		this.br = br;
		this.user = user;
	}

	@Override
	public void doReservate(boolean isDomestic) {
		int acco_id = selectAccoId(isDomestic); // 숙소ID입력
		LocalDate sDate = null;
		LocalDate eDate = null;
		while(true) {
			try {
				System.out.println("예약하고 싶은 시작 날짜를 입력하세요 (yyyy-MM-dd):");
				LocalDate today = LocalDate.now();
				String input_s_date = br.readLine().trim();
				sDate = LocalDate.parse(input_s_date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				System.out.println("예약하고 싶은 종료 날짜를 입력하세요 (yyyy-MM-dd):");
				String input_e_date = br.readLine().trim();
				eDate = LocalDate.parse(input_e_date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

				if(posDate(sDate, eDate, today)) {
					break;
				}else {
					System.out.println("유효하지않은 날짜입니다");
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		int mem = Integer.MIN_VALUE;
		while(true) {
			try {
				boolean isValid = true;
				System.out.println("예약 인원 수:");
				mem = Integer.parseInt(br.readLine());
				List<Integer> list = reservationDAO.getDateRangeReservedNum(acco_id, sDate, eDate);
				int limitNum = reservationDAO.getAllowedNum(acco_id);
				System.out.println(Arrays.toString(list.toArray()));
				int day = 0;
				for(int dayRsvPeopleNum : list) {
					int num = limitNum - mem - dayRsvPeopleNum;
					if(num < 0) {
						DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
						System.out.println( sDate.plusDays(day++).format(format) );
						System.out.println("해당날짜의 예약가능인원이 입력한 인원보다 적습니다.");
						System.out.printf("입력된 예약인원수 : %d , 해당날짜의 최대 예약가능인원수 : %d\n",mem, limitNum - dayRsvPeopleNum);
						isValid = false;
					}else {
						day++;
					}
				}
				if(isValid) {
					break;
				}else {
					continue;
				}
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
		}

		int price = reservationDAO.getPrice(acco_id);
		boolean result = reservationDAO.reservate(user.getID(), acco_id, sDate, eDate, price * mem ,mem);
		if(result) {
			System.out.println("예약이 완료되었습니다!");
		}else {
			System.out.println("예약 실패 ! ! !");
		}
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

	private int selectAccoId(boolean isDomestic) {
		List<Accommodation> accommodationList;
		if(isDomestic) { // 국내인경우
			accommodationList = accommodationDAO.getDomesticAccommodation();
		}else { // 해외인경우
			accommodationList = accommodationDAO.getOverseasAccommodation();
		}
		System.out.println("숙소번호\t숙소이름\t지역\t주소\t운영상태\t예약 가능 인원");
		List<Integer> idList = new ArrayList<>();
		for(Accommodation accommodation : accommodationList) {
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
			System.out.println("예약하고 싶은 숙소 번호를 입력하세요:");
			try {
				acco_id = Integer.parseInt(br.readLine());
				if(idList.contains(acco_id)) break;
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
			}
		}
		return acco_id;
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

		Accommodation suggestAcco = reservationDAO.suggestAcco(location_name, rcmd_season);
		System.out.println("추천 결과입니다.");
		System.out.printf("번호 : %d번 , 숙소이름 : %s , 숙소주소 : %s\n" , suggestAcco.getAccommodatin_id(),suggestAcco.getAccommodation_name(),suggestAcco.getAccommodation_address());
		System.out.println(suggestAcco.getAccommodation_address());
		System.out.println("가격 : " + suggestAcco.getAccommodation_price());
	}


	@Override
	public void showReservation() {

	}

	@Override
	public void deleteReservation() {
		int reservationID = Integer.MIN_VALUE;
		List<Reservation> reservList = reservationDAO.getReservationList(user.getID());
		List<Integer> reviewIdList = new ArrayList<>();
		for(Reservation reserv : reservList) {
			System.out.println("-".repeat(20));
			reviewIdList.add(reserv.getReservation_id());
			System.out.printf("%d번 리뷰 / 작성자 : %s / 숙소번호 : %d\n" , reserv.getReservation_id() , reserv.getUser_id() , reserv.getAccomodation_id());
			System.out.printf("예약날짜 %s ~ %s\n", reserv.getReservation_start_date() , reserv.getReservation_end_date());
			System.out.printf("가격 : %d / 인원수 : %d\n" , reserv.getReservation_price() , reserv.getReservation_number());
		}

		while(true) {
			try {
				System.out.println("삭제할 번호를 입력하세요");
				reservationID  = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
			if(reviewIdList.contains(reservationID)) {
				break;
			}else {
				System.out.println("목록에 있는 값을 입력해주세요");
				continue;
			}
		}
		
		boolean result = reservationDAO.deleteReservation(reservationID);
		
		if(result) {
			System.out.println("삭제가 완료되었습니다.");
		}else {
			System.out.println("삭제가 실패했습니다.");
		}
	}
}

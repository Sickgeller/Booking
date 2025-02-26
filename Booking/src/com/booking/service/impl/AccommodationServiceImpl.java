package com.booking.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.booking.DAO.AccommodationDAO;
import com.booking.DAO.impl.AccommodationDAOImpl;
import com.booking.dto.Accommodation;
import com.booking.dto.Admin;
import com.booking.dto.User;
import com.booking.service.AccommodationService;

public class AccommodationServiceImpl implements AccommodationService{

	private Admin admin;
	private BufferedReader br;
	private User user;
	private AccommodationDAO accommodationDAO = new AccommodationDAOImpl();

	public AccommodationServiceImpl(Admin admin, BufferedReader br) {
		this.admin = admin;
		this.br = br;
	}

	public AccommodationServiceImpl(User user, BufferedReader br) {
		this.user = user;
		this.br = br;
	}

	@Override
	public void showEveryAcco() {
		List<Accommodation> accoList = accommodationDAO.getEveryAccommodation();
		for(Accommodation accm : accoList) {
			System.out.printf("숙소번호 : %d , 숙소이름 : %s\n" , accm.getAccommodatin_id(), accm.getAccommodation_name());
		}
	}

	@Override
	public void insertAccommodation() {


		System.out.println("숙소 이름을 입력해주세요");
		String accommodation_name = null;
		try {
			accommodation_name = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("주소를 입력해주세요");
		String accommodation_address = null;
		try {
			accommodation_address = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("숙소 설명을 작성해주세요");
		String accommodation_description = null;
		try {
			accommodation_description = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("숙소의 가격을 입력해주세요");
		int accommodation_price;
		while(true) {
			try {
				accommodation_price = Integer.parseInt(br.readLine());
				if(accommodation_price > 0) {
					break;
				}else {
					System.out.println("가격은 음수나 0원일수 없습니다.");
				}

			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
		}
		System.out.println("속한 도시를 입력해주세요");
		String location_name = null;
		try {
			location_name = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("추천 계절을 입력해주세요");
		String recommendation_season = null;
		try {
			recommendation_season = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("숙소수용가능인웡을 입력해주세요");
		int allowed_number;
		while(true) {
			try {
				allowed_number = Integer.parseInt(br.readLine());
				if(allowed_number > 0) {
					break;
				}else {
					System.out.println("수용가능인원은 음수나 0일수 없습니다.");
				}

			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
		}
		boolean result = accommodationDAO.insertAccommodation(accommodation_name, accommodation_address,
				accommodation_description, accommodation_price, location_name,
				recommendation_season, 1, allowed_number);
		if(result) {
			System.out.println("숙소등록이 완료되었습니다.");
		}else {
			System.out.println("숙소등록이 실패했습니다.");
		}
	}

	@Override
	public Map<List<Integer> , Integer>  showDomesticInfo() {
		List<Accommodation> domesticAccoList = accommodationDAO.getDomesticAccommodation();
		List<Integer> idList = new ArrayList<>();
		Map<List<Integer> , Integer> result = new HashMap<>();
		if(domesticAccoList.isEmpty()) {
			System.out.println("검색된 정보가 없습니다.");
			return null;
		}
		System.out.println("숙소번호\t숙소이름\t지역\t주소\t운영상태\t예약 가능 인원");
		for(Accommodation accommodation : domesticAccoList) {
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
		char answer ='a';
		while(true) {
			System.out.println("숙소 상세정보 보시겠습니까? ( y / n ) ");
			try {
				answer = br.readLine().charAt(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(answer == 'y' || answer == 'n') {
				break;
			}
		}
		
		if(answer == 'y') {
			int accommodation_id = Integer.MIN_VALUE;
			while(!idList.contains(accommodation_id)) {
				System.out.println("숙소번호를 입력해주세요");
				try {
					accommodation_id = Integer.parseInt(br.readLine());
				} catch (NumberFormatException | IOException e) {
					System.out.println("숫자만 입력해주세요");
					continue;
				}
			}
			result.put(idList, accommodation_id);
			showDetailInfo(accommodation_id);
		}else if(answer == 'n') {
			result.put(idList, -1);
			return result;
		}
		return result;
	}

	@Override
	public void showDetailInfo(int accommodation_id) {
		Accommodation accommodation = accommodationDAO.getAccommodationInfo(accommodation_id);
		System.out.println("숙소번호\t숙소이름\t지역\t주소\t운영상태\t예약 가능 인원");
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
	@Override
	public Map<List<Integer> , Integer> showOverseasInfo() {
		List<Accommodation> overseasList = accommodationDAO.getOverseasAccommodation();
		Map<List<Integer> , Integer> result = new HashMap<>();
		System.out.println("숙소번호\t숙소이름\t지역\t주소\t운영상태\t예약 가능 인원");
		List<Integer> idList = new ArrayList<>();
		for(Accommodation accommodation : overseasList) {
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
		char answer ='a';
		while(true) {
			System.out.println("숙소 상세정보 보시겠습니까? ( y / n ) ");
			try {
				answer = br.readLine().charAt(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(answer == 'y' || answer == 'n') {
				break;
			}
		}
		
		if(answer == 'y') {
			int accommodation_id = Integer.MIN_VALUE;
			while(!idList.contains(accommodation_id)) {
				System.out.println("숙소번호를 입력해주세요");
				try {
					accommodation_id = Integer.parseInt(br.readLine());
				} catch (NumberFormatException | IOException e) {
					System.out.println("숫자만 입력해주세요");
					continue;
				}
			}
			result.put(idList, accommodation_id);
			showDetailInfo(accommodation_id);
		}else if(answer == 'n') {
			result.put(idList, -1);
			return result;
		}
		return result;
	}

}

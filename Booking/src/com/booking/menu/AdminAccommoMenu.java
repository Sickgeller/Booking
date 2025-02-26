package com.booking.menu;

import java.io.BufferedReader;

import com.booking.DAO.AccommodationDAO;
import com.booking.dto.Admin;
import com.booking.service.AccoMgmtService;
import com.booking.service.AccommodationService;
import com.booking.service.impl.AccoMgmtServiceImpl;
import com.booking.service.impl.AccommodationServiceImpl;
import com.util.Util;

public class AdminAccommoMenu {
	
	private BufferedReader br;
	private Admin admin;
	private AccoMgmtService accoMgmtService;
	private AccommodationService accommodationService;
	
	AdminAccommoMenu(BufferedReader br, Admin admin){
		this.br = br;
		this.admin = admin;
		accoMgmtService = new AccoMgmtServiceImpl(admin, br);
		accommodationService = new AccommodationServiceImpl(admin, br);
		menu();
	}
	
	private void menu(){
		int answer = Integer.MIN_VALUE;
		while(true) {
			System.out.println("숙소관리 메뉴입니다.");
			System.out.println("원하시는 메뉴를 선택해주세요");
			System.out.println("1. 숙소 관리");
			System.out.println("2. 숙소 등록");
			System.out.println("0. 뒤로가기");
			try {
				answer = Integer.parseInt(br.readLine());
				if(Util.checkValidNum(answer, 1,2,0)) {
					break;
				}else {
					System.out.println("1,2,0중 하나를 입력해주세요");
					continue;
				}
			} catch (Exception e) {
				System.out.println("잘못된 입력값입니다.");
				continue;
			}
		}
		if(answer == 1) {
			accommodation_management();
		}
		else if(answer == 2 ) {
			accommodationService.insertAccommodation();
		}
	}

	private void accommodation_management() {
		int answer = Integer.MIN_VALUE;
		while(true) {
			System.out.println("희망하는 처분을 골라주세요");
			System.out.println("1.숙소 영업 정지");
			System.out.println("2.숙소 영업 정지 해제");
			System.out.println("0.뒤로가기");

			try {
				answer = Integer.parseInt(br.readLine());
				if(Util.checkValidNum(answer, 1,2,0)){
					break;
				}else {
					System.out.println("1,2,0 중 하나를 입력해주세요");
					continue;
				}
			} catch (Exception e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
		}
		
		if(answer == 1) {
			accoMgmtService.accommodation_suspension();
		}else if(answer == 2) {
			accoMgmtService.accommodation_resume();
		}else if(answer == 0) {
			return;
		}
	}

}

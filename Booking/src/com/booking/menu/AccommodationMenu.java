package com.booking.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.booking.dto.User;
import com.booking.service.AccommodationService;
import com.booking.service.impl.AccommodationServiceImpl;
import com.util.Util;

public class AccommodationMenu { 

	private BufferedReader br;
	private User user;
	private AccommodationService accommodationService;

	public AccommodationMenu(BufferedReader br, User user){
		this.br = br;
		this.user = user;
		this.accommodationService = new AccommodationServiceImpl(user, br);
		menu();
	}

	// 숙소메뉴 호출 함수
	public void menu(){

		//1. 숙소보기
		int no = Integer.MIN_VALUE;
		while (true) {
			System.out.println("원하는 항목을 선택하세요.");
			System.out.println("1. 전체 숙소 보기");
			System.out.println("2. 예약하기");
			System.out.println("0. 뒤로가기");

			try {
				no = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
			if(Util.checkValidNum(no, 1,2,0)) {
				break;
			}else {
				System.out.println("1,2,0중 하나를 입력해주세요");
				continue;
			}
		}
		if(no==1) {
			showEveryAccommodation();
		}else if(no == 2) {
			new ReservationMenu(user, br);
		}

	} // class

	private void showEveryAccommodation(){
		int num = Integer.MIN_VALUE;
		while(true) {
			System.out.println("1.국내 2.해외 0.뒤로가기");
			try {
				num = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
			if(Util.checkValidNum(num, 1,2,0)) {
				break;
			}else {
				System.out.println("1,2,0중 하나를 입력해주세요");
			}
		}
		if(num == 1) {
			accommodationService.domestic();
		}else if(num == 2) {
			accommodationService.oversea();
		}else if(num == 0) {
			return;
		}
	}


}
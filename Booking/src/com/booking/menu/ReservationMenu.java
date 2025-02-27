package com.booking.menu;

import java.io.BufferedReader;
import java.io.IOException;

import com.booking.dto.User;
import com.booking.service.ReservationService;
import com.booking.service.impl.ReservationServiceImpl;
import com.util.Util;

public class ReservationMenu {

	private User user;
	private BufferedReader br;
	private ReservationService reservationService;

	public ReservationMenu(User user, BufferedReader br) {
		super();
		this.user = user;
		this.br = br;
		reservationService = new ReservationServiceImpl(br, user);
		menu();
	}

	private void menu() {
		int menuNum = Integer.MIN_VALUE;
		while(true) {
			System.out.println("에약하기");
			System.out.println("1. 국내");
			System.out.println("2. 해외");
			System.out.println("3. 예약 관리");
			System.out.println("0. 뒤로 가기");

			try {
				menuNum = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
			if(Util.checkValidNum(menuNum, 1,2,3,0)) {
				break;
			}else {
				System.out.println("1,2,3,0중 하나만 입력해주세요");
			}
		}

		if(menuNum == 1) {
			reservateDomestic();
		}else if(menuNum == 2) {
			reservateOverseas();
		}else if(menuNum == 3) {
			reservationManagement();
		}else if(menuNum == 0) {

		}
	}

	private void reservationManagement() {
		int answer = Integer.MIN_VALUE;
		while(true) {
			System.out.println("1. 예약 목록 조회 및 결제");
			System.out.println("2. 예약 삭제");
			System.out.println("0. 뒤로가기");
			try {
				answer = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
			if(Util.checkValidNum(answer, 1,2,0)) {
				break;
			}else {
				System.out.println("1,2,0중 하나를 입력해주세요");
				continue;
			}
		}
		
		if(answer == 1) {
			new PaymentMenu(br, user);
		}else if(answer == 2) {
			reservationService.deleteReservation();
		}else if(answer == 0) {
			return;
		}
	}

	private void reservateOverseas() {
		int answer = Integer.MIN_VALUE;
		while(true) {
			System.out.println("1. 해외 여행지");	
			System.out.println("2. 추천여행지 보기");
			System.out.println("0. 뒤로가기");
			try {
				answer = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				e.printStackTrace();
			}
			if(Util.checkValidNum(answer, 1,2,0)) {
				break;
			}else {
				System.out.println("1,2,0중 하나를 입력해주세요");
			}
		}

		if(answer == 1) {
			reservationService.doReservate(false);
		}else if(answer == 2) {
			reservationService.suggest_accommodation("해외");
		}else if(answer == 0) {
			return;
		}
	}

	private void reservateDomestic() {
		int answer = Integer.MIN_VALUE;
		while(true) {
			System.out.println("1. 국내 여행지");	
			System.out.println("2. 추천여행지 보기");
			System.out.println("0. 뒤로가기");
			try {
				answer = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				e.printStackTrace();
			}
			if(Util.checkValidNum(answer, 1,2,0)) {
				break;
			}else {
				System.out.println("1,2,0중 하나를 입력해주세요");
			}
		}

		if(answer == 1) {
			reservationService.doReservate(true);
		}else if(answer == 2) {
			reservationService.suggest_accommodation(null);
		}else if(answer == 0) {
			return;
		}

	}

}

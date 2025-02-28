package com.booking.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.booking.dto.User;
import com.booking.service.PaymentService;
import com.booking.service.ReservationService;
import com.booking.service.UserService;
import com.booking.service.impl.PaymentServiceImpl;
import com.booking.service.impl.ReservationServiceImpl;
import com.booking.service.impl.UserServiceImpl;
import com.util.Util;

public class PaymentMenu {

	private BufferedReader br;
	private User user;
	private PaymentService paymentService;
	private ReservationService reservationService;
	private UserService userService = new UserServiceImpl(br);

	public PaymentMenu(BufferedReader br, User user) {
		super();
		this.br = br;
		this.user = user;
		reservationService = new ReservationServiceImpl(br, user);
		paymentService = new PaymentServiceImpl(br, user);
		menu();
	}

	private void menu() {
		int answer = Integer.MIN_VALUE;

		while(true) {
			System.out.println("결제 메뉴입니다.");
			System.out.println("1. 예약 현황 조회");
			System.out.println("2. 결제 내역 조회");
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
				System.out.println("1,2,0 중 하나만 입력해주세요");
			}
		}

		if(answer == 1) {
			doPayMenu();

		}else if(answer == 2) { 
			paymentService.showMyPayment();
		}else if(answer == 0) {
			return; 
		}

	}

	private void doPayMenu() {

		//1번 고른 예약번호 2번보유현금 3번숙소가격
		int paymentMethod = Integer.MIN_VALUE;
		while(true) {
			System.out.println("결제 방법을 선택");
			System.out.println("1. 현금");
			System.out.println("2. 현금 + 포인트");
			System.out.println("뒤로 가기");
			try {
				paymentMethod  = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력하세요");
				continue;
			}
			if(Util.checkValidNum(paymentMethod, 1,2)) {
				break;
			}
		}

		if(paymentMethod == 1) {
			paymentService.processPayment();
		}else if(paymentMethod == 2) {
			paymentService.paymentWithPoint();
		}else if(paymentMethod == 3) {
			callDoCharge();
		}else if(paymentMethod == 0) {
			return;
		}

	}

	private void callDoCharge() {
		int answerCharge = Integer.MIN_VALUE;
		while(true) {
			System.out.println("충전하시겠습니까? (1.예 / 2.아니오");
			try {
				Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
			}
			if(Util.checkValidNum(answerCharge, 1,2)) {
				break;
			}
		}
		if(answerCharge == 1) {
			chargeMoney(answerCharge);
		}else {
			System.out.println("결제가 취소되었습니다.");
			return;
		}
	}

	private void chargeMoney(int answerCharge) {
		int chargeMoney = 0;
		while(true) {
			try {
				System.out.println("충전할 금액을 입력하세요");
				chargeMoney = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력하세요");
				continue;
			}
			userService.chargeAccount(user.getID(), chargeMoney);
		}
	}


}

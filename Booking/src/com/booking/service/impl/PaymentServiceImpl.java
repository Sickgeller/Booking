package com.booking.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.List;

import com.booking.DAO.PaymentDAO;
import com.booking.DAO.impl.PaymentDAOImpl;
import com.booking.dto.Reservation;
import com.booking.dto.User;
import com.booking.service.PaymentService;
import com.booking.service.ReservationService;

public class PaymentServiceImpl implements PaymentService{

	private BufferedReader br;
	private User user;
	private ReservationService reservationService = new ReservationServiceImpl(br, user);
	private PaymentDAO paymentDAO = new PaymentDAOImpl(user);

	public PaymentServiceImpl(BufferedReader br, User user) {
		super();
		this.br = br;
		this.user = user;
	}
	@Override
	public List<Integer> selectPaymentTarget() {
		List<Integer> result = new ArrayList<>();
		Reservation selectedReservation = reservationService.showAndSelectReservation();
		result.add(selectedReservation.getReservation_id());
		result.add(user.getCash());
		result.add(selectedReservation.getPrice());
		return result;
	}
	@Override
	public boolean processPayment() {
		List<Integer> list = selectPaymentTarget(); 
		int reservationId = list.get(1);
		int userCash = list.get(2);
		int reservationPrice = list.get(3);
		boolean updateResult = false;
		if(userCash > reservationPrice) {
			System.out.println("결제 진행합니다.");
			updateResult = paymentDAO.updateCashPayment(userCash-reservationPrice);
			System.out.println("============================================================");
			System.out.println("👤 사용자 ID : " + user.getID());
			System.out.println("💰 보유 금액 : " + user.getCash());
			System.out.println("============================================================");
			updateResult = true;
		}else {
			int requiredCash = reservationPrice-userCash;
			System.out.printf("잔액이 부족하여 결제할 수 없습니다. 추가로 %d원이 필요합니다.\n", requiredCash);
		}
		return false;
	}
	@Override
	public boolean paymentWithPoint() {
		List<Integer> list = selectPaymentTarget(); 
		int reservationId = list.get(1);
		int userCash = list.get(2);
		int reservationPrice = list.get(3);

		System.out.printf("숙소 가격: %d원\n", reservationPrice);
		System.out.println("현재 보유한 원화입니다 : " + user.getCash() );
		System.out.println("현재 보유한 포인트입니다 : " + user.getPoint());
		System.out.println("사용할 포인트를 입력하세요");
		
		int point = 0;
		while(true) {
			try {
				point = Integer.parseInt(br.readLine());
				if(point <= user.getPoint()) break;
				else System.out.println("범위 밖의 값입니다.");
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력하세요");
				continue;
			}
		}
		if(userCash < reservationPrice - point ) {
            System.out.println("결제 금액이 모자랍니다");
            System.out.println("모자란 금액 : " + ((reservationPrice - point) - userCash));
            return false;
		}else {
			System.out.println("결제 진행합니다.");
			paymentDAO.updateCashPayment(userCash-reservationPrice);
			System.out.println("============================================================");
			System.out.println("👤 사용자 ID : " + user.getID());
			System.out.println("💰 보유 금액 : " + user.getCash());
			System.out.println("============================================================");
			return true;
		}
	}

}

package com.booking.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.booking.DAO.PaymentDAO;
import com.booking.DAO.impl.PaymentDAOImpl;
import com.booking.dto.Payment;
import com.booking.dto.Reservation;
import com.booking.dto.User;
import com.booking.service.PaymentService;
import com.booking.service.ReservationService;

public class PaymentServiceImpl implements PaymentService{

	private BufferedReader br;
	private User user;
	private ReservationService reservationService;
	private PaymentDAO paymentDAO;

	public PaymentServiceImpl(BufferedReader br, User user) {
		super();
		this.br = br;
		this.user = user;
		reservationService = new ReservationServiceImpl(br, user);
		paymentDAO = new PaymentDAOImpl(user);
	}
	@Override
	public List<Integer> selectPaymentTarget() {
		List<Integer> result = new ArrayList<>();
		Reservation selectedReservation = selectUnpaidReservation();
		result.add(selectedReservation.getReservation_id());
		result.add(user.getCash());
		result.add(selectedReservation.getReservation_price());
		return result;
	}
	private Reservation selectUnpaidReservation() {
		List<Reservation> unpaidReserv = paymentDAO.getUnpaidReservation(user.getID());
		List<Integer> idList = new ArrayList<>();
		for(Reservation reserv : unpaidReserv) {
			idList.add(reserv.getReservation_id());
			System.out.println("============================================================");
			System.out.println("예약 번호 : " + reserv.getReservation_id());
			System.out.println("사용자 ID : " + reserv.getUser_id());
			System.out.println("숙소 번호 : " + reserv.getAccomodation_id());
			System.out.println("예약 시작일 : " + reserv.getReservation_start_date());
			System.out.println("예약 종료일 : " + reserv.getReservation_end_date());
			System.out.println("가격 : " + reserv.getReservation_price());
			System.out.println("예약 인원 : " + reserv.getReservation_number() + "명");
			System.out.println("============================================================");
		}
		int answer = Integer.MIN_VALUE;
		
		while(true) {
			System.out.println("예약 번호를 입력해주세요");
			try {
				answer = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
			if(idList.contains(answer)) {
				break;
			}else {
				System.out.println("목록에 있는 id를 입력해주세요");
			}
		}
		for(Reservation reserv : unpaidReserv) {
			if(reserv.getReservation_id() == answer) return reserv;
		}
		return null;
	}
	@Override
	public boolean processPayment() {
		List<Integer> list = selectPaymentTarget(); 
		int reservationId = list.get(0);
		int userCash = list.get(1);
		int reservationPrice = list.get(2);
		if(userCash > reservationPrice) {
			System.out.println("결제 진행합니다.");
			paymentDAO.updateCashPayment(userCash-reservationPrice);
			System.out.println("============================================================");
			
			System.out.println("👤 사용자 ID : " + user.getID());
			System.out.println("💰 보유 금액 : " + user.getCash());
			System.out.println("============================================================");
			boolean result = paymentDAO.insertPayment(reservationId, reservationPrice , 0, reservationPrice , 1);
			if(result) {
				System.out.println("결제가 완료되었습니다.");
				return true;
			}else {
				System.out.println("결제가실패했습니다.");
				return false;
			}
		}else {
			int requiredCash = reservationPrice-userCash;
			System.out.printf("잔액이 부족하여 결제할 수 없습니다. 추가로 %d원이 필요합니다.\n", requiredCash);
		}
		return false;
	}
	@Override
	public boolean paymentWithPoint() {
		List<Integer> list = selectPaymentTarget(); 
		int reservationId = list.get(0);
		int userCash = list.get(1);
		int reservationPrice = list.get(2);

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
		int priceWithPoint = reservationPrice - point;
		if(userCash < priceWithPoint ) {
			System.out.println("결제 금액이 모자랍니다");
			System.out.println("모자란 금액 : " + (priceWithPoint - userCash));
			return false;
		}else {
			System.out.println("결제 진행합니다.");
			paymentDAO.updateCashPayment(userCash - priceWithPoint);
			System.out.println("============================================================");
			System.out.println("👤 사용자 ID : " + user.getID());
			System.out.println("💰 보유 금액 : " + user.getCash());
			System.out.println("============================================================");
			boolean result = paymentDAO.insertPayment(reservationId, priceWithPoint , point, reservationPrice , 2);
			if(result) {
				System.out.println("결제가 완료되었습니다.");
				return true;
			}else {
				System.out.println("결제가 실패했습니다.");
				return false;
			}
		}
	}
	@Override
	public void showMyPayment() {
		List<Payment> paymentList =  paymentDAO.getPaymentHistory();
		if(paymentList.isEmpty()) System.out.println("결제 내역이 없습니다.");
		System.out.println("=============================");
		for(Payment payment : paymentList) {
			System.out.println("번호\t결제유저\t결제 진행한 예약 ID\t사용한 현금 \t결제날짜");
			System.out.print(payment.getPayment_id());
			System.out.print("      ");

			System.out.print(payment.getUser_id());
			System.out.print("     ");
			System.out.print(payment.getReservation_id());
			System.out.print("           ");
			System.out.print(payment.getPayment_total_price());
			System.out.print("            ");
			System.out.print(payment.getPayment_date());
			System.out.println();
		}
		System.out.println("=============================");

	}

}

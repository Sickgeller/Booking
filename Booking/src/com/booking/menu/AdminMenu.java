package com.booking.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.booking.DAO.AccommodationDAO;
import com.booking.DAO.CouponDAO;
import com.booking.DAO.QnADAO;
import com.booking.DAO.impl.QnADAOImpl;
import com.booking.dto.Admin;
import com.booking.dto.Coupon;
import com.booking.dto.QNA;
import com.booking.dto.User;
import com.booking.service.QnAService;
import com.booking.service.impl.QnAServiceImpl;

public class AdminMenu { 
	// 어드민 메뉴 카테고리
	// 파라미터들 정리해놓음
	private Admin admin;
	private BufferedReader br;
	private User user;
	private Coupon coupon;
	private QnAService qnaService;

	public AdminMenu(BufferedReader br, Admin admin,User user,Coupon coupon){
		this.br = br;
		this.admin = admin;
		this.user = user;
		this.coupon = coupon;

		QnADAO qnaDAO = new QnADAOImpl(admin);
		this.qnaService = new QnAServiceImpl(qnaDAO, br);

		menu();
	}


	public void menu() {
		while(true) {
			try {
				System.out.println("관리자 메뉴입니다.");
				System.out.println("원하시는 항목을 골라주세요");
				System.out.println("1.숙소 관리");
				System.out.println("2.문의 관리 페이지");
				System.out.println("3.쿠폰 관리 페이지");
				System.out.println("0.로그아웃");
				int answer = Integer.parseInt(br.readLine());

				if(answer == 1) {
					accommodationAdmin();
				}else if(answer == 2) {
					qnaManagement();
				}else if(answer == 3) {
					couponManagement();


				}else if(answer == 0){
					return;
				}else {
					System.out.println("잘못된 입력입니다");
					continue;
				}
			}
			catch (NumberFormatException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void accommodationAdmin() {
		AccommodationDAO accommodationDAO = new AccommodationDAO();
		int answer = Integer.MIN_VALUE;

		while(true) {
			System.out.println("숙소관리 메뉴입니다.");
			System.out.println("원하시는 메뉴를 선택해주세요");
			System.out.println("1. 숙소 관리");
			System.out.println("2. 숙소 등록");
			System.out.println("0. 뒤로가기");
			try {
				answer = Integer.parseInt(br.readLine());
				if(answer != 1 && answer != 2 && answer != 0) {
					System.out.println("유효하지않은 입력값입니다.");
					continue;
				}
				break;
			} catch (Exception e) {
				System.out.println("잘못된 입력값입니다.");
				continue;
			}
		}

		// 입력받아와서 처리
		if(answer == 1) accommodationDAO.accommodation_management(br, admin);
		else if(answer == 2 ) accommodation_insert();
		else if (answer == 0) return;
	}

	private void accommodation_insert() { // 만들다 말았음 숙소 INSERT 부분

	}

	private void qnaManagement() { // 문의 메뉴
		int answer = Integer.MIN_VALUE;

		while(true) {
			System.out.println("문의 관련 페이지 입니다.");
			System.out.println("1. 미답변 QnA 답변하기");
			System.out.println("2. 답변한 QnA 수정하기");
			System.out.println("3. QnA 전체보기");
			System.out.println("0. 뒤로가기");

			try {
				answer = Integer.parseInt(br.readLine());
				if(answer != 1 && answer != 2 && answer != 3 && answer != 0) {
					System.out.println("유효하지않은 입력입니다.");
					continue;
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("숫자만 입력해주세요.");
				continue;
			}
		}

		if(answer == 1) {
			qnaService.answerToQNA(admin);
		} else if(answer == 2) {
			// 답변 수정 처리
			qnaService.updateQNA(admin);
		
		} else if(answer == 3) {
			qnaService.showAllQNA();
		}
	}
	private void couponManagement() { // 쿠폰 관리 메뉴


		// 쿠폰 종류 조회
		try {

			System.out.println("쿠폰을 관리하는 페이지입니다.");
			System.out.println("1. 쿠폰 종류 조회");
			System.out.println("2. 쿠폰 등록");
			System.out.println("3. 신규 사용자에게 기본 쿠폰 발급");
			System.out.println("4. 사용자에게 쿠폰 발급");


			int num = Integer.parseInt(br.readLine());
			try{
				CouponDAO coupondao = new CouponDAO();


				if(num == 1) {
					System.out.println("쿠폰 종류 조회");

					coupondao.showAllCoupon(admin.getID());
				}else if(num == 2) 
				{
					System.out.println("쿠폰 등록");
					coupondao.reg_coupon(br);
				}
				else if(num == 3) {
					System.out.println("신규 사용자에게 기본 쿠폰 발급");


					coupondao.giveNewUserCoupon(coupondao.getDefaultCouponID());
				}
				else if(num == 4) {
					System.out.println("사용자에게 쿠폰 발급");
					//coupondao.giveCouponUser(admin);
				}
			}catch (Exception e) {
				// TODO: handle exception
			}

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

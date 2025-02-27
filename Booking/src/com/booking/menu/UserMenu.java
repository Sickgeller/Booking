package com.booking.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

import com.booking.DAO.CashDAO;
import com.booking.DAO.CouponDAO;
import com.booking.DAO.ReviewDAO;
import com.booking.DAO.UserDAO;
import com.booking.DAO.impl.ReviewDAOImpl;
import com.booking.dto.Review;
import com.booking.dto.User;
import com.booking.service.ReviewService;
import com.booking.service.UserService;
import com.booking.service.impl.ReviewServiceImpl;
import com.booking.service.impl.UserServiceImpl;
import com.util.Util;

public class UserMenu {

	private BufferedReader br = null;
	private User user;
	private ReviewMenu reviewMenu;
	private UserService userService;
	private ReviewService reviewService;

	public UserMenu(BufferedReader br,User user){
		this.br = br;
		this.user = user;
		reviewMenu = new ReviewMenu(br);
		reviewService = new ReviewServiceImpl(br);
		userService= new UserServiceImpl(br);
		userMenu();
	}

	private void userMenu() {
		while(true) {
			
			System.out.println("우와! 환영합니다! 😊 우와놀자에서 최고의 여행을 경험하세요!");
			System.out.println("원하시는 항목을 선택하세요 ! ! !");
			System.out.println("1. 숙소 관련");	
			System.out.println("2. 마이페이지");
			System.out.println("3. 문의하기");
			System.out.println("4. 뒤로 가기");
			System.out.println("0. 로그아웃");
			int num = Integer.MIN_VALUE;

			while(true) {
				try {
					num = Integer.parseInt(br.readLine());
					if(Util.checkValidNum(num, 1,2,3,4,0)) {
						break;
					}else {
						System.out.println("1,2,3,4,0 중 하나를 선택해주세요");
						continue;
					}
				} catch (NumberFormatException | IOException e) {
					e.printStackTrace();
				}
			}

			if(num == 1) {
				System.out.println("\n숙소 관련");
				System.out.println("숙소 관련 메뉴 입니다.");
				new AccommodationMenu(br, user);

			}else if(num == 2) {
				System.out.println("\n마이페이지");
				U_Menu();
			}
			else if(num == 3) { 
				System.out.println("문의하기");
				new UserQnAMenu(br,user);
			}
			else if(num == 4) {
				System.out.println("🔙 뒤로 가기 완료!");
				return;

			}else if (num == 0) {
				System.out.println("로그아웃 완료");
				break;
			}
		}

	}


	private void U_Menu()  {
		// 사용자 정보 메뉴 
		int no = Integer.MIN_VALUE;
		String ID = null;

		while(true) {
			System.out.println("1. 회원 정보 변경");
			System.out.println("3. 등급 확인");
			System.out.println("4. 금액 충전");
			System.out.println("5. 작성 리뷰 내역");
			System.out.println("6. 쿠폰 확인");
			System.out.println("7. 로그아웃");
			System.out.println("8. 회원 탈퇴");
			System.out.println("0. 뒤로 가기");
			ID = user.getID();
			try {
				no = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
			if(Util.checkValidNum(no, 1,2,3,4,5,6,7,8,0)) {
				break;
			}else {
				System.out.println("유효하지않은 입력입니다. 1,2,3,4,5,6,7,8,0 중 하나를 입력해주세요");
			}
		}

		if(no == 1) {
			changeUserInfo(ID);
		}else if(no == 3) {// 등급확인 메서드
			userService.checkUserGrade(ID); 
			
		}else if(no == 4) { // 금액충전메서드
			userService.chargeAccount(ID, user.getCash());
			
		}else if(no == 5) {
			while(true) {
				try {
					System.out.println("작성 리뷰 내역 확인");
					System.out.println("리뷰 관리하시겠습니까? ( y / n )");
					char answer = br.readLine().charAt(0);
					if(answer == 'y') {
						reviewMenu.reviewManagement(user); // ReviewMenu로 통합
						break;
					}else if(answer == 'n') {
						break;
					}
				} catch (IOException |InputMismatchException | IllegalArgumentException | StringIndexOutOfBoundsException e) {
					e.printStackTrace();
					System.out.println("y/n글자만 입력하세요");
					continue;
				}
			}
		}else if(no == 6) {
			System.out.printf("%s 님이 보유하신 쿠폰입니다 \n", ID);
			userService.showUserCoupon(ID);

		}else if(no == 7) {
			System.out.println("🚪 로그아웃되었습니다. 프로그램을 종료합니다.");
			System.exit(0); // 프로그램 완전 종료

		}else if(no == 8) {
			userService.deleteUser(user.getID());
		}

	} // userMenu	

	private void changeUserInfo(String ID) {
		int num1 = Integer.MIN_VALUE;
		while(true) {
			System.out.println("회원 정보 변경");
			System.out.println("변경하고 싶은 정보를 선택하세요.(숫자)");
			System.out.println("1.이름 2.이메일 3.비밀번호 0.뒤로가기");
			try {
				num1 = Integer.parseInt(br.readLine());
				if(Util.checkValidNum(num1, 1,2,3,0)) {
					break;
				}else {
					System.out.println("유효하지않은 입력입니다. 1,2,3,0중 하나를 입력해주세요");
				}
			}catch(Exception e){
				System.out.println("오로지. 오직. 무조건. [숫자]만 입력하세요");
				continue;
			} // catch
		}
		if(num1 == 1) { // 이름변경
			userService.changeUserName(ID);
		}else if(num1 == 2) { // 이메일 변경
			userService.changeUserEmail(ID);
		}else if(num1 == 3) { // 비밀번호 변경 메서드
			userService.changeUserPW(ID);
		}else if(num1 == 0) {
		}
	}

}







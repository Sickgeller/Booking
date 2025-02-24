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

public class UserMenu {

	private BufferedReader br = null;
	private User user;
	private ReviewMenu reviewMenu;
	private UserService userService;

	static Review review;
	static UserDAO userDAO;
	static CashDAO cashDAO;
	static ReviewDAOImpl reviewDAO;
	static CouponDAO couponDAO;
	private ReviewService reviewService;

	public UserMenu(BufferedReader br,User user){
		this.br = br;
		this.user = user;
		reviewMenu = new ReviewMenu(br);
		ReviewDAO reviewDAO = new ReviewDAOImpl();
		reviewService = new ReviewServiceImpl(reviewDAO, br);
		userService= new UserServiceImpl(br);
		userMenu();
	}

	private void userMenu() {
		System.out.println("로그인이 완료되었습니다.");
		System.out.println("우와! 환영합니다! 😊 우와놀자에서 최고의 여행을 경험하세요!");
		System.out.println("원하시는 항목을 선택하세요 ! ! !\n");
		System.out.println("1. 숙소 예약");	
		System.out.println("2. 마이페이지");
		System.out.println("3. 문의하기");
		System.out.println("4. 뒤로 가기");
		System.out.println("0. 로그아웃");
		List<Integer> answer = new ArrayList<>();
		answer.addAll(Arrays.asList(1, 2, 3, 4, 0));
		int num = Integer.MIN_VALUE;
		while(true) {
			try {
				num = Integer.parseInt(br.readLine());
				if(answer.contains(num)) {
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
			System.out.println("\n숙소 예약");
			System.out.println("숙소 메뉴 입니다.");
			new AccommodationMenu(br);

		}else if(num == 2) {
			System.out.println("\n마이페이지");
			U_Menu();
		}
		else if(num == 3) { 
			System.out.println("문의하기");
		}
		else if(num == 4) {
			System.out.println("🔙 뒤로 가기 완료!");
			return;

		}else if (num == 0) {
			System.out.println("로그아웃 완료");
			return;
		}

	}


	public void U_Menu()  {
		// 사용자 정보 메뉴 
		try {
			while(true) {
				System.out.println("1. 회원 정보 변경");
				System.out.println("2. 비밀번호 변경");
				System.out.println("3. 등급 확인");
				System.out.println("4. 금액 충전");
				System.out.println("5. 작성 리뷰 내역");
				System.out.println("6. 쿠폰 확인");
				System.out.println("7. 로그아웃");
				System.out.println("8. 회원 탈퇴");

				String ID = user.getID();

				int no = Integer.parseInt(br.readLine());

				if(no == 1) {
					while(true) {
						System.out.println("회원 정보 변경");
						System.out.println("변경하고 싶은 정보를 선택하세요.(숫자)");
						System.out.println("1.이름 2.이메일 3.비밀번호 0.뒤로가기");

						int num1 = Integer.parseInt(br.readLine());

						try {
							if(num1 == 1) { // 이름변경
								userService.changeUserName(ID);
								break;
							}else if(num1 == 2) { // 이메일 변경
								userService.changeUserEmail(ID);
								break;
							}else if(num1 == 3) { // 비밀번호 변경 메서드
								userService.changeUserPW(ID);
								break;
							}else if(num1 == 0) {
								return;
							}else if(num1 != 1 && num1 != 2 && num1 != 3) { // 유효한 숫자범위 검사부분
								System.out.println("1 ~ 2 의 숫자를 입력하세요");
								continue;
							} // if
						}catch(Exception e) { // 숫자검사 입력부분
							System.out.println("오로지. 오직. 무조건. [숫자]만 입력하세요");
							continue;
						} // catch
					}
				}
				else if(no == 3) {// 등급확인 메서드
					userService.checkUserGrade(ID); 

				}else if(no == 4) { // 금액충전메서드
					try {
						System.out.println("금액 충전");
						int cash = user.getCash();
						System.out.println("충전할 금액을 입력하세요.");
						cash = Integer.parseInt(br.readLine());
						cashDAO.chargeCash(ID, cash, br);
					}catch(NumberFormatException e) {
						e.printStackTrace();
						System.out.println("숫자만 입력하세요 ");
					}

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
						} catch (InputMismatchException | IllegalArgumentException | StringIndexOutOfBoundsException e) {
							e.printStackTrace();
							System.out.println("y/n글자만 입력하세요");
							continue;
						} 
					}
				}else if(no == 6) {



				}else if(no == 7) {
					System.out.println("🚪 로그아웃되었습니다. 프로그램을 종료합니다.");
					System.exit(0); // 프로그램 완전 종료

				}else if(no == 8) {
					userService.deleteUser(user.getID());
				}
				else if(no > 8 ) { 
					System.out.println("1 ~ 8 의 숫자를 입력하세요");
					continue;
				}
			}

		}catch(NumberFormatException | IOException e) {
			e.printStackTrace();	

		}catch(Exception e) {
			System.out.println("오류발생");

			e.printStackTrace();
		}
	} // userMenu	


}







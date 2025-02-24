package com.booking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.booking.DAO.AccommodationviewDAO;
import com.booking.DAO.AdminDAO;
import com.booking.DAO.CashDAO;
import com.booking.DAO.CouponDAO;
import com.booking.DAO.UserDAO;
import com.booking.DAO.impl.ReviewDAOImpl;
import com.booking.accommodation.Accommodation;
import com.booking.dto.Admin;
import com.booking.dto.Coupon;
import com.booking.dto.User;
import com.booking.menu.AccommodationMenu;
import com.booking.menu.AdminMenu;
import com.booking.menu.UserMenu;


public class Main {

	static BufferedReader br;
	static Admin admin;
	static UserDAO userDAO;
	static User user;
	static com.booking.dto.Review review;
	static ReviewDAOImpl reviewDAO;
	static Accommodation accommodation; 
	static AccommodationviewDAO accommodationviewDAO;
	static AccommodationMenu accommodationMenu;
	static CashDAO cashDAO;
	static boolean loginStatus;
	static Coupon coupon;
	static CouponDAO couponDAO;

	public Main(){
		br = new BufferedReader(new InputStreamReader(System.in));
		userDAO = new UserDAO();

		accommodationviewDAO = new AccommodationviewDAO();
		cashDAO = new CashDAO();

		couponDAO = new CouponDAO();
		reviewDAO = new ReviewDAOImpl();

		callMenu();
	}

	private void callMenu(){


		while(true) {
			int menuNum = Integer.MAX_VALUE;


			while(true) {
				try {
					System.out.println("================================================================================");
					System.out.println("                         ✨🌟  우와놀자 - 콘솔 예약 시스템  🌟✨                    ");
					System.out.println("================================================================================");
					System.out.println("👉 원하시는 메뉴를 입력해주세요");
					System.out.println("1. 로그인");
					System.out.println("2. 회원가입");
					System.out.println("0. 프로그램 종료");
					menuNum = Integer.parseInt(br.readLine());
					if(menuNum != 1 && menuNum != 2 && menuNum != 0) {
						System.out.println("1,2,0 번 메뉴중 하나를 입력해주세요");
						continue;
					}else break;

				}catch (Exception e) {
					System.out.println("❌ 잘못된 입력입니다 ❌");
					continue;
				}
			}


			if(menuNum == 1) {

				try {
					System.out.println("로그인할 ID를 입력해주세요");
					String ID = br.readLine();
					System.out.println("비밀번호를 입력해주세요");
					String passwd = br.readLine();

					if((admin = AdminDAO.adminLogin(ID, passwd)) != null) { // 로그인할떄 admin이 잡히면 admin을 부여
						loginStatus = true;
						AdminMenu adminMenu = new AdminMenu(br, admin,user,coupon);

					}
					else if((user = userDAO.login(ID, passwd)) != null) {
						loginStatus = true;
						System.out.println("로그인이 완료되었습니다.");


//						System.out.println("숙소 메뉴 입니다.");
//						AccommodationMenu accommodationMenu = new AccommodationMenu();
//						accommodationMenu.AccMenu(br,accommodation, accommodationviewDAO);
//						

//						UserMenu userMenu = new UserMenu();
						System.out.println("우와! 환영합니다! 😊 우와놀자에서 최고의 여행을 경험하세요!");


				System.out.println("원하시는 항목을 선택하세요 ! ! !\n");
						System.out.println("1. 숙소 예약");	
						System.out.println("2. 마이페이지");
						System.out.println("3. 문의하기");
						System.out.println("4. 뒤로 가기");

						System.out.println("0. 로그아웃");
						int num;
						try {
							num = Integer.parseInt(br.readLine());
							if(num == 1) {
								System.out.println("\n숙소 예약");
								System.out.println("숙소 메뉴 입니다.");
								accommodationMenu.AccMenu(br,accommodation, accommodationviewDAO);
								
							}else if(num == 2) {
								System.out.println("\n마이페이지");
								new UserMenu(br,user);
							}
							else if(num == 3) { 
								System.out.println("문의하기");
							}
							else if(num == 4) {
								System.out.println("🔙 뒤로 가기 완료!");

							}else if (num == 0) {
								System.out.println("로그아웃 완료");


							}

						} catch (NumberFormatException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				} 

			}else if(menuNum == 2) {

				String emailFormat = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

				try {
					System.out.println("회원가입 모드입니다.");
					String ID;
					while(true) { // 중복된 ID 거르기 위한 while문
						System.out.println("회원가입할 ID를 입력해주세요");
						ID = br.readLine();
						if(UserDAO.checkIDDuplicate(ID)) {
							System.out.println("중복된 ID입니다.");
							continue;
						}else {
							break;
						}
					}
					System.out.println("비밀번호를 입력해주세요");
					String passwd = br.readLine();

					System.out.println("이름을 입력해주세요");
					String name = br.readLine();

					String email = null;
					while(true) {
						System.out.println("이메일을 입력해주세요");
						email = br.readLine();
						if(!email.matches(emailFormat)) {
							System.out.println("잘못된 이메일 형식입니다.");
						}else {
							break;
						}
					}

					if(!userDAO.register(ID, passwd, name, email)) {
						System.out.println("회원가입에 실패했습니다.");
					}else {
						System.out.println("회원가입에 성공했습니다.");
					}
					continue;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}else if(menuNum == 0) {
				System.exit(0);
			}
		}
	}
}




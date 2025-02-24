package com.booking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.booking.dto.Admin;
import com.booking.dto.User;
import com.booking.menu.AccommodationMenu;
import com.booking.menu.AdminMenu;
import com.booking.menu.UserMenu;
import com.booking.service.AdminService;
import com.booking.service.UserService;
import com.booking.service.impl.AdminServiceImpl;
import com.booking.service.impl.UserServiceImpl;
import com.util.Util;


public class Main {

	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));;
	private Admin admin;
	private User user;
	private UserService userService = new UserServiceImpl(br);;
	private AdminService adminService = new AdminServiceImpl();
	
	public Main(){
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
					if(Util.checkValidNum(menuNum, 1,2,0)) {
						break;
					}else {
						System.out.println("1,2,0 번 메뉴중 하나를 입력해주세요");
						continue;
					}
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

					if((admin = adminService.login(ID, passwd)) != null) { // 로그인할떄 admin이 잡히면 admin을 부여
						new AdminMenu(br, admin); // 어드민메뉴로 분리
					}
					else if((user = userService.login(ID, passwd)) != null) {
						System.out.println("로그인이 완료되었습니다.");
						new UserMenu(br,user);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}else if(menuNum == 2) { // 회원가입 탭
				userService.register();
			}else if(menuNum == 0) { // 종료 메뉴
				System.exit(0);
			}
		}
	}

	
}




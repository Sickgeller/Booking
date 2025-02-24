package com.booking.menu;

import java.io.BufferedReader;

import com.booking.DAO.AccommodationDAO;
import com.booking.dto.Admin;
import com.util.Util;

public class AdminAccomoMenu {
	
	private BufferedReader br;
	private Admin admin;
	
	AdminAccomoMenu(BufferedReader br, Admin admin){
		this.br = br;
		this.admin = admin;
		menu();
	}
	
	private void menu(){
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
				if(Util.checkValidNum(answer, 1,2,0)) {
					break;
				}else {
					System.out.println("1,2,0중 하나를 입력해주세요");
					continue;
				}
			} catch (Exception e) {
				System.out.println("잘못된 입력값입니다.");
				continue;
			}
		}
		if(answer == 1) accommodationDAO.accommodation_management(br, admin);
//		else if(answer == 2 ) 
//			accommodation_insert();
		else if (answer == 0) return;
	}

}

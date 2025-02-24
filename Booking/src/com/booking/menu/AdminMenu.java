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
import com.util.Util;

public class AdminMenu { 
	// 어드민 메뉴 카테고리
	// 파라미터들 정리해놓음
	private Admin admin;
	private BufferedReader br;


	public AdminMenu(BufferedReader br, Admin admin){
		this.br = br;
		this.admin = admin;
		menu();
	}


	public void menu() {	
		while(true) {
			int answer = Integer.MIN_VALUE;
			while(true) {
				try {
					System.out.println("관리자 메뉴입니다.");
					System.out.println("원하시는 항목을 골라주세요");
					System.out.println("1.숙소 관리");
					System.out.println("2.문의 관리 페이지");
					System.out.println("3.쿠폰 관리 페이지");
					System.out.println("0.로그아웃");
					answer = Integer.parseInt(br.readLine());

					if(Util.checkValidNum(answer, 1,2,3,0)) {
						break;
					}else {
						System.out.println("유효하지않은 입력입니다. 1,2,3,0중 하나를 입력해주세요");
					}
				}catch (NumberFormatException e) {
					System.out.println("숫자만 입력해주세요");
					continue;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if(answer == 1) {
				new AdminAccomoMenu(br, admin); // 어드민 숙소관리 메뉴
			}else if(answer == 2) {
				new AdminQNAMenu(br,admin);
			}else if(answer == 3) {
				couponManagement();
			}else if(answer == 0){
				break;
			}
		}
	}

	private void couponManagement() { // 쿠폰 관리 메뉴
		// 쿠폰 종류 조회


	}
}

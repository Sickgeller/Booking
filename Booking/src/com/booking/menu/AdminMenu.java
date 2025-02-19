package com.booking.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.booking.DAO.AccommodationDAO;
import com.booking.DAO.AdminDAO;
import com.booking.DAO.QnADAO;
import com.booking.member.Admin;

public class AdminMenu { // 어드민 메뉴 카테고리

	static Admin admin;
	static int answer;
	static AdminDAO adminDAO;
	static AccommodationDAO accommodationDAO;

	public void menu(BufferedReader br, Admin admin, AdminDAO adminDAO) {

		AdminMenu.admin = admin;
		AdminMenu.adminDAO = adminDAO;

		try {
			while(true) {
				System.out.println("관리자 메뉴입니다.");
				System.out.println("원하시는 항목을 골라주세요");
				System.out.println("1.숙소 관리");
				System.out.println("2.문의 관리 페이지");
				System.out.println("3.쿠폰 관리 페이지");
				System.out.println("0.로그아웃");
				answer = Integer.parseInt(br.readLine());
				if(answer == 1) {
					accommodationAdmin(br);
				}else if(answer == 2) {
					qnaManagement(br);
				}else if(answer == 3) {
					couponManagement(br);
				}else if(answer == 0){
					return;
				}else {
					System.out.println("잘못된 입력입니다");
					continue;
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void accommodationAdmin(BufferedReader br) {

		accommodationDAO = new AccommodationDAO();
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
		if(answer == 1) accommodationDAO.accommodation_management(br, accommodationDAO, admin);
		else if(answer == 2 ) accommodation_insert(br);
		else if (answer == 0) return;
	}

	private void accommodation_insert(BufferedReader br) { // 만들다 말았음 숙소 INSERT 부분
		
	}



	private void qnaManagement(BufferedReader br) {
		int answer = Integer.MIN_VALUE;
		QnADAO qnaDAO = new QnADAO();
		while(true) {
			System.out.println("문의 관련 페이지 입니다.");
			System.out.println("1.미답변 QnA 답변하기");
			System.out.println("2.답변한 QnA 수정하기");
			System.out.println("3.QnA 전체보기");
			System.out.println("0. 뒤로가기");
			try {
				answer = Integer.parseInt(br.readLine());
				if(answer != 1 && answer != 2 && answer != 3 && answer != 0) {
					System.out.println("유효하지않은 입력입니다.");
					continue;
				}else {
					break;
				}
			} catch (Exception e) {
				System.out.println("비정상적인 입력입니다.");
				continue;
			}
		}
		
		if(answer == 1) {
			qnaDAO.answerToQNA(br,admin);
		}else if(answer == 2) {
			qnaDAO.answerUpdate(br, admin);
		}else if(answer == 3) {
			qnaDAO.showQnA();
		}else if(answer == 0) {
			return;
		}
	}
	private void couponManagement(BufferedReader br) { // 쿠폰 관리 메뉴

	}
}

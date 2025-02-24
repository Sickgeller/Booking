package com.booking.menu;

import java.io.BufferedReader;

import java.io.IOException;

import com.booking.DAO.AccommodationDAO;
import com.booking.DAO.AccommodationviewDAO;
import com.booking.DAO.impl.ReviewDAOImpl;
import com.booking.accommodation.Accommodation;
import com.booking.dto.Review;

public class AccommodationMenu { 
	private BufferedReader br;
	static AccommodationviewDAO accommodationviewDAO;  // 숙소 DAO
	static Accommodation accommodation;   // 숙소클래스(생성자, getter, setter)
	// 리뷰 클래스
	static Review review; 
	// 리뷰 DAO
	static ReviewDAOImpl reviewDAO; 
	
	// 숙소메뉴 호출 함수
	public void AccMenu(BufferedReader br, Accommodation accommodation, AccommodationviewDAO accommodationviewDAO) throws IOException {
		//숙소 메뉴
		AccommodationMenu.accommodation = accommodation;
		AccommodationMenu.accommodationviewDAO = accommodationviewDAO;

		//1. 숙소보기
		try {

			while (true) {
				System.out.println("원하는 항목을 선택하세요.");
				System.out.println("1.전체 숙소 보기");
				System.out.println("2.숙소 상세보기");
				System.out.println("3.예약하기");
				int no = Integer.parseInt(br.readLine());
				if(no==1) {
					System.out.println("1.국내 2.해외");
					int num1 = Integer.parseInt(br.readLine());
					
					accommodationviewDAO.selectInfo();
					
					// 생성하고 다음 메뉴 부르기(reviewMenu)
					ReviewMenu reviewMenu = new ReviewMenu();
					reviewMenu.R_menu(br,review, reviewDAO, accommodation,accommodationviewDAO);
				

				}else if(no==2){
					//상세 글 한번 보기
					// 목록에서 선택할 글 번호 확인
					// (숙소정보 한번 더 보여주기)
					accommodationviewDAO.selectInfo();
					System.out.print("선택한 숙소 번호 >");
					int num = Integer.parseInt(br.readLine());
					System.out.println("============================");
					accommodationviewDAO.selectDetailInfo(num);

				}
				// 희동쿤이 해야할 예약 하기
				else if(no == 3) {
					AccommodationDAO accommodationDAO = new AccommodationDAO();

					System.out.println("예약을 위해 국내 / 해외 선택하세요 (1번을 누르면 [국내] 2번을 누르면 [해외] 입니다 ");
					int areaNum = Integer.parseInt(br.readLine());
					try {
						if(areaNum == 1) {
							System.out.println("국내 예약");
							try {
								System.out.println("2번 추천여행지 보기");
								int sugNum = Integer.parseInt(br.readLine());
								try {
									if(sugNum == 2) { // 추천 여행지 메뉴판
										System.out.println("추천 여행지 보기");
										accommodationDAO.suggest_accommodation(br, null);
									}

								}catch (Exception e) {
									// TODO: handle exception
								}


							}catch (Exception e) {
								// TODO: handle exception
							}
						}
						else if(areaNum == 2) {
							System.out.println("해외 예약");

							System.out.println("2번 추천 여행지 보기");
							int sugNum = Integer.parseInt(br.readLine());
							try {
								if(sugNum == 2) {// 추천 여행지 메뉴판
									accommodationDAO.suggest_accommodation(br, "해외");
									
								}
							}catch (Exception e) {
								// TODO: handle exception
							}
						}

					}catch(Exception e) {

					}


				}

			} 
		}catch(NumberFormatException e){
			System.out.println("[숫자만 입력 가능]");
		}finally {

		}


	} // class

}

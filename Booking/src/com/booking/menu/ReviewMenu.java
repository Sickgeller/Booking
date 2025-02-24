package com.booking.menu;

import java.io.BufferedReader;
import java.io.IOException;

import com.booking.DAO.AccommodationviewDAO;
import com.booking.DAO.ReviewDAO;
import com.booking.DAO.impl.ReviewDAOImpl;
import com.booking.accommodation.Accommodation;
import com.booking.dto.Review;
import com.booking.dto.User;
import com.booking.service.ReviewService;
import com.booking.service.impl.ReviewServiceImpl;

public class ReviewMenu {
	static Review review;
	static Accommodation accommodation;
	//숙소 뷰
	static AccommodationviewDAO adao;
	private BufferedReader br;
	private ReviewService reviewService;
	private ReviewDAO reviewDAO;

	//
	public ReviewMenu(BufferedReader br) {
		this.br = br;
		this.reviewDAO = new ReviewDAOImpl();
		this.reviewService = new ReviewServiceImpl(reviewDAO, br);
	}
	public void R_menu(BufferedReader br) {
		// 선택된 숙소의 리뷰 보기
		// 초기화를 안했
		try {
			while(true) {
				System.out.println("숙소 리뷰 확인하시겠습니까?");
				System.out.println("1. 예 2. 아니오");

				int no = Integer.parseInt(br.readLine());
				if(no == 1) {
					System.out.println("숙소번호 입력하세요>");
					int num = Integer.parseInt(br.readLine());
					System.out.println("================================================");
					reviewService.selectdetailReview(num);
				}else if(no == 2) {
					// 예약하기 화면으로
//					accommodationviewDAO.selectInfo();
					System.out.print("선택한 숙소 번호 >");
					int num = Integer.parseInt(br.readLine());
					System.out.println("============================");
//					accommodationviewDAO.selectDetailInfo(num);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {

		}

	}
	
	public void reviewManagement(User user) { // 리뷰  관련 메뉴
		int menuNum = Integer.MIN_VALUE;
		while(true) {
			System.out.println("원하는 번호를 선택하세요.");
			System.out.println("1. 리뷰 수정 하기");
			System.out.println("2. 리뷰 삭제 하기");
			System.out.println("0. 뒤로가기");
			try {
				menuNum = Integer.parseInt(br.readLine());
				if(menuNum != 1 && menuNum != 2 && menuNum != 0) {
					continue;
				}else {
					break;
				}
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
				System.out.println("숫자만 입력해주세요");
				continue;
			}
		}
		
		if(menuNum == 1) {
			reviewService.updateReview(user);
		}else if(menuNum == 2) {
			reviewService.deleteReview(user);
		}else if(menuNum == 0) {
			return;
		}
	}


}// class

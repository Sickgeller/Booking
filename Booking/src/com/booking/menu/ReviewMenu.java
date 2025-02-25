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
import com.util.Util;

public class ReviewMenu {
	static Review review;
	static Accommodation accommodation;
	//숙소 뷰
	static AccommodationviewDAO adao;
	private BufferedReader br;
	private ReviewService reviewService;

	//
	public ReviewMenu(BufferedReader br) {
		this.br = br;
		this.reviewService = new ReviewServiceImpl(br);
	}
	public void R_menu(BufferedReader br) {
		// 선택된 숙소의 리뷰 보기
		// 초기화를 안했
		while(true) {
		try {
				System.out.println("숙소 리뷰 확인하시겠습니까?");
				System.out.println("1. 예 2. 아니오");
				int no = Integer.parseInt(br.readLine());
				if(no == 1) {
					System.out.println("숙소번호 입력하세요>");
					int num = Integer.parseInt(br.readLine());
					System.out.println("================================================");
					reviewService.selectdetailReview(num);
				}else if(no == 2) {
					//추후 완성 의도 모르겠음 아직
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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
				if(Util.checkValidNum(menuNum, 1,2,0)) {
					break;
				}else {
					System.out.println("1,2,0중 하나를 입력해주세요");
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

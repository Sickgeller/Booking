package com.booking.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.booking.dto.User;
import com.booking.service.ReviewService;
import com.booking.service.impl.ReviewServiceImpl;
import com.util.Util;

public class ReviewMenu {
	//숙소 뷰
	//	static AccommodationviewDAO adao;
	private BufferedReader br;
	private ReviewService reviewService;
	private List<Integer> idList;
	private int id;

	public ReviewMenu(BufferedReader br) {
		this.br = br;
	}

	public ReviewMenu() {

	}
	//id가 -1이면 입력받지않은 상태
	public ReviewMenu(BufferedReader br, List<Integer> idList, int id) {
		this.br = br;
		this.idList = idList;
		this.id = id;
		this.reviewService = new ReviewServiceImpl(br);
		menu();
	}
	private void menu() {
		int answer = Integer.MIN_VALUE;
		while(true) {
			System.out.println("숙소 리뷰 확인하시겠습니까?");
			System.out.println("1. 예 2. 아니오");
			try {
				answer = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
			if(answer == 1) {
				checkAccoReview();
				return;
			}else if(answer == 2) {
				return;
			}
		}
	}

	private void checkAccoReview() {
		int acco_id = Integer.MIN_VALUE;
		if(id == -1) {
			while(true) {
				try {
					System.out.println("리뷰를 확인할 숙소 번호를 입력해주세요");
					acco_id = Integer.parseInt(br.readLine());
					if(idList.contains(acco_id)) break;
					else {
						System.out.println("범위내의 숙소를 입력해주세요");
						continue;
					}
				} catch (NumberFormatException | IOException e) {
					e.printStackTrace();
				}
			}
			reviewService.selectdetailReview(acco_id);	
		}else{
			reviewService.selectdetailReview(id);	
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

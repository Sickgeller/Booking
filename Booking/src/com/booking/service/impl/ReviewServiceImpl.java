package com.booking.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.booking.DAO.ReviewDAO;
import com.booking.dto.QNA;
import com.booking.dto.Review;
import com.booking.dto.User;
import com.booking.service.ReviewService;

public class ReviewServiceImpl implements ReviewService{

	ReviewDAO reviewDAO;
	BufferedReader br;

	public ReviewServiceImpl(ReviewDAO reviewDAO, BufferedReader br) {
		this.reviewDAO = reviewDAO;
		this.br = br;
	}

	@Override
	public void updateReview(User user) {
		List<Review> reviewList = reviewDAO.getReviewByUserID(user);
		showReview(reviewList);
		int review_num = Integer.MIN_VALUE;
		while(true) {
			System.out.println("수정할 리뷰 번호 선택");
			try {
				review_num = Integer.parseInt(br.readLine());
				if(isValidReview(reviewList, review_num)) {
					break;
				}else {
					System.out.println("유효하지않은 리뷰 번호 입니다.");
					continue;
				}
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
		}
		String review_Content = null;
		System.out.println("수정할 내용을 입력하세요");
		try {
			review_Content = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean result = reviewDAO.updateReview(review_num, review_Content);
		if(result) {
			System.out.println("수정 성공 ! ! ! ");
		}else {
			System.out.println("수정 실패");
		}
	}

	private void showReview(List<Review> reviewList){
		for(Review review : reviewList) {
			System.out.println("----------------------------------------------");
			System.out.println("번호 : " + review.getReview_ID());
			System.out.println("작성자 이름 : " + review.getID());
			System.out.println("숙소 번호 : " + review.getAccomodation_ID());
			System.out.println("리뷰 작성 날짜 : " + review.getReview_date());
			System.out.println("리뷰 내용 : " + review.getReview_content());
			System.out.println("평점 : " + review.getReview_rating());
			System.out.println("----------------------------------------------");
		}
	}

	private boolean isValidReview(List<Review> answeredReviewList, int reviewId) {
		for(Review review : answeredReviewList) {
			if(review.getReview_ID() == reviewId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void deleteReview(User user) {
		System.out.println("리뷰 삭제하기");
		int num = Integer.MIN_VALUE;
		List<Review> reviewList = reviewDAO.getReviewByUserID(user);
		showReview(reviewList);
		while(true) {
			try {
				System.out.println("삭제할 리뷰 번호 선택");
				num = Integer.parseInt(br.readLine());
				if(isValidReview(reviewList, num)) {
					break;
				}else {
					System.out.println("우효하지않은 리뷰번호입니다.");
				}
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				e.printStackTrace();
			}
		}
		
		boolean result = reviewDAO.deleteReview(num);
		System.out.println(result ? "리뷰를 삭제했습니다." : "삭제에 실패했습니다");
	}

	@Override
	public void selectdetailReview(int accomodation_ID) {
		
		List<Review> reviewList = reviewDAO.getReviewsByAccommodationID(accomodation_ID);
		
		if(reviewList.isEmpty()) {
			System.out.println("검색된 숙소 리뷰가 없습니다.");
			return;
		}
		
		for(Review review : reviewList) {
			System.out.println("-------------------------------------------------");
			System.out.println("사용자 ID:" + review.getID());
			System.out.println("리뷰 대상 숙소 번호:" + review.getAccomodation_ID());
			System.out.println("리뷰 작성일:" + review.getReview_date());
			System.out.println("리뷰 내용:"+ review.getReview_content());
			System.out.println("리뷰 평점:"+ review.getReview_rating());
			System.out.println("-------------------------------------------------");
		}
	}

}

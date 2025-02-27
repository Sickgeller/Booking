package com.booking.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.booking.DAO.CouponDAO;
import com.booking.DAO.impl.CouponDAOImpl;
import com.booking.dto.Admin;
import com.booking.dto.Coupon;
import com.booking.service.CouponService;

public class CouponServiceImpl implements CouponService{
	
	private BufferedReader br;
	private CouponDAO couponDAO = new CouponDAOImpl();
	private Admin admin;
	
	public CouponServiceImpl(BufferedReader br, Admin admin){
		this.br = br;
		this.admin = admin;
	}

	@Override
	public void showAllCoupon() {
		List<Coupon> couponList = couponDAO.getEveryCoupon();
		for(Coupon coupon : couponList) {
			System.out.println("쿠폰 번호 : " + coupon.getCoupon_ID());
			System.out.println("쿠폰 코드 : " + coupon.getCoupon_code());
			System.out.println("발급 일자 : " + coupon.getCoupon_issuance_date());
			System.out.println("만료일 : " + coupon.getCoupon_expired_date());
			System.out.println("할인 금액 : " + coupon.getCoupon_discount());
		}
	}

	@Override
	public void reg_coupon() {
		String coupon_code = null;
		while(true) {
			System.out.print("쿠폰 코드 : \n");
			try {
				coupon_code = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(!couponDAO.isCouponExists(coupon_code)) {
				break;
			}else {
				System.out.println("중복된 코드입니다.");
			}
		}
		
		int coupon_discount = Integer.MIN_VALUE;
		while(true) {
			System.out.println("할인율 가격 : ");
			try {
				coupon_discount = Integer.parseInt(br.readLine());
				if(coupon_discount > 0) {
					break;
				}else {
					System.out.println("유효한 할인가격을 입력해주세요");
				}
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
		}
		boolean result = couponDAO.reg_coupon(coupon_code, admin.getID(), coupon_discount);
		if(result) {
			System.out.println("쿠폰 발급 성공");
		}else {
			System.out.println("쿠폰 발급 실패");
		}
	}

	@Override
	public void giveCouponUser() {
		List<Coupon> couponList = couponDAO.getEveryCoupon();
		List<Integer> idList = new ArrayList<>();
		for(Coupon coupon : couponList) {
			System.out.println("쿠폰 번호 : " + coupon.getCoupon_ID());
			idList.add(coupon.getCoupon_ID());
			System.out.println("쿠폰 코드 : " + coupon.getCoupon_code());
			System.out.println("발급 일자 : " + coupon.getCoupon_issuance_date());
			System.out.println("만료일 : " + coupon.getCoupon_expired_date());
			System.out.println("할인 금액 : " + coupon.getCoupon_discount());
		}
		int coupon_id = Integer.MIN_VALUE;
		while(true) {
			try {
				coupon_id = Integer.parseInt(br.readLine());
				if(idList.contains(coupon_id)) {
					break;
				}else {
					System.out.println("목록에 있는 쿠폰 ID를 입력해주세요");
					continue;
				}
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
		}
		
		System.out.println("유저 ID를 입력해주세요 :"); // 목록 보여주고 할지말지는 희동씨 선택
		String user_id = null;
		try {
			user_id = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean result = couponDAO.giveCouponUser(coupon_id, user_id);
		if(result) {
			 System.out.println(" 기존 쿠폰 업데이트 완료 (보유 수 증가)");
		}else {
			  System.out.println(" 쿠폰 업데이트 실패");
		}
	}
	
	

}

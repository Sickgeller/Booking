package com.booking.menu;

import java.io.BufferedReader;

import com.booking.DAO.CouponDAO;
import com.booking.dto.Admin;
import com.util.Util;

public class AdminCouponMenu {

	private BufferedReader br;
	private Admin admin;

	AdminCouponMenu(BufferedReader br , Admin admin){
		this.br = br;
		this.admin = admin;
		menu();
	}

	private void menu() {
		int num = Integer.MIN_VALUE;
		while(true) {
			try{
				System.out.println("쿠폰을 관리하는 페이지입니다.");
				System.out.println("1. 쿠폰 종류 조회");
				System.out.println("2. 쿠폰 등록");
				System.out.println("3. 신규 사용자에게 기본 쿠폰 발급");
				System.out.println("4. 사용자에게 쿠폰 발급");
				System.out.println("0. 뒤로가기");
				num = Integer.parseInt(br.readLine());
				
				if(Util.checkValidNum(num, 1,2,3,4,0)) {
					break;
				}else {
					System.out.println("1,2,3,4,0중 하나를 입력해주세요");
					continue;
				}
			}catch (Exception e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}

		}
		CouponDAO coupondao = new CouponDAO();
		if(num == 1) {
			System.out.println("쿠폰 종류 조회");
			coupondao.showAllCoupon(admin.getID());
		}else if(num == 2) 
		{
			System.out.println("쿠폰 등록");
			coupondao.reg_coupon(br);
		}
		else if(num == 3) {
			System.out.println("신규 사용자에게 기본 쿠폰 발급");
			coupondao.giveNewUserCoupon(coupondao.getDefaultCouponID());
		}
		else if(num == 4) {
			System.out.println("사용자에게 쿠폰 발급");
			//coupondao.giveCouponUser(admin);
		}else if(num == 0) {
			return;
		}
		
	}

}

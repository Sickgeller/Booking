package com.booking.DAO;

import java.util.List;

import com.booking.dto.Coupon;

public interface CouponDAO {
	
	List<Coupon> getEveryCoupon(); // 모든 쿠폰 가져오는 메서드
	List<Coupon> getUserCoupon(String userID);
	boolean giveCouponUser(int coupon_id, String User_ID);
	boolean reg_coupon(String coupon_code, String admin_ID, int coupon_discount);
	boolean isCouponExists(String coupon_code);

}

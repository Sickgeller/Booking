package com.booking.DAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.booking.dto.Admin;
import com.booking.dto.Coupon;
import com.booking.dto.User;
import com.dbutil.DBUtil;




public class CouponDAO {

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
    static Coupon coupon;
    static Admin admin;
    static User user;
	public void updateCoupon(Admin ID) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}	
	
		
	}
	
	// ✅ 신규 사용자에게 기본 쿠폰 지급하는 메서드
	public void giveNewUserCoupon(int coupon_ID) {
	    Connection conn = null;
	    PreparedStatement check_pstmt = null;
	    PreparedStatement insert_pstmt = null;
	    ResultSet rs = null;
	    String check_sql = "SELECT COUNT(*) FROM CP_POSSESS WHERE USER_ID = ?";
	    String insert_sql = "INSERT INTO CP_POSSESS (COUPON_ID, USER_ID, COUPON_COUNT) VALUES (?, ?, 1)";

	    try {
	        conn = DBUtil.getConnection();
	        System.out.print("신규 사용자 ID 입력: ");
	        String user_ID = br.readLine();  // 신규 사용자 ID 입력 받기

	        // 1️⃣ 사용자가 이미 쿠폰을 가지고 있는지 확인
	        check_pstmt = conn.prepareStatement(check_sql);
	        check_pstmt.setString(1, user_ID);
	        rs = check_pstmt.executeQuery();

	        if (rs.next() && rs.getInt(1) > 0) {
	            System.out.println("이미 해당 쿠폰을 보유하고 있는 사용자입니다.");
	            return;
	        }

	        // 2️⃣ 신규 사용자에게 기본 쿠폰 지급
	        insert_pstmt = conn.prepareStatement(insert_sql);
	        insert_pstmt.setInt(1, coupon_ID);
	        insert_pstmt.setString(2, user_ID);

	        int update = insert_pstmt.executeUpdate();
	        if (update > 0) {
	            conn.commit();
	            System.out.println("기본 쿠폰 지급 완료!");
	        } else {
	            conn.rollback();
	            System.out.println("기본 쿠폰 지급 실패.");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.executeClose(rs, check_pstmt, null);
	        DBUtil.executeClose(null, insert_pstmt, conn);
	    }
	}

	// 사용자가 겹치는 쿠폰이 있는지 확인하는 코드
	
	public boolean  isDupUserCoupon(int coupon_ID, String user_ID) {
	
			Connection conn = null;
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    String sql = "SELECT COUNT(*) FROM COUPON WHERE COUPON_ID = ?";
		    try {
		        conn = DBUtil.getConnection();
		        pstmt = conn.prepareStatement(sql);
		        pstmt.setInt(1, coupon_ID);
		        pstmt.setString(2,user_ID);
		        rs = pstmt.executeQuery();

		        if (rs.next() && rs.getInt(1) > 0) {
		            return true;  // 중복된 쿠폰 코드가 있음
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        DBUtil.executeClose(rs, pstmt, conn);
		    }

		    return false;  // 중복된 쿠폰 코드 없음
		}
		
		
	
	
	// 어드민이 유저에게 쿠폰을 지급하는 코드 / 만약 이미 있는 쿠폰이면 갯수만 증가
	public void giveCouponUser(String Admin_ID, int coupon_id,String User_ID) {
		Connection conn = null;
		PreparedStatement select_pstmt = null;
		PreparedStatement insert_pstmt = null;
		PreparedStatement update_pstmt = null;
		String insert_sql = null;
		String update_sql = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			
			String select_sql = "SELECT COUPON_ID, COUPON_CODE,COUPON_DISCOUNT FROM COUPON";
			rs = select_pstmt.executeQuery();

	        List<Integer> couponList = new ArrayList<>();
	        while (rs.next()) {
	            couponList.add(rs.getInt("COUPON_ID"));
	        }
	        
	        if(!isDupUserCoupon(coupon_id,User_ID)) {
	        	
	               insert_sql = "INSERT INTO CP_POSSESS (COUPON_ID, USER_ID,COUPON_COUNT)" +
	                    "SELECT ?,U.USER_ID, 1" +
	                    "FROM USERS U WHERE TRUNC(U.JOIN_DATE) = TRUNC(SYSDATE)";
	                     
	                     
	                     insert_pstmt.setInt(1, coupon_id);
	                     insert_pstmt.setString(2, User_ID);
	                     int update = insert_pstmt.executeUpdate();
	                     if(update == 1) {
	                    	 
	                    	 conn.commit();
	                    	 System.out.println("새 쿠폰 지급 완료");
	                     }else {
	                    	 conn.rollback();
	                    	 System.out.println("쿠폰을 지급하지 못했습니다.");
	                     }
	        	
	        }else {
	        	update_sql =  "UPDATE CP_POSSESS SET COUPON_COUNT = COUPON_COUNT + 1 WHERE COUPON_ID = ? AND USER_ID=?";
	        	update_pstmt = conn.prepareStatement(update_sql);

	        	        update_pstmt.setInt(1, coupon_id);
	        	        update_pstmt.setString(1, User_ID);
	        	        int update= update_pstmt.executeUpdate();

	        	        if (update > 0) {
	        	            conn.commit();
	        	            System.out.println(" 기존 쿠폰 업데이트 완료 (보유 수 증가)");
	        	        } else {
	        	            conn.rollback();
	        	            System.out.println(" 쿠폰 업데이트 실패");
	        	        }
	        }
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}finally {
			DBUtil.executeClose(rs, update_pstmt, conn);
			DBUtil.executeClose(rs, insert_pstmt, conn);
			DBUtil.executeClose(rs, select_pstmt,conn);
		}
	}
	
	
	public void showUserCoupon(String ID) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			System.out.println(" 보유한 쿠폰 목록 조회 ");
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM COUPON WHERE USER_ID=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				System.out.println("쿠폰 번호 : " + rs.getInt("COUPON_CODE"));
				System.out.println("발급 일자 : " + rs.getDate("COUPON_ISSUANCE_DATE"));
				System.out.println("만료일 : " + rs.getDate("COUPON_EXPIRED_DATE"));
				System.out.println("할인 금액 : " + rs.getInt("COUPON_DISCOUNT"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
	}

	// 쿠폰 등록하는 코드
	public void reg_coupon(BufferedReader br) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		
		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO COUPON (COUPON_ID, ADMIN_ID, COUPON_CODE, COUPON_ISSUANCE_DATE, COUPON_EXPIRED_DATE, COUPON_DISCOUNT) " +
                    "VALUES (COUPON_SEQ.NEXTVAL, ?, ?, SYSDATE, SYSDATE + 30, ?)";
				
			String AdminID = "ADMIN";
			System.out.print("쿠폰 코드 : \n");
		    String coupon_code = br.readLine();
			
		    System.out.print("할인율 가격 : ");		
		    int coupon_discount = Integer.parseInt(br.readLine());
		    pstmt = conn.prepareStatement(sql);
		    pstmt.setString(1, AdminID);
	        pstmt.setString(2, coupon_code);
	        pstmt.setInt(3, coupon_discount);
		    
		    if(!isCouponExists(coupon_code)) {
		    	int update = pstmt.executeUpdate();
			    if(update == 1) {
			    	conn.commit();
			    	System.out.println("쿠폰 발급 성공");
			    }
			    else {
			    	conn.rollback();
			    	System.out.println("쿠폰 발급 실패");
			    }
		    }
		    else {
		    	
			    	conn.rollback();
			    	System.out.println("쿠폰 발급 실패");
			    	System.out.println("쿠폰 코드가 겹칩니다");
			    
		    }
		    	
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		
	}
	
	// 관리자가 모든 쿠폰을 확인하는 코드
	public void showAllCoupon(String ID) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			System.out.println(" 보유한 쿠폰 목록 조회 ");
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM COUPON WHERE ADMIN_ID=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "ADMIN");
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				System.out.println("\n쿠폰 코드 : " + rs.getString("COUPON_CODE"));
				System.out.println("발급 일자 : " + rs.getDate("COUPON_ISSUANCE_DATE"));
				System.out.println("만료일 : " + rs.getDate("COUPON_EXPIRED_DATE"));
				System.out.println("할인 금액 : \n" + rs.getInt("COUPON_DISCOUNT"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
	}
	
	// 발급하려는 코드가 중복되면 표시

	public boolean isCouponExists(String coupon_code) {
		
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = "SELECT COUNT(*) FROM COUPON WHERE COUPON_CODE = ?";
	    try {
	        conn = DBUtil.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, coupon_code);
	        rs = pstmt.executeQuery();

	        if (rs.next() && rs.getInt(1) > 0) {
	            return true;  // 중복된 쿠폰 코드가 있음
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.executeClose(rs, pstmt, conn);
	    }

	    return false;  // 중복된 쿠폰 코드 없음
	}
	
	// 사용한 쿠폰이면 지워주는 코드
	public int getDefaultCouponID() {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = "SELECT COUPON_ID FROM COUPON WHERE ADMIN_ID = 'ADMIN' AND COUPON_CODE = 'NEWUSER1000'";
	    int couponID = -1;

	    try {
	        conn = DBUtil.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            couponID = rs.getInt("COUPON_ID");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.executeClose(rs, pstmt, conn);
	    }

	    return couponID;
	}
	
}

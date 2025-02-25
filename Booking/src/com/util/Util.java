package com.util;

import java.sql.Connection;

public class Util {
	
	public static final String emailFormat = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	public static boolean checkValidNum(int num, int...numArr) { // 유효한 번호 입력했는지 검사해주는 메서드
		for(int numElement : numArr ) {
			if(num == numElement) return true;
		}
		return false;
	}
	public static void doCommitOrRollback(Connection conn, int num) {
		if(num == 1) {
			if(conn != null)try {conn.commit();}catch(Exception e) {}
		}else {
			if(conn != null)try {conn.rollback();}catch(Exception e) {}
		}
	}
}

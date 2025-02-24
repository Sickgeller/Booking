package com.util;

public class Util {
	
	public static final String emailFormat = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	public static boolean checkValidNum(int num, int...numArr) { // 유효한 번호 입력했는지 검사해주는 메서드
		for(int numElement : numArr ) {
			if(num == numElement) return true;
		}
		return false;
	}
	
}

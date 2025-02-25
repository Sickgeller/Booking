package com.booking.menu;

import java.io.BufferedReader;

import com.util.Util;

public class UserQnAMenu {
	
	private BufferedReader br;
	
	UserQnAMenu(BufferedReader br){
		this.br = br;
		menu();
	}

	private void menu() {
		int answer = Integer.MIN_VALUE;
		while(true) {
			System.out.println("문의 관련 페이지 입니다.");
			
			try {
				answer = Integer.parseInt(br.readLine());
			
				if(Util.checkValidNum(answer, 1)) {
					break;
				}else {
					System.out.println("1중 하나를 입력해주세요");
				}
			} catch (Exception e) {
				System.out.println("숫자만 입력해주세요.");
				continue;
			}
		}
		if(answer == 1) {
			
		}else if(answer == 2) {
			
		}else if(answer == 3) {
			
		}else if(answer == 0) {
			return;
		}
		
	}
}

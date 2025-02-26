package com.booking.menu;

import java.io.BufferedReader;

import com.booking.dto.User;
import com.booking.service.QnAService;
import com.booking.service.impl.QnAServiceImpl;
import com.util.Util;

public class UserQnAMenu {
	
	private BufferedReader br;
	private User user;
	private QnAService qnaService;
	
	UserQnAMenu(BufferedReader br, User user){
		this.br = br;
		this.user = user;
		qnaService = new QnAServiceImpl(br,user);
		menu();
	}

	private void menu() {
		int answer = Integer.MIN_VALUE;
		while(true) {
			System.out.println("문의 관련 페이지 입니다.");
			System.out.println("1. 문의하기");
			System.out.println("2. 내가한 문의 확인");
			System.out.println("3. 문의수정");
			System.out.println("0. 뒤로가기");
			try {
				answer = Integer.parseInt(br.readLine());
			
				if(Util.checkValidNum(answer, 1,2,3,0)) {
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
			qnaService.questionQNA();
		}else if(answer == 2) {
			qnaService.checkMyQNA();
		}else if(answer == 3) {
			qnaService.updateMyQNA();
		}else if(answer == 0) {
			return;
		}
		
	}
}

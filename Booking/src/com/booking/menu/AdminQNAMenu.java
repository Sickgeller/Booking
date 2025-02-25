package com.booking.menu;

import java.io.BufferedReader;

import com.booking.DAO.QnADAO;
import com.booking.DAO.impl.QnADAOImpl;
import com.booking.dto.Admin;
import com.booking.service.QnAService;
import com.booking.service.impl.QnAServiceImpl;
import com.util.Util;

public class AdminQNAMenu {
	
	private Admin admin;
	private BufferedReader br;
	private QnAService qnaService;
	
	public AdminQNAMenu(BufferedReader br , Admin admin) {
		this.br = br;
		this.admin = admin;
		this.qnaService = new QnAServiceImpl(br);
		menu();
	}
	
	private void menu() {
		int answer = Integer.MIN_VALUE;

		while(true) {
			System.out.println("문의 관련 페이지 입니다.");
			System.out.println("1. 미답변 QnA 답변하기");
			System.out.println("2. 답변한 QnA 수정하기");
			System.out.println("3. QnA 전체보기");
			System.out.println("0. 뒤로가기");

			try {
				answer = Integer.parseInt(br.readLine());
				if(Util.checkValidNum(answer, 1,2,3,0)) {
					break;
				}else {
					System.out.println("1,2,3,0 중 하나만 입력해주세요");
					continue;
				}
			} catch (Exception e) {
				System.out.println("숫자만 입력해주세요.");
				continue;
			}
		}

		if(answer == 1) {
			qnaService.answerToQNA(admin);
		} else if(answer == 2) {
			// 답변 수정 처리
			qnaService.updateQNA(admin);

		} else if(answer == 3) {
			qnaService.showAllQNA();
		}
	}

}

package com.booking.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.booking.DAO.QnADAO;
import com.booking.dto.Admin;
import com.booking.dto.QNA;
import com.booking.service.QnAService;

public class QnAServiceImpl implements QnAService {

	private QnADAO qnaDAO;
	private BufferedReader br;
	private List<Integer> list = new ArrayList<>();

	public QnAServiceImpl(QnADAO qnaDAO , BufferedReader br) {
		this.br = br;
		this.qnaDAO = qnaDAO;
	}	

	@Override
	public void answerToQNA(Admin admin) {
		int qnaId = -1;
		String adminId = admin.getID();
		List<QNA> answeredQnAList = qnaDAO.getUnansweredQNA(); // 어드민이 답변하지않은 qna 리스트
		if(answeredQnAList.isEmpty()) {
			System.out.println("답변하지않은 qna가 없습니다.");
			return;
		}
		for(QNA qna : answeredQnAList) {
			System.out.printf("%d번 문의 / 작성자 : %s / 문의주제 : %s / 문의날짜 : %s\n" , qna.getQnaId(), qna.getUserId() , qna.getSubjectName() , qna.getQnaQuestionedDate());
			System.out.printf("문의 내용 : %s\n" , qna.getQnaContent());
		}
		

		while (true) {
			try {
				System.out.print("답변할 QnA ID를 입력하세요: ");
				qnaId = Integer.parseInt(br.readLine());
				if(isValidQNA(answeredQnAList, qnaId)) {
					break;
					// 유효한 ID면 루프 탈출
				}else {
					System.out.println("유효하지않은 입력입니다.");
					continue;
				}
			} catch (NumberFormatException e) {
				System.out.println("숫자만 입력해주세요.");
				continue;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 수정할 답변을 입력받고 처리
		System.out.print("답변을 입력하세요: ");
		try {
			String newAnswer = br.readLine();
			boolean result = qnaDAO.answerToQNA(qnaId, newAnswer, admin.getID());  // DB에 답변 수정
			if (result) {
				System.out.println("답변이 성공적으로 수정되었습니다.");
			} else {
				System.out.println("답변 수정에 실패했습니다.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	// 유효한 QnA ID 확인 메서드
	private boolean isValidQNA(List<QNA> answeredQnAList, int qnaId) {
		for(QNA qna : answeredQnAList) {
			if(qna.getQnaId() == qnaId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateQNA(Admin admin) { // 답변 수정 메서드

		List<QNA> answeredQNA = qnaDAO.getAnsweredQNA(admin.getID()); // 어드민이 답변한 리스트
		if(answeredQNA.isEmpty()) {
			System.out.println("답변한 문의가 없습니다.");
			return;
		}
		
		for(QNA qna : answeredQNA) {
			System.out.printf("%d번 문의 / 작성자 : %s / 문의주제 : %s / 문의날짜 : %s\n" , qna.getQnaId(), qna.getUserId() , qna.getSubjectName() , qna.getQnaQuestionedDate());
			System.out.printf("문의 내용 : %s\n" , qna.getQnaContent());
			System.out.printf("답변자 : %s / 답변날짜 : %s / 답변내용 : %s\n" , qna.getAdminId() , qna.getQnaAnsweredDate() , qna.getQnaAnswer());
		}
		
		int qnaId = Integer.MIN_VALUE; // qnaId 초기

		while(!isValidQNA(answeredQNA, qnaId)) { // 유효한 qna_id인지 검증하는 while문
			try {
				System.out.print("수정할 QnA ID: ");
				qnaId = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
		}

		System.out.print("새로운 답변 내용: ");
		String newAnswer = null;
		try {
			newAnswer = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean success = qnaDAO.updateAnswer(qnaId, newAnswer);
		if (success) {
			System.out.println("답변이 수정되었습니다.");
		} else {
			System.out.println("답변 수정에 실패했습니다.");
		}

	}

	@Override
	public void showAllQNA() {
		// 모든 QnA 조회
		List<QNA> allQNA = qnaDAO.getAllQNA();
		if (allQNA.isEmpty()) {
			System.out.println("등록된 문의가 없습니다.");
		} else {
			for (QNA qna : allQNA) {
				System.out.println("QNA ID: " + qna.getQnaId());
				System.out.println("User ID: " + qna.getUserId());
				System.out.println("Subject: " + qna.getSubjectName());
				System.out.println("Content: " + qna.getQnaContent());
				System.out.println("Answered: " + (qna.getQnaAnswer() != null ? qna.getQnaAnswer() : "미답변"));
				System.out.println("-------------------------");
			}
		}
	}
}

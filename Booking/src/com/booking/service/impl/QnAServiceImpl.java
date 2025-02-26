package com.booking.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.booking.DAO.QnADAO;
import com.booking.DAO.impl.QnADAOImpl;
import com.booking.dto.Admin;
import com.booking.dto.QNA;
import com.booking.dto.User;
import com.booking.service.QnAService;

public class QnAServiceImpl implements QnAService {

	private QnADAO qnaDAO = new QnADAOImpl();;
	private BufferedReader br;
	private User user;
	
	public QnAServiceImpl(BufferedReader br) {
		this.br = br;
	}	

	public QnAServiceImpl(BufferedReader br, User user) {
		this.br = br;
		this.user = user;
		
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
		

		while (true) { // 답변할 문의 ID선택 메서드
			try {
				System.out.print("답변할 문의 ID를 입력하세요: ");
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

	@Override
	public void questionQNA() {
		int subject_id = selectQNASubject();
		String content = null;
		System.out.println("문의 내용을 입력해주세요");
		try {
			content = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean result = qnaDAO.questionQNA(subject_id, content, user);
		System.out.println(result ? "문의등록이 완료되었습니다" : "문의등록이 실패했습니다");
	}


	@Override
	public void checkMyQNA() {
		List<QNA> qnaList = qnaDAO.getMyQNA(user);
		showUserQNA(qnaList);
	}

	private void showUserQNA(List<QNA> qnaList) {
		for(QNA qna : qnaList) {
			System.out.printf("%d번 문의 / 작성자 : %s / 작성날짜 : %s / 문의주제 : %s \n" , qna.getQnaId() , qna.getUserId() , qna.getQnaQuestionedDate(), qna.getSubjectName());
			System.out.printf("문의 내용 : %s\n" , qna.getQnaContent());
			if(qna.getAdminId() == null) {
				System.out.println("답변되지않음");
			}else {
				System.out.printf("답변자 : %s / 답변날짜 : %s \n" , qna.getAdminId() , qna.getQnaAnsweredDate());
				System.out.printf("답변 내용 : %s\n", qna.getQnaAnswer());
			}
		}
	}

	@Override
	public void updateMyQNA() {
		List<QNA> qnaList = qnaDAO.getMyQNA(user);
		showUserQNA(qnaList);
		System.out.println("수정을 원하시는 문의 번호를 입력해주세요");
		List<Integer> idList = new ArrayList<>();
		for(QNA qna : qnaList) {
			idList.add(qna.getQnaId());
		}
		int qna_id = Integer.MIN_VALUE;
		while(!idList.contains(qna_id)) {
			try {
				qna_id = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
		}
		
		System.out.println("수정할 내용을 입력해주세요");
		String content = null;
		try {
			content = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean result = qnaDAO.updateQNA(qna_id, content);
		System.out.println(result ? "문의내용 수정완료": "문의내용수정실패");
		
		
	}
	private int selectQNASubject() { // 문의 주제를 선택하기위한 메서드
		Map<Integer,String> qnaSubject = qnaDAO.getQNASubject();
		int size = qnaSubject.size();
		for(int i = 1 ; i <= size ; i++) {
			System.out.printf("%d번주제 : %s \n", i, qnaSubject.get(i));
		}
		int sub_num = Integer.MIN_VALUE;
		while(!qnaSubject.keySet().contains(sub_num)) {
			try {
				System.out.println("원하시는 주제번호를 선택해주세요");
				sub_num = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
		}
		return sub_num;
	}
}

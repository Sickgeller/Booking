package com.booking.DAO;

import java.util.List;
import java.util.Map;

import com.booking.dto.QNA;
import com.booking.dto.User;

public interface QnADAO {
    List<QNA> getUnansweredQNA(); // 미답변된 문의 목록 조회
    boolean answerToQNA(int qna_id, String answer, String adminId); // QNA 답변 처리
    boolean updateAnswer(int qna_id, String newAnswer); // 답변 수정 처리
    List<QNA> getAnsweredQNA(String adminId); // 관리자가 답변한 QNA 목록 조회
    List<QNA> getAllQNA(); // 모든 QNA 조회
	Map<Integer, String> getQNASubject();
	boolean questionQNA(int subject_id, String content, User user);
	List<QNA> getMyQNA(User user);
	boolean updateQNA(int qna_id, String content);
}

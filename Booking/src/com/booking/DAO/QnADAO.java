package com.booking.DAO;

import java.util.List;
import com.booking.dto.QNA;

public interface QnADAO {
    List<QNA> getUnansweredQNA(); // 미답변된 문의 목록 조회
    boolean answerToQNA(int qna_id, String answer, String adminId); // QNA 답변 처리
    boolean updateAnswer(int qna_id, String newAnswer); // 답변 수정 처리
    List<QNA> getAnsweredQNA(String adminId); // 관리자가 답변한 QNA 목록 조회
    List<QNA> getAllQNA(); // 모든 QNA 조회
}

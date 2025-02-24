package com.booking.dto;

import java.util.Date;

public class QNA {
    private int qnaId; // QNA ID
    private String userId; // 사용자 ID
    private String subjectName; // 문의 제목
    private String qnaContent; // 문의 내용
    private Date qnaQuestionedDate; // 문의 날짜
    private String adminId; // 관리자 ID (답변한 관리자)
    private String qnaAnswer; // 답변 내용
    private Date qnaAnsweredDate; // 답변 날짜

    // 기본 생성자
    public QNA() {}

    // 답변이 없는 QNA 생성자
    public QNA(int qnaId, String userId, String subjectName, String qnaContent, Date qnaQuestionedDate) {
        this.qnaId = qnaId;
        this.userId = userId;
        this.subjectName = subjectName;
        this.qnaContent = qnaContent;
        this.qnaQuestionedDate = qnaQuestionedDate;
    }

    // 답변이 있는 QNA 생성자
    public QNA(int qnaId, String userId, String subjectName, String qnaContent, Date qnaQuestionedDate,
               String adminId, String qnaAnswer, Date qnaAnsweredDate) {
        this.qnaId = qnaId;
        this.userId = userId;
        this.subjectName = subjectName;
        this.qnaContent = qnaContent;
        this.qnaQuestionedDate = qnaQuestionedDate;
        this.adminId = adminId;
        this.qnaAnswer = qnaAnswer;
        this.qnaAnsweredDate = qnaAnsweredDate;
    }

    // Getter and Setter methods
    public int getQnaId() {
        return qnaId;
    }

    public void setQnaId(int qnaId) {
        this.qnaId = qnaId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getQnaContent() {
        return qnaContent;
    }

    public void setQnaContent(String qnaContent) {
        this.qnaContent = qnaContent;
    }

    public Date getQnaQuestionedDate() {
        return qnaQuestionedDate;
    }

    public void setQnaQuestionedDate(Date qnaQuestionedDate) {
        this.qnaQuestionedDate = qnaQuestionedDate;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getQnaAnswer() {
        return qnaAnswer;
    }

    public void setQnaAnswer(String qnaAnswer) {
        this.qnaAnswer = qnaAnswer;
    }

    public Date getQnaAnsweredDate() {
        return qnaAnsweredDate;
    }

    public void setQnaAnsweredDate(Date qnaAnsweredDate) {
        this.qnaAnsweredDate = qnaAnsweredDate;
    }
}

package com.booking.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.booking.DAO.QnADAO;
import com.booking.dto.QNA;
import com.booking.dto.User;
import com.dbutil.DBUtil;
import com.util.Util;

public class QnADAOImpl implements QnADAO {



	@Override
	public List<QNA> getUnansweredQNA() {
		List<QNA> unansweredQNA = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM QNA Q INNER JOIN QNA_SUBJECT QS ON Q.SUBJECT_ID = QS.SUBJECT_ID WHERE QNA_ANSWERED_STATUS = 0 ORDER BY QNA_ID";

		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				QNA qna = new QNA(
						rs.getInt("QNA_ID"),
						rs.getString("USER_ID"),
						rs.getString("SUBJECT_NAME"),
						rs.getString("QNA_CONTENT"),
						rs.getDate("QNA_QUESTIONED_DATE")
						);
				unansweredQNA.add(qna);
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return unansweredQNA;
	}

	@Override
	public boolean answerToQNA(int qna_id, String answer, String adminId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE QNA SET QNA_ANSWERED_STATUS = 1, QNA_ANSWER = ?, QNA_ANSWERED_DATE = SYSDATE, ADMIN_ID = ? WHERE QNA_ID = ?";

		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, answer);
			pstmt.setString(2, adminId);
			pstmt.setInt(3, qna_id);

			int result = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, result);
			return result == 1;

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return false;
	}

	@Override
	public boolean updateAnswer(int qna_id, String newAnswer) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE QNA SET QNA_ANSWER = ? WHERE QNA_ID = ?";

		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newAnswer);
			pstmt.setInt(2, qna_id);

			int result = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, result);
			return result == 1;
		} catch (SQLException | ClassNotFoundException e) {
			if(conn!=null) try {conn.rollback();}catch(Exception e1) {}
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return false;
	}

	@Override
	public List<QNA> getAnsweredQNA(String adminId) {
		List<QNA> answeredQNA = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM QNA Q INNER JOIN QNA_SUBJECT QS ON Q.SUBJECT_ID = QS.SUBJECT_ID WHERE QNA_ANSWERED_STATUS = 1 AND ADMIN_ID = ?";

		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, adminId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				QNA qna = new QNA(
						rs.getInt("QNA_ID"),
						rs.getString("USER_ID"),
						rs.getString("SUBJECT_NAME"),
						rs.getString("QNA_CONTENT"),
						rs.getDate("QNA_QUESTIONED_DATE"),
						rs.getString("ADMIN_ID"),
						rs.getString("QNA_ANSWER"),
						rs.getDate("QNA_ANSWERED_DATE")
						);
				answeredQNA.add(qna);
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return answeredQNA;
	}

	@Override
	public List<QNA> getAllQNA() {
		List<QNA> allQNA = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM QNA Q INNER JOIN QNA_SUBJECT QS ON Q.SUBJECT_ID = QS.SUBJECT_ID";

		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				QNA qna = new QNA(
						rs.getInt("QNA_ID"),
						rs.getString("USER_ID"),
						rs.getString("SUBJECT_NAME"),
						rs.getString("QNA_CONTENT"),
						rs.getDate("QNA_QUESTIONED_DATE"),
						rs.getString("ADMIN_ID"),
						rs.getString("QNA_ANSWER"),
						rs.getDate("QNA_ANSWERED_DATE")
						);
				allQNA.add(qna);
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return allQNA;
	}

	@Override
	public Map<Integer, String> getQNASubject() {
		Map<Integer,String> result = new HashMap<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM QNA_SUBJECT";

		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				do {
					int subjectId = rs.getInt(1);
					String subject = rs.getString(2);
					result.put(subjectId, subject);
				}while(rs.next());
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return result;
	}

	@Override
	public boolean questionQNA(int subject_id, String content, User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int update = Integer.MIN_VALUE;
		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO QNA(QNA_ID, SUBJECT_ID ,USER_ID, QNA_CONTENT) VALUES (QNA_SEQ.NEXTVAL, ?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subject_id);
			pstmt.setString(2, user.getID());
			pstmt.setString(3, content);
			update = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, update);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return update == 1;
	}

	@Override
	public List<QNA> getMyQNA(User user) { // 유저아이디에 맞는 qna리스트 반환
		List<QNA> myQNA = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM QNA Q INNER JOIN QNA_SUBJECT QS ON Q.SUBJECT_ID = QS.SUBJECT_ID "
				+ "WHERE USER_ID = ? ORDER BY QNA_ID";

		int qnaId; // QNA ID
		String userId = user.getID();
		String subjectName; // 문의 제목
		String qnaContent; // 문의 내용
		Date qnaQuestionedDate; // 문의 날짜
		String adminId; // 관리자 ID (답변한 관리자)
		String qnaAnswer; // 답변 내용
		Date qnaAnsweredDate; // 답변 날짜
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getID());
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				do {
					qnaId = rs.getInt("QNA_ID");
					subjectName = rs.getString("SUBJECT_NAME");
					qnaContent = rs.getString("QNA_CONTENT");
					qnaQuestionedDate = rs.getDate("QNA_QUESTIONED_DATE");
					adminId = rs.getString("ADMIN_ID");
					qnaAnswer = rs.getString("QNA_ANSWER");
					qnaAnsweredDate = rs.getDate("QNA_ANSWERED_DATE");
					
					myQNA.add(new QNA(qnaId,userId,subjectName, qnaContent, qnaQuestionedDate
							, adminId, qnaAnswer, qnaAnsweredDate));
				}while(rs.next());
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return myQNA;
	}

	@Override
	public boolean updateQNA(int qna_id, String content) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE QNA SET QNA_CONTENT = ? WHERE QNA_ID = ?";
		int result = Integer.MIN_VALUE;
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, content);
			pstmt.setInt(2, qna_id);

			result = pstmt.executeUpdate();
			Util.doCommitOrRollback(conn, result);
			return result == 1;
		} catch (SQLException | ClassNotFoundException e) {
			if(conn!=null) try {conn.rollback();}catch(Exception e1) {}
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return result == 1;
	}
}

package com.booking.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.booking.DAO.QnADAO;
import com.booking.dto.Admin;
import com.booking.dto.QNA;
import com.dbutil.DBUtil;

public class QnADAOImpl implements QnADAO {
	
	
	private Admin admin;
	
	
    public QnADAOImpl(Admin admin) {
    	super();
    	this.admin = admin;
	}

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

            int update = pstmt.executeUpdate();
            return update == 1;

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

            int update = pstmt.executeUpdate();
            return update == 1;

        } catch (SQLException | ClassNotFoundException e) {
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
}

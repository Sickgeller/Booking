package com.booking.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.booking.DAO.PaymentDAO;
import com.booking.dto.User;
import com.dbutil.DBUtil;
import com.util.Util;

public class PaymentDAOImpl implements PaymentDAO{

	private User user;
	
	public PaymentDAOImpl(User user) {
		this.user = user;
	}

	@Override
	public boolean updateCashPayment(int cash) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE \"USER\" SET CASH = ? WHERE USER_ID = ?";
        int update = Integer.MIN_VALUE;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cash);
            pstmt.setString(2, user.getID());
            update = pstmt.executeUpdate();
            Util.doCommitOrRollback(conn, update);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DBUtil.executeClose(null, pstmt, conn);
        }
		return update == 1;
	}

}

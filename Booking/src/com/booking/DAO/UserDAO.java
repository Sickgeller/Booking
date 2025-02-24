package com.booking.DAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;

import com.booking.dto.Grade;
import com.booking.dto.User;
import com.booking.menu.UserMenu;
import com.dbutil.DBUtil;

public class UserDAO {

	
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	public User login(String ID, String passwd) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		

		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT * FROM \"USER\" WHERE USER_ID = ? AND PASSWORD = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			pstmt.setString(2, passwd);
			rs = pstmt.executeQuery();
			

			if(rs.next()) {
				// 회원 ID는 하나이기 떄문에 do-while 사용 X
				System.out.println("로그인 성공");
				String name = rs.getString("NAME");
				String email = rs.getString("EMAIL");
				int point = rs.getInt("POINT");
				int cash = rs.getInt("CASH");
				Date reg_date = rs.getDate("REG_DATE");
				
				// boolean status = rs.getInt("STATUS") == 0 ? false : true;
				Enum grade = Grade.valueOf(rs.getString("USER_GRADE"));
				return new User(ID, email, passwd, name, grade, point,cash,reg_date);
				//String iD, String email, String passwd, String name, Enum grade, int point, int cash, Date reg_date
				// status
			}else {
				System.out.println("잘못된 ID나 잘못된 passwd입니다");
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}

		return null;
	}



	public static boolean checkIDDuplicate(String ID) { // 증복 아이디 확인
		
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs =  null;
		
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT COUNT(ID) FROM MEMBER WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return false;
	}

	public boolean register(String ID, String passwd, String name, String email) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {	
			conn = DBUtil.getConnection();
			sql = "INSERT INTO MEMBER (ID,PASSWORD, NAME, EMAIL) VALUES (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			int count = pstmt.executeUpdate(sql);
			return count == 1 ? true :false; //false 는 ID로 인한 이유가 아닌 다른 이유로 발생할것(한번 중복을 걸렀기때문)
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return false;
	}
	public void changeUserName(String ID, String name) {
		Connection conn = null;
		PreparedStatement pstmtU = null;
		String sqlU = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			sqlU =  "UPDATE \"USER\" SET NAME=? WHERE USER_ID=?";
			pstmtU = conn.prepareStatement(sqlU);
			pstmtU.setString(1, name);
			pstmtU.setString(2, ID);
			int update = pstmtU.executeUpdate();
			if(update == 1) {
				 conn.commit();
				System.out.println("이름 변경 완료");
			}
			else {
				 conn.rollback();
				 System.out.println("사용자가 없습니다.");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				if(conn!=null)conn.rollback();
			}
			catch(SQLException e1){
				
				e1.printStackTrace();
			}
		}finally {
			DBUtil.executeClose(null, pstmtU, conn);
		}		
		
	}
	public void changeUserEmail(String ID, String email) {
		Connection conn = null;
		PreparedStatement pstmtU = null;
		String sqlU = null;
		try {
			conn = DBUtil.getConnection();
			sqlU =  "UPDATE \"USER\" SET EMAIL=? WHERE USER_ID=?";
			pstmtU = conn.prepareStatement(sqlU);
			pstmtU.setString(1, email);
			pstmtU.setString(2, ID);
			int update = pstmtU.executeUpdate();
			if(update == 1)
			{
				 conn.commit();
				System.out.println("이메일 변경 완료");
			}
			else {
				 conn.rollback();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		try {	conn.rollback(); }catch(SQLException e1){}
		finally {DBUtil.executeClose(null, pstmtU, conn);}
		}
	}

	public void changeUserPW(String ID, String passwd,BufferedReader br) {
		Connection conn = null;
		PreparedStatement pstmtU = null;
		String sqlU = null;
		String passWd2;
		try {
			conn = DBUtil.getConnection();
			System.out.println("변경할 비밀번호를 한번 더 입력하세요.");
			passWd2 = br.readLine();
			if(passWd2.equals(passwd)) {
				sqlU =  "UPDATE \"USER\" SET PASSWORD=? WHERE USER_ID=?";
				pstmtU = conn.prepareStatement(sqlU);
				pstmtU.setString(1, passwd);
				pstmtU.setString(2, ID);
				int update = pstmtU.executeUpdate();
				if(update == 1) {
					conn.commit();
					System.out.println("비밀번호가 성공적으로 변경 됐습니다.");
				}
				else {
					conn.rollback();
					System.out.println("비밀번호가 변경 실패");

				}
			}else {
				System.out.println("재입력한 비밀번호가 일치하지 않습니다.");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}finally {
			DBUtil.executeClose(null, pstmtU, conn);
		}
	}

		
		
	
	public void checkGrade(String ID, Enum<?> Grade) {
		Connection conn = null;
		PreparedStatement pstmtS = null;
		String sqlS = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			sqlS =  "SELECT USER_GRADE FROM \"USER\" WHERE USER_ID=? AND USER_GRADE=?";
			pstmtS = conn.prepareStatement(sqlS);
			pstmtS.setString(1, ID);
			pstmtS.setString(2, Grade.name());
			
			
			rs = pstmtS.executeQuery();
			if(rs.next()) {
				 System.out.print("사용자의 등급은 = " + rs.getString("USER_GRADE") + "\n");
			}
			else {
				System.out.println("해당 등급의 사용자가 없습니다.");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally {DBUtil.executeClose(null, pstmtS, conn);}
	}
	
	public int showCash(String ID, int cash) {
		return 0;
	}
	
    public void chargeCash(String ID){
		
	}
    public void deleteUser(String ID,BufferedReader br) {
    	Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();
			System.out.println("데이터 삭제 준비 완료");
			System.out.println("정말로 삭제하시겠습니까? 삭제하시면 돌이킬 수 없습니다.");
			System.out.println("삭제를 원하시면 y 아니면 n을 입력하세요.");
			char answer = br.readLine().charAt(0);
			try {
				if(answer == 'y') {
					 
					 
				     sql = "DELETE FROM \"USER\" WHERE USER_ID=?";
				     pstmt = conn.prepareStatement(sql);
				     pstmt.setString(1, ID);
				     
				     int update = pstmt.executeUpdate();
				     if(update == 1) {
				    	 conn.commit();
				    	 System.out.println("사용자가 삭제되었습니다.");
				    	 System.out.println("프로그램을 종료합니다");
				    	 System.exit(0); 
				     }
				     else if(update == 0) {
				    	 conn.rollback();
				    	 System.out.println("계정 삭제가 실패했습니다.");
				     }
				}else if(answer == 'n') {
					
				}
			} catch (InputMismatchException | IllegalArgumentException | StringIndexOutOfBoundsException e) {
				e.printStackTrace();
				System.out.println("y/n글자만 입력하세요");
			} 
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			// 자원 정리
			DBUtil.executeClose(null, pstmt, conn);
			
		}
    }
}

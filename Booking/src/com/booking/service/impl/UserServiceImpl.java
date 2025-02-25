package com.booking.service.impl;

import java.io.BufferedReader;
import java.io.IOException;

import com.booking.DAO.UserDAO;
import com.booking.DAO.impl.UserDAOImpl;
import com.booking.dto.User;
import com.booking.service.UserService;
import com.dbutil.DBUtil;
import com.util.Util;

public class UserServiceImpl implements UserService{
	private UserDAO userDAO;
	private BufferedReader br;

	public UserServiceImpl(BufferedReader br){
		this.br = br;
		userDAO = new UserDAOImpl();
	}

	@Override
	public User login(String ID, String passwd) {
		return userDAO.login(ID, passwd);
	}

	@Override
	public boolean checkIDDuplicate(String ID) {
		return userDAO.checkIDDuplicate(ID);
	}

	@Override
	public void register() {
		try {
			System.out.println("회원가입 모드입니다.");
			String ID = null;
			while(true) { // 중복ID 유효성검사 루프문
				System.out.println("회원가입할 ID를 입력해주세요");
				ID = br.readLine();
				if(checkIDDuplicate(ID)) {
					System.out.println("중복된 ID입니다.");
					continue;
				}else {
					break;
				}
			}
			System.out.println("비밀번호를 입력해주세요");
			String passwd = br.readLine();

			System.out.println("이름을 입력해주세요");
			String name = br.readLine();

			String email = null;

			while(true) { // 이메일 유효성검사 루프문
				System.out.println("이메일을 입력해주세요");
				email = br.readLine();
				if(!email.matches(Util.emailFormat)) {
					System.out.println("잘못된 이메일 형식입니다.");
					continue;
				}else {
					break;
				}
			}

			if(userDAO.register(ID, passwd, name, email)) {
				System.out.println("회원가입에 성공했습니다.");
			}else {
				System.out.println("회원가입에 실패했습니다.");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void changeUserName(String ID) {
		System.out.println("이름을 변경하세요 : ");
		String name = null;
		try {
			name = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean result = userDAO.changeUserName(ID, name);

		if(result) {
			System.out.println("이름 변경 완료");
		}else {
			System.out.println("사용자가 없습니다.");
		}
	}

	@Override
	public void changeUserEmail(String ID) {
		String email = null;
		while(true) {
			try {
				System.out.println("이메일을 변경하세요 : ");
				email = br.readLine();
				if(email.matches(Util.emailFormat)) {
					break;
				}else {
					System.out.println("이메일의 형식이 아닙니다.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		boolean result = userDAO.changeUserEmail(ID, email);
		if(result) {
			System.out.println("이메일 변경 완료");
		}else {
			System.out.println("이메일 변경 실패");
		}
	}

	@Override
	public void changeUserPW(String ID) {
		try {
			System.out.println("비밀번호 변경");
			System.out.println("변경할 비밀번호 입력 :");
			String passwd = br.readLine();
			boolean result = userDAO.changeUserPW(ID,passwd);
			if(result) {
				System.out.println("비밀번호 변경이 완료되었습니다.");
			}else {
				System.out.println("비밀번호 변경이 실패했습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteUser(String ID) {
		System.out.println("데이터 삭제 준비 완료");
		System.out.println("정말로 삭제하시겠습니까? 삭제하시면 돌이킬 수 없습니다.");
		System.out.println("삭제를 원하시면 y 아니면 n을 입력하세요.");
		while(true) {
			try {
				char answer = br.readLine().charAt(0);
				if(answer == 'y') {
					boolean result = userDAO.deleteUser(ID);
					if(result) {
						System.out.println("사용자가 삭제되었습니다.");
						System.out.println("프로그램을 종료합니다");
						System.exit(0);
					}else {
						System.out.println("사용자 삭제가 실패했습니다.");
						return;
					}
				}else if(answer == 'n') {
					System.out.println("사용자 삭제를 취소합니다.");
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void checkUserGrade(String ID) {
		System.out.println("등급 확인");
		String grade = userDAO.checkUserGrade(ID);
		if(grade != null) System.out.printf("사용자의 등급은 %s입니다" , grade);
		else System.out.println("해당하는 사용자가 업습니다.");
	}

	@Override
	public void chargeAccount(String ID, int money) {
		int chargeCash = Integer.MIN_VALUE;
		while(true) {
			try {
				System.out.println("금액 충전");
				System.out.println("충전할 금액을 입력하세요.");
				chargeCash = Integer.parseInt(br.readLine());
				if(chargeCash > 0)  break;
			}catch(NumberFormatException | IOException e) {
				e.printStackTrace();
				System.out.println("숫자만 입력하세요 ");
				continue;
			}

			boolean result = userDAO.chargeAccount(ID, money);
			if(result) {
				System.out.println("충전 성공 !");
			}else {
				System.out.println("충전 실패 ! ! ! ");
			}
		}
	}
}

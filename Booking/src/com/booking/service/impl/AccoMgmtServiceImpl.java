package com.booking.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.booking.DAO.AccoMgmtDAO;
import com.booking.DAO.impl.AccoMgmtDAOImpl;
import com.booking.dto.Accommodation;
import com.booking.dto.Admin;
import com.booking.service.AccoMgmtService;

public class AccoMgmtServiceImpl implements AccoMgmtService{

	private Admin admin;
	private BufferedReader br;
	private AccoMgmtDAO accoMgmtDAO = new AccoMgmtDAOImpl();
	
	public AccoMgmtServiceImpl(Admin admin, BufferedReader br) {
		this.admin = admin;
		this.br = br;
	}

	@Override
	public void accommodation_suspension() {
		List<Accommodation> acco = accoMgmtDAO.getNormalAcco();
		if(acco.isEmpty()) {
			System.out.println("영업중인 숙소가 없습니다.");
			return;
		}
		for(Accommodation acco_tmp : acco) {
			System.out.printf("숙소번호 : %d , 숙소이름 : %s , 영업상태 : %s\n" , acco_tmp.getAccommodatin_id(), acco_tmp.getAccommodation_name(), acco_tmp.getAccommodation_status() == 1 ?" 영업중 " : "영업정지중");
		}
		
		int acco_num = Integer.MIN_VALUE;
		while(true) {
			System.out.println("영업정지를 원하는 숙소번호를 입력해주세요");
			try {
				acco_num = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
			if(validAccoNum(acco_num, acco)) {
				break;
			}else {
				System.out.println("유효하지않은 숙소번호입니다.");
				continue;
			}
		}
		System.out.println("영업정지 사유를 입력해주세요");
		String reason = null;
		try {
			reason = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean result = accoMgmtDAO.accommodation_suspension(acco_num, admin, reason);
		if(result) {
			System.out.println("영업정지 처리가 완료되었습니다.");
		}else {
			System.out.println("영업정지 처리가 실패했습니다.");
		}
	}

	@Override
	public void accommodation_resume() {
		List<Accommodation> susAccoList = accoMgmtDAO.getSusAcco();
		if(susAccoList.isEmpty()) {
			System.out.println("영업정지중인 숙소가 없습니다.");
			return;
		}
		for(Accommodation acco : susAccoList) {
			System.out.printf("숙소번호 : %d , 숙소이름 : %s , 영업상태 : %s\n" , acco.getAccommodatin_id(), acco.getAccommodation_name(), acco.getAccommodation_status() == 1 ?" 영업중 " : "영업정지중");
		}
		
		int acco_num = Integer.MIN_VALUE;
		while(true) {
			System.out.println("영업재개를 원하는 숙소번호를 입력해주세요");
			try {
				acco_num = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("숫자만 입력해주세요");
				continue;
			}
			if(validAccoNum(acco_num, susAccoList)) {
				break;
			}else {
				System.out.println("유효하지않은 숙소번호입니다.");
				continue;
			}
		}
		System.out.println("영업재개 사유를 입력해주세요");
		String reason = null;
		try {
			reason = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean result = accoMgmtDAO.accommodation_resume(acco_num, admin, reason);
		if(result) {
			System.out.println("영업재개 처리가 완료되었습니다.");
		}else {
			System.out.println("영업재개 처리가 실패했습니다.");
		}
	}
	
	private boolean validAccoNum(int acco_num, List<Accommodation> acco) {
		for(Accommodation accoEle : acco) {
			if(accoEle.getAccommodatin_id() == acco_num) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isValidAccmNum(int accm_num) {
		return accoMgmtDAO.isValidAccmNum(accm_num);
	}
	

}

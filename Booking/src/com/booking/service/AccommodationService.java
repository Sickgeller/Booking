package com.booking.service;

import java.util.List;
import java.util.Map;

public interface AccommodationService {

	void showEveryAcco();
	void insertAccommodation();
	Map<List<Integer>, Integer> showDomesticInfo();
	void showDetailInfo(int accommodation_id);
	Map<List<Integer>, Integer> showOverseasInfo(); // 디테일 하게 봤는지, 아이디 여부
	void domestic();
	void oversea();
}

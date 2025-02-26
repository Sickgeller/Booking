package com.booking.DAO;

import java.util.List;

import com.booking.dto.Accommodation;

public interface AccommodationDAO {
	
	List<Accommodation> getOverseasAccommodation();
	List<Accommodation> getDomesticAccommodation();
	List<Accommodation> getDomesticAccommodation(String local_name);
	boolean insertAccommodation(String accommodation_name, String accommodation_address, String accommodation_description,
			int accommodation_price, String location_name, String recommendation_season, int accommodation_status, int allowed_number);

	Accommodation suggest_accommodation(String local_name, String season);
	List<Accommodation> getEveryAccommodation();
	Accommodation getAccommodationInfo(int accommodation_id);
	
}

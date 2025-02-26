package com.booking.service.impl;

import java.io.BufferedReader;

import com.booking.dto.User;
import com.booking.service.ReservationService;

public class ReservationServiceImpl implements ReservationService{

	private BufferedReader br;
	private User user;
	
	public ReservationServiceImpl(BufferedReader br, User user) {
		super();
		this.br = br;
		this.user = user;
	}

	@Override
	public void reservateOverseas() {
		
	}

	@Override
	public void reservateDomestic() {
		
	}

	@Override
	public void checkMyReservation() {
		
	}

	
	
}

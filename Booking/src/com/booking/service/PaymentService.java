package com.booking.service;

import java.util.List;

public interface PaymentService {
	List<Integer> selectPaymentTarget();

	boolean processPayment();
	boolean paymentWithPoint();
}

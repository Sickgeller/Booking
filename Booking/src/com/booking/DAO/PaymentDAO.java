package com.booking.DAO;

import java.util.List;

import com.booking.dto.Payment;

public interface PaymentDAO {

	boolean updateCashPayment(int cash);
	List<Payment> getPaymentHistory();

}

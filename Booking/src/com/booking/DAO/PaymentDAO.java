package com.booking.DAO;

import java.util.List;

import com.booking.dto.Payment;
import com.booking.dto.Reservation;

public interface PaymentDAO {

	boolean updateCashPayment(int cash);
	List<Payment> getPaymentHistory();
	boolean insertPayment(int reservationId, int reservationPrice, int i, int reservationPrice2, int j);
	List<Reservation> getUnpaidReservation(String id);

}

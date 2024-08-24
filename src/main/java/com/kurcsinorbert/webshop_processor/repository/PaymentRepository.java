package com.kurcsinorbert.webshop_processor.repository;

import com.kurcsinorbert.webshop_processor.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p.webshopId, " +
            "SUM(CASE WHEN UPPER( p.paymentMethod) = :card THEN p.amount ELSE 0 END) AS cardPayments, " +
            "SUM(CASE WHEN UPPER(p.paymentMethod) = :transfer THEN p.amount ELSE 0 END) AS transferPayments " +
            "FROM Payment p GROUP BY p.webshopId")
    List<Object[]> findWebshopReportData(String card, String transfer);
}

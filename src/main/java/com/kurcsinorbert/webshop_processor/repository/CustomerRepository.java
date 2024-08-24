package com.kurcsinorbert.webshop_processor.repository;

import com.kurcsinorbert.webshop_processor.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query("SELECT c.name, c.address, SUM(p.amount) AS totalSpent " +
            "FROM Customer c LEFT JOIN Payment p " +
            "ON c.customerId = p.customerId AND c.webshopId = p.webshopId " +
            "GROUP BY c.customerId, c.webshopId")
    List<Object[]> findCustomerReportData();

    @Query("SELECT c.name, c.address, SUM(p.amount) AS totalSpent " +
            "FROM Customer c LEFT JOIN Payment p " +
            "ON c.customerId = p.customerId AND c.webshopId = p.webshopId " +
            "GROUP BY c.customerId, c.webshopId " +
            "ORDER BY totalSpent DESC")
    Page<Object[]> findTopCustomers(Pageable pageable);
}

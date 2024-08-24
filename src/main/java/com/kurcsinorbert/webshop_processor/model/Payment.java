package com.kurcsinorbert.webshop_processor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue
    private Long id;
    private String webshopId;
    private String customerId;
    private String paymentMethod;
    private BigDecimal amount;
    private String accountNumber;
    private String cardNumber;
    private String paymentDate;
}

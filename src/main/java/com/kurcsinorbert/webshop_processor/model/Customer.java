package com.kurcsinorbert.webshop_processor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "customers")
@IdClass(CustomerId.class)
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    private String customerId;
    @Id
    private String webshopId;
    private String name;
    private String address;
}

package com.kurcsinorbert.webshop_processor.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerId implements Serializable {
    private String webshopId;
    private String customerId;
}

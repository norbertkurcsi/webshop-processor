package com.kurcsinorbert.webshop_processor.processor;

import com.kurcsinorbert.webshop_processor.model.Customer;
import org.springframework.batch.item.ItemProcessor;

public class ReportProcessor implements ItemProcessor<Customer, String> {

    @Override
    public String process(Customer customer) throws Exception {
        // Generate report data for the customer
        return customer.getName() + "," + customer.getAddress() + ",Total Purchase Amount";
    }
}

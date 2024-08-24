package com.kurcsinorbert.webshop_processor.processor;

import com.kurcsinorbert.webshop_processor.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomerItemProcessor implements ItemProcessor<Customer,Customer> {
    private static final Logger logger = LoggerFactory.getLogger(CustomerItemProcessor.class);
    @Override
    public Customer process(Customer customer) throws Exception {
        if (customer.getCustomerId() == null || customer.getCustomerId().isEmpty()) {
            logger.error("Invalid customer record: customerId is missing. Record: {}", customer);
            return null;
        }
        if (customer.getWebshopId() == null || customer.getWebshopId().isEmpty()) {
            logger.error("Invalid customer record: webshopId is missing. Record: {}", customer);
            return null;
        }
        if (customer.getName() == null || customer.getName().isEmpty()) {
            logger.error("Invalid customer record: name is missing. Record: {}", customer);
            return null;
        }
        if (customer.getAddress() == null || customer.getAddress().isEmpty()) {
            logger.error("Invalid customer record: address is missing. Record: {}", customer);
            return null;
        }

        return customer;
    }
}

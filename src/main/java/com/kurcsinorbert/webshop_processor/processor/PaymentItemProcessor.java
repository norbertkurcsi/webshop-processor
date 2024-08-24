package com.kurcsinorbert.webshop_processor.processor;

import com.kurcsinorbert.webshop_processor.model.Payment;
import com.kurcsinorbert.webshop_processor.model.enums.PaymentMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PaymentItemProcessor implements ItemProcessor<Payment,Payment> {
    private static final Logger logger = LoggerFactory.getLogger(CustomerItemProcessor.class);
    @Override
    public Payment process(Payment payment) throws Exception {
        if (payment.getCustomerId() == null || payment.getCustomerId().isEmpty()) {
            logger.error("Invalid payment record: customerId is missing. Record: {}", payment);
            return null;
        }
        if (payment.getWebshopId() == null || payment.getWebshopId().isEmpty()) {
            logger.error("Invalid payment record: webshopId is missing. Record: {}", payment);
            return null;
        }
        if (payment.getAmount() == null) {
            logger.error("Invalid payment record: amount is missing. Record: {}", payment);
            return null;
        }
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isEmpty()) {
            logger.error("Invalid payment record: paymentMethod is missing. Record: {}", payment);
            return null;
        }
        try {
            if(PaymentMethod.valueOf(payment.getPaymentMethod().toUpperCase()) == PaymentMethod.CARD){
                if (payment.getCardNumber() == null || payment.getCardNumber().isEmpty()) {
                    logger.error("Invalid payment record: cardNumber is missing. Record: {}", payment);
                    return null;
                }
            } else if (PaymentMethod.valueOf(payment.getPaymentMethod().toUpperCase()) == PaymentMethod.TRANSFER) {
                if (payment.getAccountNumber() == null || payment.getAccountNumber().isEmpty()) {
                    logger.error("Invalid payment record: accountNumber is missing. Record: {}", payment);
                    return null;
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid payment record: paymentMethod value is not correct. Record: {}", payment);
            return null;
        }

        if (payment.getPaymentDate() == null || payment.getPaymentDate().isEmpty()) {
            logger.error("Invalid payment record: paymentDate is missing. Record: {}", payment);
            return null;
        } else {
            try {
                LocalDate.parse(payment.getPaymentDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            } catch (DateTimeParseException e) {
                logger.error("Invalid payment record: paymentDate has incorrect format. Record: {}", payment);
                return null;
            }
        }

        return payment;
    }
}
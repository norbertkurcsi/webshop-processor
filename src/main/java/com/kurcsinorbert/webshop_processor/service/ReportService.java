package com.kurcsinorbert.webshop_processor.service;

import com.kurcsinorbert.webshop_processor.model.enums.PaymentMethod;
import com.kurcsinorbert.webshop_processor.repository.CustomerRepository;
import com.kurcsinorbert.webshop_processor.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    public void generateReports() throws IOException {
        generateCustomerReport();
        generateTopCustomersReport();
        generateWebshopReport();
    }

    private void generateCustomerReport() throws IOException {
        List<Object[]> results = customerRepository.findCustomerReportData();

        String filePath = "result" + File.separator + "report01.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("NAME,ADDRESS,vásárlás összesen\n");
            for (Object[] row : results) {
                String name = (String) row[0];
                String address = (String) row[1];
                BigDecimal totalSpent = (BigDecimal) row[2];
                if (totalSpent != null) {
                    writer.append(String.format("%s,%s,%s\n", name, address, totalSpent));
                }
            }
        }
    }

    private void generateTopCustomersReport() throws IOException {
        Pageable top2 = PageRequest.of(0, 2);
        var page = customerRepository.findTopCustomers(top2);

        String filePath = "result" + File.separator + "top.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("NAME,ADDRESS,vásárlás összesen\n");
            for (Object[] row : page.getContent()) {
                String name = (String) row[0];
                String address = (String) row[1];
                BigDecimal totalSpent = (BigDecimal) row[2];
                writer.append(String.format("%s,%s,%s\n", name, address, totalSpent));
            }
        }
    }

    private void generateWebshopReport() throws IOException {
        List<Object[]> results = paymentRepository.findWebshopReportData(PaymentMethod.CARD.name(), PaymentMethod.TRANSFER.name());

        String filePath = "result" + File.separator + "report02.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("WEBSHOP,kártyás vásárlások összege,átutalásos vásárlások összege\n");
            for (Object[] row : results) {
                String webshopId = (String) row[0];
                BigDecimal cardPayments = (BigDecimal) row[1];
                BigDecimal transferPayments = (BigDecimal) row[2];
                writer.append(String.format("%s,%s,%s\n", webshopId, cardPayments, transferPayments));
            }
        }
    }
}

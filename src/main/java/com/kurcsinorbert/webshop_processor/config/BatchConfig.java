package com.kurcsinorbert.webshop_processor.config;

import com.kurcsinorbert.webshop_processor.model.Customer;
import com.kurcsinorbert.webshop_processor.model.Payment;
import com.kurcsinorbert.webshop_processor.processor.CustomerItemProcessor;
import com.kurcsinorbert.webshop_processor.processor.PaymentItemProcessor;
import com.kurcsinorbert.webshop_processor.service.ReportService;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final EntityManagerFactory entityManagerFactory;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    private final ReportService reportService;

    @Bean
    public FlatFileItemReader<Customer> customerItemReader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .resource(new ClassPathResource("customer.csv"))
                .delimited()
                .delimiter(";")
                .names(new String[] {"webshopId", "customerId", "name", "address"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Customer.class);
                }})
                .build();
    }

    @Bean
    public FlatFileItemReader<Payment> paymentItemReader() {
        return new FlatFileItemReaderBuilder<Payment>()
                .name("customerItemReader")
                .resource(new ClassPathResource("payments.csv"))
                .delimited()
                .delimiter(";")
                .names(new String[] {"webshopId", "customerId", "paymentMethod", "amount", "accountNumber", "cardNumber", "paymentDate"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Payment.class);
                }})
                .build();
    }

    @Bean
    public JpaItemWriter<Customer> customerItemWriter() {
        return new JpaItemWriterBuilder<Customer>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public JpaItemWriter<Payment> paymentItemWriter() {
        return new JpaItemWriterBuilder<Payment>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public Step importCustomerStep() {
        return new StepBuilder("importCustomerStep", jobRepository)
                .<Customer, Customer>chunk(10, transactionManager)
                .reader(customerItemReader())
                .processor(new CustomerItemProcessor())
                .writer(customerItemWriter())
                .build();
    }

    @Bean
    public Step importPaymentStep() {
        return new StepBuilder("importPaymentStep", jobRepository)
                .<Payment, Payment>chunk(10, transactionManager)
                .reader(paymentItemReader())
                .processor(new PaymentItemProcessor())
                .writer(paymentItemWriter())
                .build();
    }

    @Bean
    public Step generateReportsStep() {
        return new StepBuilder("generateReportsStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    reportService.generateReports();
                    return null;
                }, transactionManager)
                .build();
    }

    @Bean
    public Job reportJob() {
        return new JobBuilder("reportJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(importCustomerStep())
                .next(importPaymentStep())
                .next(generateReportsStep())
                .build();
    }

}

package com.example.excelreader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfig {
    @Bean
    public ItemReader<StudentDTO> excelReader() {
        PoiItemReader<StudentDTO> reader = new PoiItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource("data/students.xlsx"));
        reader.setRowMapper(excelRowMapper());
        return reader;
    }

    @Bean
    public ItemWriter<StudentDTO> writer(ConsoleItemWriter consoleItemWriter) {
        return consoleItemWriter;
    }

    private RowMapper<StudentDTO> excelRowMapper() {
        BeanWrapperRowMapper<StudentDTO> rowMapper = new BeanWrapperRowMapper<>();
        rowMapper.setTargetType(StudentDTO.class);
        return rowMapper;
    }

    @Bean
    public Job excelJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("excelReaderJob", jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, ConsoleItemWriter consoleItemWriter) {
        return new StepBuilder("step", jobRepository)
                .<StudentDTO, StudentDTO>chunk(1000, transactionManager)
                .reader(excelReader())
                .writer(writer(consoleItemWriter))
                .build();
    }
}

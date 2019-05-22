package com.grayraccoon.sample.batchpractice.config;

import com.grayraccoon.sample.batchpractice.domain.People;
import com.grayraccoon.sample.batchpractice.processor.PeopleItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import javax.batch.api.listener.JobListener;
import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    @Primary
    public FlatFileItemReader<People> peopleCSVReader() {
        return new FlatFileItemReaderBuilder<People>()
                .name("peopleItemReader")
                .resource(new ClassPathResource("input/csv/sample.csv"))
                .delimited()
                .names(new String[] {"first_name", "last_name", "email"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<People>(){{
                    setTargetType(People.class);
                }})
                .build();
    }

    @Bean
    public PeopleItemProcessor peopleItemProcessor() {
        return new PeopleItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<People> peopleItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<People>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name, email) VALUES (:first_name, :last_name, :email)")
                .dataSource(dataSource)
                .build();
    }


    @Bean
    public Step ourStep1(JdbcBatchItemWriter<People> writer) {
        return stepBuilderFactory.get("ourStep1")
                .<People, People> chunk(2)      // TODO: externalize chunk size.
                .reader(peopleCSVReader())
                .processor(peopleItemProcessor())
                .writer(writer)
                .build();
    }

    @Bean
    public Job importPeopleFromCSVJob(JobExecutionListener listener, Step ourStep1) {
        return jobBuilderFactory.get("importPeopleFromCSVJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(ourStep1)
                .end()
                .build();
    }

}

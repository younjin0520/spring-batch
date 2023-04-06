package com.spring.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tasklet은 하나의 메소드로 구성되어 있는 인터페이스로 Step 안에서 수행될 기능들을 명시
 * Tasklet 하나 = Reader + Processor + Writer
 */
@Slf4j
@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class TaskletJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job TaskletJob(){
        Job customJob = jobBuilderFactory.get("taskletJob").start(TaskStep()).build();
        return customJob;
    }

    @Bean
    public Step TaskStep(){
        return stepBuilderFactory.get("taskletStep1")
                .tasklet(new CustomTasklet()).build();
    }
}

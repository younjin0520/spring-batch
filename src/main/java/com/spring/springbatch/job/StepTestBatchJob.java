package com.spring.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * StartLimit : 재시작 횟수 제한
 * 외부와의 통신, DB 작업 중 연결이 끊기면서 작업이 실패하는 경우, 배치를 재시작 해야함
 * -> 기본 재시작 가능 횟수 : 1회
 * -> startLimit()으로 횟수 지정 가능
 * -> 지정 횟수를 초과하면 Exception 발생
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepTestBatchJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job startLimitJob() {
        return jobBuilderFactory.get("startLimitJob")
                .start(startLimitStep())
                .build();
    }

    @Bean
    @JobScope
    public Step startLimitStep() {
        return stepBuilderFactory.get("startLimitStep")
                .startLimit(3)  // 재시작 3번 가능
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>> This is SimpleStep1");
                    int error = 5/0;    // 에러 발생 코드
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}

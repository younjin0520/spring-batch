//package com.spring.springbatch.job;
//
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.JobScope;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.item.*;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.sql.SQLException;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class StepTestBatchJob {
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    public Job startLimitJob() {
//        return jobBuilderFactory.get("startLimitJob")
//                .start(startLimitStep())
//                .build();
//    }
//
//    @Bean
//    public Job skipRetryJob() {
//        return jobBuilderFactory.get("skipJob")
//                //.start(skipStep())
//                .start(retryStep())
//                .build();
//    }
//
//    /**
//     * StartLimit : 재시작 횟수 제한
//     * 외부와의 통신, DB 작업 중 연결이 끊기면서 작업이 실패하는 경우, 배치를 재시작 해야함
//     * -> 기본 재시작 가능 횟수 : 1회
//     * -> startLimit()으로 횟수 지정 가능
//     * -> 지정 횟수를 초과하면 Exception 발생
//     */
//
//    @Bean
//    @JobScope
//    public Step startLimitStep() {
//        return stepBuilderFactory.get("startLimitStep")
//                .startLimit(3)  // 재시작 3번 가능
//                .tasklet((contribution, chunkContext) -> {
//                    log.info(">>>> This is SimpleStep1");
//                    int error = 5/0;    // 에러 발생 코드
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//
//    /* Skip: 데이터를 처리하는 동안 설정된 Exception이 발생한 경우, 해당 데이터 처리를 건너뛰는 기능 */
//    @Bean
//    public Step skipStep() {
//        return stepBuilderFactory.get("skipStep")
//                .<String, String>chunk(5)
//                .reader(reader())
//                .processor(processor())
//                .writer(writer())
//                .faultTolerant()
//                .skipLimit(4)
//                .skip(ArithmeticException.class)
//                .skip(SQLException.class)
//                .noSkip(NullPointerException.class)
//                .build();
//    }
//
//    @Bean
//    public Step retryStep() {
//        return stepBuilderFactory.get("retryStep")
//                .<String, String>chunk(5)
//                .reader(reader())
//                .processor(processor())
//                .writer(writer())
//                .faultTolerant()
//                .retry(SQLException.class)
//                .retryLimit(2)
//                .build();
//    }
//
//    @Bean
//    public ItemReader<String> reader() {
//        //데이터를 불러오는 로직을 작성
//        return new ItemReader<String>() {
//            int i = 0;
//
//            @SneakyThrows
//            @Override
//            public String read() throws ArithmeticException {
//                i++;
//
//                //skipLimit(2)이므로 아래 예외들은 모두 skip됨
////                if (i == 2) {
////                    log.error("ItemReader ArithmeticException 발생");
////                    throw new ArithmeticException("에러 발생");
////                }
////                if (i == 4) {
////                    log.error("ItemReader SQLException 발생");
////                    throw new SQLException("에러 발생");
////                }
//                log.info("itemReader >>> " + i);
//                return i > 20 ? null : String.valueOf(i);
//            }
//        };
//    }
//
//    @Bean
//    public ItemProcessor<String, String> processor() {
//        // 데이터를 처리하는 로직 작성
//        return item -> {
//            log.info("itemProcessor >>> " + item);
//
//            // [SKIP]에러 발생 시, chunk의 첫 단계(1부터)로 돌아가 itemReader로부터 다시 데이터를 전달받음
//            // => 이미 에러가 발생한 item은 skip하고 넘어감
//            // [RETRY] 예외 발생 시 재시도 됨
//            if(item.equals("3")){
//                log.error("ItemProcessor 에러 발생");
//                throw new SQLException();
//            }
//            return item;
//        };
//    }
//
//    @Bean
//    public ItemWriter<String> writer() {
//        // DB 저장과 같은 Transactional한 로직 작성
//        return items -> {
//            for (String item : items) {
//                if(item.equals("3")) {
//                    // [skip] 예외 발생 후 item을 리스트가 아니라 하나씩 받아옴 (처음에는 리스트로 받음)
//                    // [retry] 예외 발생 후 item을 리스트로 처리
//                    log.error("ItemWriter 에러 발생");
//                    throw new SQLException();
//                }
//            }
//            log.info("items >>> " + items);
//        };
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

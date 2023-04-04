//package com.spring.springbatch.job;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.ExitStatus;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * multiStepBatchJob의 경우 Next를 통해서 순차적으로 step을 제어하지만,
// * 앞의 step에서 오류가 발생하는 경우 뒤에있는 Step들은 실행 불가
// * => Flow를 통해 분기처리하여 Step을 실행
// */
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class FlowStepBatchJob {
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    public Job FlowStepJob(){
//        // on : Step의 ExitStatus를 캐치할 상태 입력 (BatchStatus가 아님)
//        // to : 다음으로 이동할 Step 작성
//        // from : 일종의 이벤트 리스너 역할
//        // end : builder 종료
//        return jobBuilderFactory.get("flowStepJob")
//                .start(startFlowStep())
//                    .on("FAILED")   // startFlowStep() -> failOverStep() 실행
//                    .to(failOverStep())
//                    .on("*")
//                    .end()  //step 종료
//                .from(startFlowStep())  // startFlowStep() -> successOverStep() -> finishOverStep() 실행
//                    .on("COMPLETED")
//                    .to(successOverStep())
//                    .next(finishOverStep())
//                    .on("*")
//                    .end()
//                .from(startFlowStep())  // startFlowStep() -> allOverStep() 실행
//                    .on("*")
//                    .to(allOverStep())
//                    .on("*")
//                    .end()
//                .end()  // job 종료
//                .build();
//
//    }
//
//    @Bean
//    public Step startFlowStep() {
//        return stepBuilderFactory.get("startStep")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("FlowStepJob Start Step");
//                    contribution.setExitStatus(ExitStatus.FAILED);  //Fail 테스트
//                    //contribution.setExitStatus(ExitStatus.COMPLETED);
//                    //contribution.setExitStatus(ExitStatus.UNKNOWN);
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//
//    @Bean
//    public Step failOverStep() {
//        return stepBuilderFactory.get("failOverStep")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("FlowStepJob FailOverStep -> startStep이 FAILED일 경우 실행");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//
//    @Bean
//    public Step successOverStep() {
//        return stepBuilderFactory.get("successOverStep")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("FlowStepJob SuccessOverStep");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//
//    @Bean
//    public Step finishOverStep() {
//        return stepBuilderFactory.get("finishOverStep")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("FlowStepJob FinishOverStep -> successOverStep 이후 실행");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//
//    @Bean
//    public Step allOverStep() {
//        return stepBuilderFactory.get("allOverStep")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("FlowStepJob allOverStep -> startStep 상태 관계 없이 실행됨");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//}

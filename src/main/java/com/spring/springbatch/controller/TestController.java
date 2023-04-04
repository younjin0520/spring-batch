package com.spring.springbatch.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@Controller
//@Slf4j
//@RequiredArgsConstructor
//public class TestController {
//    private final Job StepTestJob;
//    private final JobLauncher jobLauncher;
//
//    @SneakyThrows
//    @GetMapping("/test/{number}")
//    public void test(@PathVariable Long number) {
//        log.info(number + "번째 실행");
//        jobLauncher.run(StepTestJob, new JobParameters());
//    }
//}
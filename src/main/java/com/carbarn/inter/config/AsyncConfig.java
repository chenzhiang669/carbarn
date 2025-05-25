package com.carbarn.inter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync // 启用异步任务
public class AsyncConfig {
    @Bean(name = "customAsyncExecutor")
    public Executor customAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 核心线程数
        executor.setMaxPoolSize(3); // 最大线程数
        executor.setQueueCapacity(100); // 队列容量
        executor.setThreadNamePrefix("custom-async-"); // 线程名称前缀
        executor.setKeepAliveSeconds(60); // 非核心线程空闲存活时间
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy()); // 拒绝策略
        executor.initialize(); // 初始化线程池
        return executor;
    }
}

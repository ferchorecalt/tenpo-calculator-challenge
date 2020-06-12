package com.tenpo.challenge.configuration;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configure threadpool used in {@link com.tenpo.challenge.service.TransactionRecorderService}
 *
 * @author Fermin Recalt
 */

@Configuration
@EnableAsync
public class ThreadConfig {
	
	private static final int CORE_POOL_SIZE = 10;
	private static final int MAX_POOL_SIZE = 10;
	public static final String TRANSACTION_RECORDER_THREAD_POOL_TASK_EXECUTOR = "transactionRecorderThreadPoolTaskExecutor";
	
	@Bean(name = TRANSACTION_RECORDER_THREAD_POOL_TASK_EXECUTOR)
	public Executor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(CORE_POOL_SIZE);
		executor.setMaxPoolSize(MAX_POOL_SIZE);
		executor.setThreadNamePrefix(TRANSACTION_RECORDER_THREAD_POOL_TASK_EXECUTOR);
		executor.initialize();
		return executor;
	}
}

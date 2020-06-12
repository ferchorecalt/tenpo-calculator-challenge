package com.tenpo.challenge.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import com.tenpo.challenge.annotation.TransactionRecorder;
import com.tenpo.challenge.model.TransactionResult;
import com.tenpo.challenge.service.TransactionRecorderService;

/**
 * Aspect executed after controller execution in order to persist the user action
 *
 * @author Fermin Recalt
 */

@Aspect
@Configuration
public class TransactionRecorderAspect {
	
	private TransactionRecorderService transactionRecorderService;
	
	public TransactionRecorderAspect(TransactionRecorderService transactionRecorderService) {
		this.transactionRecorderService = transactionRecorderService;
	}
	
	@AfterReturning(pointcut = "@annotation(transactionRecorder)")
	public void afterMethodController(TransactionRecorder transactionRecorder) {
		this.recordTransaction(transactionRecorder.transactionName(), TransactionResult.SUCCESS);
	}
	
	@AfterThrowing(pointcut = "@annotation(transactionRecorder)")
	public void afterThrowingMethodController(TransactionRecorder transactionRecorder) {
		this.recordTransaction(transactionRecorder.transactionName(), TransactionResult.FAILURE);
	}
	
	private void recordTransaction(String transactionName, TransactionResult result) {
		this.transactionRecorderService.recordTransaction(transactionName, result);
	}
}

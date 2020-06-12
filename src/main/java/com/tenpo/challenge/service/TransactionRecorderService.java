package com.tenpo.challenge.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.tenpo.challenge.auth.helper.AuthenticationContextHelper;
import com.tenpo.challenge.auth.model.User;
import com.tenpo.challenge.auth.service.UserService;
import com.tenpo.challenge.configuration.ThreadConfig;
import com.tenpo.challenge.model.TransactionHistory;
import com.tenpo.challenge.model.TransactionResult;
import com.tenpo.challenge.repository.TransactionHistoryRepository;

/**
 * Create the user transaction object, and perform async saving.
 *
 * @author Fermin Recalt
 */

@Service
public class TransactionRecorderService {
	
	private TransactionHistoryRepository transactionHistoryRepository;
	private UserService userService;
	
	public TransactionRecorderService(TransactionHistoryRepository transactionHistoryRepository, UserService userService) {
		this.transactionHistoryRepository = transactionHistoryRepository;
		this.userService = userService;
	}
	
	@Async(ThreadConfig.TRANSACTION_RECORDER_THREAD_POOL_TASK_EXECUTOR)
	public void recordTransaction(String transactionName, TransactionResult transactionResult) {
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.setOperationName(transactionName);
		transactionHistory.setResult(transactionResult);
		
		if (AuthenticationContextHelper.isAuthenticated()) {
			User user = this.userService.findByUsername(AuthenticationContextHelper.getAuthenticatedUsername());
			transactionHistory.setUser(user);
		}
		this.transactionHistoryRepository.save(transactionHistory);
	}
}

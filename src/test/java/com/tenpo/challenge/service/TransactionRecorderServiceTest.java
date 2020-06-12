package com.tenpo.challenge.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import com.tenpo.challenge.auth.service.UserService;
import com.tenpo.challenge.model.TransactionResult;
import com.tenpo.challenge.repository.TransactionHistoryRepository;

@RunWith(SpringRunner.class)
public class TransactionRecorderServiceTest {
	
	@Mock
	private TransactionHistoryRepository transactionHistoryRepository;
	@Mock
	private UserService userService;
	
	private TransactionRecorderService transactionRecorderService;
	
	@Before
	public void setup() {
		this.transactionRecorderService = new TransactionRecorderService(transactionHistoryRepository, userService);
	}
	
	@Test
	public void userService_shouldntBeCalled() {
		this.transactionRecorderService.recordTransaction("LOGIN", TransactionResult.SUCCESS);
		
		verify(this.userService, times(0)).findByUsername(any());
		verify(this.transactionHistoryRepository, times(1)).save(any());
	}
	
	@WithMockUser(value = "spring")
	@Test
	public void userService_shouldBeCalled() {
		this.transactionRecorderService.recordTransaction("LOGIN", TransactionResult.SUCCESS);
		
		verify(this.transactionHistoryRepository, times(1)).save(any());
		verify(userService, times(1)).findByUsername(any());
	}
}

package com.tenpo.challenge.aspect;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import com.tenpo.challenge.annotation.TransactionRecorder;
import com.tenpo.challenge.model.TransactionResult;
import com.tenpo.challenge.service.TransactionRecorderService;

@RunWith(SpringRunner.class)
public class TransactionRecorderAspectTest {
	
	@Mock
	private TransactionRecorderService transactionRecorderService;
	
	private TransactionRecorderAspect aspect;
	
	@Before
	public void setup() {
		this.aspect = new TransactionRecorderAspect(transactionRecorderService);
	}
	
	@Test
	public void testSuccessUserOperationSave_Succeed() {
		aspect.afterMethodController(new TransactionRecorder() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return TransactionRecorder.class;
			}
			
			@Override
			public String transactionName() {
				return "LOGIN";
			}
		});
		
		verify(transactionRecorderService, times(1)).recordTransaction("LOGIN", TransactionResult.SUCCESS);
	}
	
	@Test
	public void testFailureUserOperationSave_Failure() {
		aspect.afterThrowingMethodController(new TransactionRecorder() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return TransactionRecorder.class;
			}
			
			@Override
			public String transactionName() {
				return "LOGIN";
			}
		});
		
		verify(transactionRecorderService, times(1)).recordTransaction("LOGIN", TransactionResult.FAILURE);
	}
}

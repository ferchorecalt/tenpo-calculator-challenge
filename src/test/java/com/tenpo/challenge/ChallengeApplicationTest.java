package com.tenpo.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.challenge.dto.ResultDTO;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ChallengeApplicationTest {
	
	protected MockMvc mvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Before()
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	private <T> T mapFromJson(String json, Class<T> clazz) throws Exception {
		return objectMapper.readValue(json, clazz);
	}
	
	private void testGETMethodWithoutError(String uri, int expectedStatus, BigDecimal expectedResult) throws Exception {
		MockHttpServletResponse httpResponse = makeCallToService(uri);
		
		assertEquals(expectedStatus, httpResponse.getStatus());
		
		ResultDTO result = mapFromJson(httpResponse.getContentAsString(), ResultDTO.class);
		assertTrue(result.getResult().equals(expectedResult));
	}
	
	private void testGETMethodWithError(String uri, int expectedStatus) throws Exception {
		MockHttpServletResponse httpResponse = makeCallToService(uri);
		
		assertEquals(expectedStatus, httpResponse.getStatus());
	}
	
	private MockHttpServletResponse makeCallToService(String uri) throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		return mvcResult.getResponse();
	}
	
	@Test
	public void getAdd() throws Exception {
		testGETMethodWithoutError("/add?first_operator=5&second_operator=4", 200, BigDecimal.valueOf(9));
	}
	
	@Test
	public void getBadRequest() throws Exception {
		testGETMethodWithError("/add?first_operator=2343&second_operator=asd0", 400);
	}
	
}

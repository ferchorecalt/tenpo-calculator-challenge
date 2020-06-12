package com.tenpo.challenge.security;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.challenge.auth.dto.UserDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityTest {
	
	@Autowired
	private WebApplicationContext context;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final String AUTH_LOGIN_URI = "/auth/login";
	private static final String AUTH_LOGOUT_URI = "/auth/logout";
	private static final String AUTH_SIGN_UP_URI = "/auth/signup";
	
	private MockMvc mockMvc;
	
	private UserDTO correctUser;
	private UserDTO wrongUser;
	
	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
		
		this.correctUser = getCorrectUser();
		this.wrongUser = getWrongUser();
	}
	
	@Test
	public void signup_shouldSucceed() throws Exception {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername("foo");
		userDTO.setPassword("bar");
		userDTO.setConfirmPassword("bar");
		this.mockMvc.perform(MockMvcRequestBuilders.post(AUTH_SIGN_UP_URI).content(mapToJson(userDTO)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void signup_shouldFail() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post(AUTH_SIGN_UP_URI).content(mapToJson(this.correctUser)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void login_shouldSucceed() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.post(AUTH_LOGIN_URI).content(mapToJson(this.correctUser)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void login_shouldFail() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.post(AUTH_LOGIN_URI).content(mapToJson(this.wrongUser)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void givenAuthRequest_shouldSucceed() throws Exception {
		
		String token = this.mockMvc.perform(MockMvcRequestBuilders.post(AUTH_LOGIN_URI).content(mapToJson(this.correctUser)).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/add?first_operator=4&second_operator=5").header("Authorization", token).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void givenAuthRequestWithoutUser_shouldFail() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/add?first_operator=4&second_operator=5").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}
	
	@Test
	public void givenLogoutUser_shouldFail() throws Exception {
		String token = this.mockMvc.perform(MockMvcRequestBuilders.post(AUTH_LOGIN_URI).content(mapToJson(this.correctUser)).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		
		this.mockMvc.perform(MockMvcRequestBuilders.get(AUTH_LOGOUT_URI).header("Authorization", token).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/add?first_operator=4&second_operator=5").header("Authorization", token).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}
	
	private String mapToJson(Object obj) throws Exception {
		return objectMapper.writeValueAsString(obj);
	}
	
	private static UserDTO getCorrectUser() {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername("name");
		userDTO.setPassword("password");
		
		return userDTO;
	}
	
	private static UserDTO getWrongUser() {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername("name");
		userDTO.setPassword("passworsadasd");
		
		return userDTO;
	}
}

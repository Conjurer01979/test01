package com.article.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AutoConfigureMockMvc
@SpringBootTest
public class ArticleCreateTest {

	@Autowired
	private MockMvc mockMvc;

	private final String TITLE = "title";
	
	@Test
	public void testArticle() throws Exception {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
		// add an article of date now
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(String.format("/articlecreate?title=%s&author=author1&content=content1&date=%s", TITLE, LocalDate.now().format(formatter)))
				.header(AuthenticationService.AUTH_TOKEN_HEADER_NAME, AuthenticationService.AUTH_USER);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertThat(result.getResponse().getContentAsString()).isEqualTo("Article added: " + TITLE);
		// ask for the list
		requestBuilder = MockMvcRequestBuilders.get("/articlelist")
				.header(AuthenticationService.AUTH_TOKEN_HEADER_NAME, AuthenticationService.AUTH_USER);
		result = mockMvc.perform(requestBuilder).andReturn();
		assertThat(result.getResponse().getContentAsString()).contains(TITLE);
		// ask for count, expecting 1
		requestBuilder = MockMvcRequestBuilders.get("/articlecount")
				.header(AuthenticationService.AUTH_TOKEN_HEADER_NAME, AuthenticationService.AUTH_ADMIN);
		result = mockMvc.perform(requestBuilder).andReturn();
		assertThat(result.getResponse().getContentAsString()).contains("1");
		// add an article of date now minus 8 days
		requestBuilder = MockMvcRequestBuilders.get(String.format("/articlecreate?title=title2&author=author1&content=content1&date=%s", LocalDate.now().minusDays(8).format(formatter)))
				.header(AuthenticationService.AUTH_TOKEN_HEADER_NAME, AuthenticationService.AUTH_USER);
		// ask for count, expecting 1
		requestBuilder = MockMvcRequestBuilders.get("/articlecount")
				.header(AuthenticationService.AUTH_TOKEN_HEADER_NAME, AuthenticationService.AUTH_ADMIN);
		result = mockMvc.perform(requestBuilder).andReturn();
		assertThat(result.getResponse().getContentAsString()).contains("1");
	}
}

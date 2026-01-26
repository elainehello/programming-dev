package com.elainehello.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoutesIntegrationTests extends PostgresContainerTestBase {

	@Autowired
	private WebApplicationContext context;

	@Test
	void customersReturnsSeedData() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		mockMvc.perform(get("/customers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].name", hasItems("Josh", "Tom")));
	}

	@Test
	void helloReturnsMessage() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		mockMvc.perform(get("/hello")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Hello World"));
	}

}

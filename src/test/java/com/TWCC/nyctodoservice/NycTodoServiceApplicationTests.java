package com.TWCC.nyctodoservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import com.TWCC.controller.EventController;

@SpringBootTest
class NycTodoServiceApplicationTests {
	
	@Autowired
	EventController eventController;

	@Test
	void contextLoads(ApplicationContext context) {
		Assertions.assertThat(context).isNotNull();
	}
	
	@Test
	void hasEventControllerConfigured() {
		Assertions.assertThat(eventController).isNotNull();
	}

}

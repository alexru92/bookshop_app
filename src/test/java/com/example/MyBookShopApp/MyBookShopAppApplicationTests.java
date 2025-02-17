package com.example.MyBookShopApp;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MyBookShopAppApplicationTests {

	@Value("${auth.secret}")
	String authSecret;

	private final MyBookShopAppApplication appApplication;

	@Autowired
	MyBookShopAppApplicationTests(MyBookShopAppApplication appApplication) {
		this.appApplication = appApplication;
	}


	@Test
	void contextLoads() {
		assertNotNull(appApplication);
	}

	@Test
	void verifyAuthSecret(){
		assertThat(authSecret, Matchers.containsString("Myown"));
	}

}

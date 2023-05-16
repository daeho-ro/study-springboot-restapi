package com.boot3.myrestapi.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Order(1)
@Component
@RequiredArgsConstructor
public class DatabaseRunner implements ApplicationRunner {

	private final DataSource dataSource;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("DataSource 구현객체는 ?? " + dataSource.getClass().getName());
		try (Connection connection = dataSource.getConnection()) {
			System.out.println(connection.getMetaData().getURL());
			System.out.println(connection.getMetaData().getUserName());
		}
	}

}

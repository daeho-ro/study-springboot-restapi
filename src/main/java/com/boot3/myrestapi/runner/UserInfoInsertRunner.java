package com.boot3.myrestapi.runner;

import com.boot3.myrestapi.userinfo.UserInfo;
import com.boot3.myrestapi.userinfo.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Order(3)
@Component
@RequiredArgsConstructor
public class UserInfoInsertRunner implements ApplicationRunner {

	private final UserInfoRepository userInfoRepository;
	private final PasswordEncoder encoder;

	@Override
	public void run(ApplicationArguments args) {
		generateUserInfo();
	}

	private void generateUserInfo() {
		UserInfo userInfo = UserInfo.builder()
				.name("admin")
				.email("admin@example.com")
				.password(encoder.encode("pwd1"))
				.roles("ROLE_ADMIN,ROLE_USER")
				.build();
		userInfoRepository.save(userInfo);
	}

}

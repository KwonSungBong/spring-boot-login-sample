package com.example.demo;

import com.example.demo.auth.security.service.UserRepository;
import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserRoleDto;
import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(scanBasePackages = "com.example.demo")
public class DemoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Resource(name = "authUserRepository")
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Bean
	@Transactional
	public InitializingBean localInitializingBean(){
		return () -> {
			if(ObjectUtils.isEmpty(userRepository.findByEmail("test@test.co.kr"))){
				UserDto.Create userCreate = new UserDto.Create();
				userCreate.setEmail("test@test.co.kr");
				userCreate.setUserName("테스트");
				userCreate.setPassword(passwordEncoder.encode("test"));

				UserRoleDto.Create userRoleCreate = new UserRoleDto.Create();
				userRoleCreate.setRole("ROLE_ADMIN");

				List<UserRoleDto.Create> userRoleList = new ArrayList<>();
				userRoleList.add(userRoleCreate);

				userCreate.setUserRoleList(userRoleList);

				userRoleCreate = new UserRoleDto.Create();
				userRoleCreate.setRole("ROLE_USER");

				userRoleList.add(userRoleCreate);

				userCreate.setUserRoleList(userRoleList);

				User user = modelMapper.map(userCreate, User.class);
				user.getUserRoleList().stream().forEach(userRole -> userRole.setUser(user));
				user.setCreatedDate(new DateTime());
				user.setLastModifiedDate(new DateTime());
				userRepository.save(user);
			}
		};
	}
}
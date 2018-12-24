package net.cnki;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

// 2.0的start中默认也有一个spring-boot-autoconfigure-2.0..RELEASE.jar，
// 如果还引用了activiti的activiti-spring-boot-starter-rest-api.jar包，
// 需要将两个包中的 SecurityAutoConfiguration.class 都排除，
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class
	//	org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
		})
@MapperScan("net.cnki.mapper")
@ComponentScan({"net.cnki.controller","net.cnki.service","net.cnki.common","net.cnki.config","net.cnki.logic","org.activiti.rest"})
//@EnableCaching
@EnableRedisHttpSession
//@EnableSwagger2
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner init(final IdentityService identityService) {
//		return new CommandLineRunner() {
//
//			@Override
//			public void run(String... args) throws Exception {
//				Group admins = identityService.newGroup("test");
//				admins.setType("security-role");
//				identityService.saveGroup(admins);
//
//				User admin = identityService.newUser("test1");
//				admin.setPassword("test");
//				identityService.saveUser(admin);
//
//				identityService.createMembership("test1", "test");
//			}
//
//		};
//	}

}


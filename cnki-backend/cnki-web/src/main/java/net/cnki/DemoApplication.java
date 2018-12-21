package net.cnki;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// 2.0的start中默认也有一个spring-boot-autoconfigure-2.0..RELEASE.jar，
// 如果还引用了activiti的activiti-spring-boot-starter-rest-api.jar包，
// 需要将两个包中的 SecurityAutoConfiguration.class 都排除，
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan("net.cnki.mapper")
//@EnableCaching
@EnableRedisHttpSession
@EnableSwagger2
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

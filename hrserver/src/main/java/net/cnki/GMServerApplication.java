package net.cnki;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan("net.cnki.mapper")
@EnableCaching
@EnableRedisHttpSession
public class GMServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GMServerApplication.class, args);
	}
}

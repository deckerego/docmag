package net.deckerego.docmag

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class Application {

	static void main(String[] args) {
		SpringApplication.run Application, args
	}
}

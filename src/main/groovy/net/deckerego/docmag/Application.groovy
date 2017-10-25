package net.deckerego.docmag

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@SpringBootApplication
@EnableElasticsearchRepositories("net.deckerego.docmag.repository")
class Application {

	static void main(String[] args) {
		SpringApplication.run Application, args
	}
}

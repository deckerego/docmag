package net.deckerego.docmag

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@SpringBootApplication
@EnableElasticsearchRepositories("net.deckerego.docmag.repository")
@ConfigurationProperties
class DocmagApplication {
    String elasticHost
    int elasticPort
    String elasticClusterName

	static void main(String[] args) {
		SpringApplication.run DocmagApplication, args
	}

	@Bean
    //TODO Move to application.properties (override with envvars)
	ElasticsearchOperations elasticsearchTemplate() {
		Settings settings = Settings.builder()
                .put("cluster.name", elasticClusterName)
                .build()
        Client client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticHost), elasticPort))
		new ElasticsearchTemplate(client)
	}
}

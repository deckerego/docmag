package net.deckerego.docmag.configuration

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.springframework.boot.actuate.elasticsearch.ElasticsearchHealthIndicator
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@EnableElasticsearchRepositories("net.deckerego.docmag.repository")
class ElasticConfig {
    String host
    int port
    String cluster

    @Bean
    ElasticsearchOperations elasticsearchTemplate() {
        Settings settings = Settings.builder()
                .put("cluster.name", cluster)
                .build()
        Client client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port))
        new ElasticsearchTemplate(client)
    }

    @Bean
    ElasticsearchHealthIndicator elasticsearchHealthIndicator(ElasticsearchOperations elasticsearchTemplate) {
        new ElasticsearchHealthIndicator(elasticsearchTemplate.getClient(), 5 * 1000, "docidx")
    }
}

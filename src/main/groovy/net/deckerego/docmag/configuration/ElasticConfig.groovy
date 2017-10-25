package net.deckerego.docmag.configuration

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "elasticsearch")
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
}
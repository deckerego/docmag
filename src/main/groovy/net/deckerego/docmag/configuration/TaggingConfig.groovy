package net.deckerego.docmag.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "tagging")
class TaggingConfig {
    float threshold
}

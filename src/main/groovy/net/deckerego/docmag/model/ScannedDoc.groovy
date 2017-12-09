package net.deckerego.docmag.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "docidx", type = "fileentry")
class ScannedDoc {
    @Id
    String id
    Metadata metadata
    String parentPath
    String fileName
    Date lastModified
    String body

    static class Metadata {
        @JsonProperty("Content-Type")
        String contentType
    }
}

package net.deckerego.docmag.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "scanned", type = "doc")
class ScannedDoc {
    @Id
    String id
    String content
    File file
    Path path

    static class File {
        @JsonProperty("last_modified")
        Date lastModified
        @JsonProperty("content_type")
        String contentType
    }

    static class Path {
        String virtual
    }
}

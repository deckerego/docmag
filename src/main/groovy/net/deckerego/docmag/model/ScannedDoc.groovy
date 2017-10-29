package net.deckerego.docmag.model

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
        Date last_modified
    }

    static class Path {
        String virtual
    }
}

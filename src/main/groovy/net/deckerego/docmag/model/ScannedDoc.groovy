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
}

class File {
    String content_type
    String url
    Date last_modified
    Date indexing_date

}

class Path {
    String virtual
}
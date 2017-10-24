package net.deckerego.docmag.model

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "scanned", type = "doc")
class ScannedDoc {
    @Id
    String id
    File file
}

class File {
    String url
}
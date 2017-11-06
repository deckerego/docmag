package net.deckerego.docmag.repository

import net.deckerego.docmag.model.ScannedDoc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.data.elasticsearch.core.query.SearchQuery
import org.springframework.stereotype.Repository

import static org.elasticsearch.index.query.QueryBuilders.termQuery
import static org.elasticsearch.index.query.QueryBuilders.idsQuery

@Repository
class ScannedRepository {
    @Autowired
    ElasticsearchOperations elasticsearchTemplate

    /**
     * "query": {"match": {"content": "electric"}}
     **/
    Page<ScannedDoc> findByContent(String name, Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(termQuery("content", name))
                .withPageable(pageable)
                .build()

        elasticsearchTemplate.queryForPage searchQuery, ScannedDoc.class
    }

    ScannedDoc findById(String id) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(idsQuery(id))
                .build()

        elasticsearchTemplate.query searchQuery, ScannedDoc.class
    }
}
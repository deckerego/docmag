package net.deckerego.docmag.repository

import net.deckerego.docmag.model.ScannedDoc
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.query.GetQuery
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.data.elasticsearch.core.query.SearchQuery
import org.springframework.stereotype.Repository

import static org.elasticsearch.index.query.QueryBuilders.matchQuery

@Repository
class ScannedRepository {
    @Autowired
    ElasticsearchOperations elasticsearchTemplate

    /**
     * "query": {"match": {"content": "electric"}}
     **/
    Page<ScannedDoc> findByContent(String name, Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("scanned")
                .withQuery(matchQuery("content", name))
                .withPageable(pageable)
                .build()

        elasticsearchTemplate.queryForPage searchQuery, ScannedDoc.class
    }

    ScannedDoc findById(String id) {
        GetQuery searchQuery = new GetQuery(id: id)

        elasticsearchTemplate.queryForObject searchQuery, ScannedDoc.class
    }

    @Cacheable("scannedIndexCount")
    long documentCount() {
        IndicesStatsResponse response = elasticsearchTemplate.client
                .admin()
                .indices()
                .prepareStats("scanned")
                .setDocs(true)
                .get()

        response.total.docs.count
    }
}
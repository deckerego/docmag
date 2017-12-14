package net.deckerego.docmag.repository

import net.deckerego.docmag.model.ScannedDoc
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse
import org.elasticsearch.index.query.RangeQueryBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.query.GetQuery
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.data.elasticsearch.core.query.SearchQuery
import org.springframework.stereotype.Repository

import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery

@Repository
class ScannedRepository {
    @Autowired
    ElasticsearchOperations elasticsearchTemplate

    Page<ScannedDoc> findByContent(String name, Date startTime, Date endTime, Pageable pageable) {
        RangeQueryBuilder rangeBuilder = new RangeQueryBuilder("lastModified")
                .from(startTime.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
                .to(endTime.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(simpleQueryStringQuery(name).field("body"))
                .withFilter(rangeBuilder)
                .withPageable(pageable)
                .build()

        elasticsearchTemplate.queryForPage searchQuery, ScannedDoc.class
    }

    ScannedDoc findById(String id) {
        GetQuery searchQuery = new GetQuery(id: id)

        elasticsearchTemplate.queryForObject searchQuery, ScannedDoc.class
    }

    long documentCount() {
        IndicesStatsResponse response = elasticsearchTemplate.client
                .admin()
                .indices()
                .prepareStats("docidx")
                .setDocs(true)
                .get()

        response.total.docs.count
    }
}
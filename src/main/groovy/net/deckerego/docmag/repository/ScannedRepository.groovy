package net.deckerego.docmag.repository

import net.deckerego.docmag.configuration.DocConfig
import net.deckerego.docmag.configuration.TaggingConfig
import net.deckerego.docmag.model.ScannedDoc
import org.apache.lucene.search.join.ScoreMode
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.NestedQueryBuilder
import org.elasticsearch.index.query.RangeQueryBuilder
import org.elasticsearch.join.query.JoinQueryBuilders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.query.GetQuery
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.data.elasticsearch.core.query.SearchQuery
import org.springframework.stereotype.Repository

import static org.elasticsearch.index.query.QueryBuilders.boolQuery
import static org.elasticsearch.index.query.QueryBuilders.matchQuery
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery

@Repository
class ScannedRepository {
    @Autowired
    TaggingConfig taggingConfig

    @Autowired
    ElasticsearchOperations elasticsearchTemplate

    Page<ScannedDoc> findByContent(String name, Collection<String> tags, Date startTime, Date endTime, Pageable pageable) {
        RangeQueryBuilder rangeBuilder = rangeQuery("lastModified")
                .from(startTime.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
                .to(endTime.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(simpleQueryStringQuery(name).field("body"))
                .withFilter(rangeBuilder)

        if(tags) {
            BoolQueryBuilder tagQuery = boolQuery()
            for (String tag : tags) {
                tagQuery = tagQuery.must(matchQuery("tags.name", tag))
                tagQuery = tagQuery.must(rangeQuery("tags.score").gte(taggingConfig.threshold))
            }

            searchQueryBuilder = searchQueryBuilder
                    .withFilter(nestedQuery("tags", tagQuery, ScoreMode.Avg))
        }

        SearchQuery searchQuery = searchQueryBuilder
                .withPageable(pageable)
                .build()

        elasticsearchTemplate.queryForPage searchQuery, ScannedDoc.class
    }

    ScannedDoc findById(String id) {
        GetQuery searchQuery = new GetQuery(id: id)

        elasticsearchTemplate.queryForObject searchQuery, ScannedDoc.class
    }

    Page<ScannedDoc> findByTag(String tag, Pageable pageable) {
        BoolQueryBuilder tagQuery = boolQuery()
        tagQuery = tagQuery.must(matchQuery("tags.name", tag))
        tagQuery = tagQuery.must(rangeQuery("tags.score").gte(taggingConfig.threshold))

        NestedQueryBuilder nestedQueryBuilder = nestedQuery("tags", tagQuery, ScoreMode.Avg)

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(nestedQueryBuilder)
                .withPageable(pageable)
                .build()

        elasticsearchTemplate.queryForPage searchQuery, ScannedDoc.class
    }

    long tagCount(String tag) {
        Page<ScannedDoc> page = findByTag tag, PageRequest.of(1, 1)
        return page.totalElements
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

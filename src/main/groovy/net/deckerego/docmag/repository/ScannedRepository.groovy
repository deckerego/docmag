package net.deckerego.docmag.repository

import net.deckerego.docmag.model.ScannedDoc
import org.elasticsearch.search.sort.FieldSortBuilder
import org.elasticsearch.search.sort.SortOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.data.elasticsearch.core.query.SearchQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

import static org.elasticsearch.index.query.QueryBuilders.*

@Repository
class ScannedRepository {
    @Autowired
    ElasticsearchOperations elasticsearchTemplate

/**
 * "sort": [{"file.last_modified": "desc"}, "_score"],
 * "query": {"match": {"content": "electric"}}
 **/
    Page<ScannedDoc> findByName(String name, Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withSort(new FieldSortBuilder("file.last_modified").order(SortOrder.DESC))
            .withQuery(termQuery("content", name))
            .withPageable(pageable)
            .build()

        elasticsearchTemplate.queryForPage(searchQuery, ScannedDoc.class)
    }
}


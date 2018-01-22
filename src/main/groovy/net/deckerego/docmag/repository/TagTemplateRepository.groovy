package net.deckerego.docmag.repository

import net.deckerego.docmag.model.TagTemplate
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface TagTemplateRepository extends ElasticsearchRepository<TagTemplate, String> {
}
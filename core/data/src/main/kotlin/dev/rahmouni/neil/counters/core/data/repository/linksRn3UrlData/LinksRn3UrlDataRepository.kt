package dev.rahmouni.neil.counters.core.data.repository.linksRn3UrlData

import dev.rahmouni.neil.counters.core.data.model.LinkRn3UrlRawData
import kotlinx.coroutines.flow.Flow

interface LinksRn3UrlDataRepository {

    val links: Flow<List<LinkRn3UrlRawData>>
    fun setLink(linkRn3UrlRawData: LinkRn3UrlRawData)
    fun deleteLink(path: String)
}
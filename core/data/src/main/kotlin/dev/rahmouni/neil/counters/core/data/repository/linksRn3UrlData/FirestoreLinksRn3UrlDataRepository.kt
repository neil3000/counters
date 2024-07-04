package dev.rahmouni.neil.counters.core.data.repository.linksRn3UrlData

import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.rahmouni.neil.counters.core.data.model.LinkRn3UrlRawData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirestoreLinksRn3UrlDataRepository @Inject constructor() : LinksRn3UrlDataRepository {

    override val links: Flow<List<LinkRn3UrlRawData>> = Firebase.firestore
        .collection("links")
        .dataObjects<LinkRn3UrlRawData>()

    override fun setLink(linkRn3UrlRawData: LinkRn3UrlRawData) {
        Firebase.firestore
            .collection("links")
            .document(linkRn3UrlRawData.path)
            .set(linkRn3UrlRawData)
    }

    override fun deleteLink(path: String) {
        Firebase.firestore
            .collection("links")
            .document(path)
            .delete()
    }
}
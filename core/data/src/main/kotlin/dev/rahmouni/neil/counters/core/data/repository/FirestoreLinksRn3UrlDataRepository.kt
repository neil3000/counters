/*
 * Copyright 2024 Rahmouni Neïl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.rahmouni.neil.counters.core.data.repository

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

package com.example.project.ui.timeline

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project.model.Post
import com.example.project.repositoty.timeline.TimelineRepository
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject

class TimelineViewModel: ViewModel() {
    private val repository = TimelineRepository()

    private val _posts = MutableLiveData<List<Post>>() // repository와만 통신하며 데이터를 저장하는 변수
    var posts: LiveData<List<Post>> = _posts // ui에서 사용되는 변수 (observe, binding 등)

    init {
        loadPosts()
    }

    private fun loadPosts() {
        val queryPostKeys = repository.getPostKeys()
        updatePosts(queryPostKeys)
    }

    // 데이터가 변화할 때마다 posts 새롭게 업데이트
    private fun updatePosts(query: Query) {
        query.addSnapshotListener { snapshot, e ->
            if(snapshot == null)
                return@addSnapshotListener

            // 마지막 데이터가 있는지 확인
            val lastVisible = snapshot.documents[snapshot.size() - 1]
            // 주어진 데이터(snapshot)으로 posts를 만들어 _posts를 업데이트 해준다.
            _posts.value = makesPosts(snapshot)
        }
    }

    private fun makesPosts(snapshot: QuerySnapshot): List<Post> {
        val documents = snapshot.documents
        val posts = mutableListOf<Post>()
        for(document in documents) {
            val postValue = document.toObject<Post>()
            posts.add(postValue!!)
        }

        return posts
    }
}
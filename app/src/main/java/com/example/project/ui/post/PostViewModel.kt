package com.example.project.ui.post


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.model.Comment
import com.example.project.model.Post
import com.example.project.model.User
import com.example.project.repositoty.post.PostRepository
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PostViewModel(postKey: String, val uid: String?): ViewModel() {
    private val repository = PostRepository(postKey)

    private val _post = MutableLiveData<Post>() // repository와만 통신하며 데이터를 저장하는 변수
    var post: LiveData<Post> = _post // ui에서 사용되는 변수 (observe, binding 등)

    private val _user = MutableLiveData<User>()
    var user: LiveData<User> = _user

    private val _comments = MutableLiveData<List<Comment>>()
    var comments: LiveData<List<Comment>> = _comments

    private val _writer = MutableLiveData<User>()
    var writer: LiveData<User> = _writer

    init {
        loadPost()
        loadUser()
        loadComments()
        //loadComment2()
    }

//    private fun loadPost() {
//        _post.value = repository.getPost()
//        Log.d("post", "viewModel ${post.value?.title}")
//    }

    private fun loadPost() {
        val documentData = repository.getPost()
        documentData.addOnSuccessListener { snapshot ->
            _post.value = snapshot.toObject<Post>()
            loadWriter(_post.value?.writerUid!!)
        }
    }

    private fun loadWriter(writerUid: String) {
        val document = repository.getWriter(writerUid)
        document.addOnSuccessListener { snapshot ->
            _writer.value = snapshot.toObject<User>()
        }
    }

    private fun loadUser() {
        val document = repository.getUser(uid)
        document.addOnSuccessListener { snapshot ->
            _user.value = snapshot.toObject<User>()
        }
    }

    private fun loadComments() {
        val queryComments = repository.getComments()
        updateComments(queryComments)
    }

    private fun updateComments(query: Query) {
        query.addSnapshotListener { snapshot, _ ->
            if(snapshot == null) return@addSnapshotListener

            _comments.value = makesComments(snapshot)
        }
    }

    private fun makesComments(snapshot: QuerySnapshot): List<Comment> {
        val documents = snapshot.documents
        val comments = mutableListOf<Comment>()
        for(document in documents) {
            val value = document.toObject<Comment>()
            comments.add(value!!)
        }

        return comments
    }
}
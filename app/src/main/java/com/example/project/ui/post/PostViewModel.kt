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

    init {
        loadPost2()
        loadUser()
        loadComments()
    }

//    private fun loadPost() {
//        _post.value = repository.getPost()
//        Log.d("post", "viewModel ${post.value?.title}")
//    }

    private fun loadPost2() {
        val documentData = repository.getPost2()
        documentData.addOnSuccessListener { snapshot ->
            _post.value = snapshot.toObject<Post>()
        }
    }

    private fun loadComments() {
        val querySnapshot = repository.getComments()
        querySnapshot.addOnSuccessListener {
            val comments = mutableListOf<Comment>()
            for(documentData in it.documents) {
                documentData.toObject<Comment>()?.let { comment ->
                    comments.add(comment) }
            }

            _comments.value = comments
            Log.d("post", comments.toString())
        }
    }

    private fun loadUser() {
        val document = repository.getUser(uid)
        document.addOnSuccessListener { snapshot ->
            val test = snapshot.toObject<User>()
            _user.value = test
            Log.d("post", test.toString())
        }
    }
}
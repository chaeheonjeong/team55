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
            Log.d("writer", _post.value?.writerUid!!)
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

    fun loadComments() {
        val querySnapshot = repository.getComments()
        querySnapshot.addOnSuccessListener {
            val comments = mutableListOf<Comment>()
            for(documentData in it.documents) {
                documentData.toObject<Comment>()?.let { comment ->
                    comments.add(comment) }
            }

            _comments.value = comments
            Log.d("comments", comments.toString())
        }
    }

    // 데이터가 변경할 때마다 데이터를 로드하는 로직
    // 근데 처음엔 좋은데, 나중엔 변화 되는것만 list만들어서 저장하게 되는거 아냐?
    // 이게 안되면, 댓글 작성할 떄마다 viewModel 접근해서 변경해보는 식으로 해보자.
    /*
    private fun loadComment2() {
        val query = repository.getComments2()
        query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("error", "Listen failed.", error)
                return@addSnapshotListener
            }

            val documents = snapshot?.documents
            if (documents != null) {
                val tempValue = mutableListOf<Comment>()
                for (document in documents) {
                    val comment = document.toObject<Comment>()
                    tempValue.add(comment!!)
                }
                _comments.value = tempValue
            }
        }
    }
    */
}
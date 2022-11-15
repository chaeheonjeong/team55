package com.example.project.repositoty.post

import android.util.Log
import com.example.project.model.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// todo 2. comments 에서 user정보 제대로 가져오는 지 확인

class PostRepository(private val postKey: String) {
    private val postCollection = Firebase.firestore.collection("posts")
    private var post = Post()

    // repository에서 async 이용하면 어떻게든 구현은 가능할 것 같은데
    // 그렇게 하면 좋은 비동기 처리 방식 냅두고, 기다려야 하는 거니까, 이게 맞는건가 싶음
//    fun getPost(): Post {
//        val documentRef = collectionRef.document(postKey)
//        documentRef.get().addOnSuccessListener { snapshot ->
//            post = snapshot.toObject<Post>()!!
//        }
//
//        return post
//    }

    fun getPost2(): Task<DocumentSnapshot> {
        val documentRef = postCollection.document(postKey)
        return documentRef.get()
    }

    fun getComments(): Task<QuerySnapshot> {
        // todo 이거 경로만 짧게 쓰는 법 있지 않을까?
        val commentsCollection = postCollection.document(postKey).collection("comments")
        // Firebase.firestore.collection("posts/$postKey/comments")
        return commentsCollection.get()
    }

    fun getUser(uid: String?): Task<DocumentSnapshot> {
        val userDocument = Firebase.firestore.document("users/$uid")
        if (uid != null) {
            Log.d("post", "hihi$uid")
        }
        return userDocument.get()
    }
}
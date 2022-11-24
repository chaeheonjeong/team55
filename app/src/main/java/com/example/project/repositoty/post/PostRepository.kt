package com.example.project.repositoty.post

import android.util.Log
import com.example.project.model.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// todo 2. comments 에서 user정보 제대로 가져오는 지 확인

class PostRepository(private val postKey: String) {
    private val postCollection = Firebase.firestore.collection("posts")


    // todo exists 조건절로 삭제 안한것만 가져오기
    fun getPost(): Task<DocumentSnapshot> {
        val documentRef = postCollection.document(postKey)
        return documentRef.get()
    }

    fun getWriter(writerUid: String): Task<DocumentSnapshot> {
        val writerDocument = Firebase.firestore.document("users/$writerUid")
        return writerDocument.get()
    }


    fun getUser(uid: String?): Task<DocumentSnapshot> {
        val userDocument = Firebase.firestore.document("users/$uid")
        if (uid != null) {
            Log.d("post", "hihi$uid")
        }
        return userDocument.get()
    }


    fun getComments(): Task<QuerySnapshot> {
        val commentsCollection =
            Firebase.firestore.collection("posts/$postKey/comments").orderBy("created_at")
        return commentsCollection.get()
    }

    fun getComments2(): Query {
        return Firebase.firestore.collection("posts/$postKey/comments")
            .orderBy("created_at")
            .whereEqualTo("exist", true)
    }
}

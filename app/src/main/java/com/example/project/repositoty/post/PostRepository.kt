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



class PostRepository(private val postKey: String) {
    private val postCollection = Firebase.firestore.collection("posts")


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


    fun getComments(): Query {
        val commentsCollection =
            Firebase.firestore.collection("posts/$postKey/comments")
                .orderBy("created_at")
                .whereEqualTo("exist", true)

        return commentsCollection
    }
}

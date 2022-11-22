package com.example.project.repositoty

import android.util.Log
import com.example.project.model.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TimelineRepository {
    private val db = Firebase.firestore

    fun getPostKeys(): Task<QuerySnapshot> {
        val queryKeysRef = db.collection("posts")
        return queryKeysRef.get()
    }
}
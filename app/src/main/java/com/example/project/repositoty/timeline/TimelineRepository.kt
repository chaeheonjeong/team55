package com.example.project.repositoty.timeline

import android.util.Log
import com.example.project.model.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TimelineRepository {
    private val db = Firebase.firestore

    // todo limit 걸어주고, 맨 아래로 가면 다시 로딩해주는 기술 공부
    fun getPostKeys(): Task<QuerySnapshot> {
        val queryKeysRef = db.collection("posts").orderBy("created_at", Query.Direction.DESCENDING)
        return queryKeysRef.get()
    }
}
package com.example.project.repositoty.timeline

import com.example.project.utils.Constants.Companion.PAGE_NUM
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TimelineRepository {
    private val db = Firebase.firestore

    // todo limit 걸어주고, 맨 아래로 가면 다시 로딩해주는 기술 공부
    // 2개 이상 조건을 걸어줄 떄는 firebase에 색인 추가를 해줘야 한다. (이건 해줌)
    fun getPostKeys(): Query {
        val postKeyCollection =  db.collection("posts")
            .whereEqualTo("exists", true)
            .orderBy("created_at", Query.Direction.DESCENDING)
            .limit(PAGE_NUM)

        return postKeyCollection
    }
}
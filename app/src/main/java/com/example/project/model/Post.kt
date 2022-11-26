package com.example.project.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

// TODO(데이터마다 필요 없는 것은 null로 해주되, 꼭 필요한 것은 null 안되게 설정 해보기)
data class Post(
    @get:PropertyName("post_key") @set:PropertyName("post_key") var postKey: String = "",
    val title: String = "",
    val content: String = "",
    @get:PropertyName("post_image") @set:PropertyName("post_image") var postImage: String = "",
    val comments: List<Comment?>? = null,
    @get:PropertyName("like_count") @set:PropertyName("like_count") var likeCount: Int = 0,
    @get:PropertyName("created_at") @set:PropertyName("created_at") var createdAt: Timestamp = Timestamp.now(),
    @get:PropertyName("writer_uid") @set:PropertyName("writer_uid") var writerUid: String = "",
    val exists: Boolean = true,
)


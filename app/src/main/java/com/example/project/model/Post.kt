package com.example.project.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

// TODO(데이터마다 필요 없는 것은 null로 해주되, 꼭 필요한 것은 null 안되게 설정 해보기)
data class Post(
    val title: String = "",
    val content: String = "",
    @get:PropertyName("post_images") @set:PropertyName("post_images") var postImages: List<String>? = null,
    val comments: List<Comment?>? = null,
    @get:PropertyName("like_count") @set:PropertyName("like_count") var likeCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(), // todo timestamp 타입으로 해보기
)

data class Comment(
    val content: String = "",
    val exists: Boolean = true,
    @get:PropertyName("profile_image") @set:PropertyName("profile_image") var profileImage: String = "",
    val name: String = "",
    @get:PropertyName("created_at") @set:PropertyName("created_at") var createdAt: Timestamp? = null, // todo Date 타입도 되는지 확인
)

data class User(
    val name: String = "",
    val email: String = "",
    val intro: String? = "",
    @get:PropertyName("profile_image") @set:PropertyName("profile_image") var profileImage: String = "",
    val posts: List<String>? = null,
    val friends: List<String>? = null,

    )


// todo 1. Comment 잘 하고

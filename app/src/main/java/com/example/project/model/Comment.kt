package com.example.project.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Comment(
    @get:PropertyName("comment_key") @set:PropertyName("comment_key") var commentKey: String = "",
    val name: String = "",
    @get:PropertyName("profile_image") @set:PropertyName("profile_image") var profileImage: String = "",
    val content: String = "",
    @get:PropertyName("created_at") @set:PropertyName("created_at") var createdAt: Timestamp = Timestamp.now(),
    val exists: Boolean = true,
)
package com.example.project.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val intro: String? = "",
    @get:PropertyName("profile_image") @set:PropertyName("profile_image") var profileImage: String = "",
    val posts: List<String>? = null,
    val friends: List<String>? = null,
    @get:PropertyName("created_at") @set:PropertyName("created_at") var createdAt: Timestamp = Timestamp.now(),
)
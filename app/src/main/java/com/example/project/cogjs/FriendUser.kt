package com.example.project.cogjs

import android.widget.ImageView
import com.google.firebase.firestore.PropertyName

data class FriendUser(
    var name : String? = null,
    @get:PropertyName("profile_image") @set:PropertyName("profile_image") var profileImage: String? = null
)
package com.example.project.ui.post

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.project.databinding.ActivityPostBinding
import com.example.project.model.Post

class PostActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: PostViewModel
    // private val uid = Firebase.auth.currentUser?.uid // todo 나중에 exception 처리 해주기
    private val uid = "0ZlSZEXqQnTW9q2KSVgttT8voTg1"
    private val postKey = "ttZ72mpZhaarPXQGxge1" // TODO(어디로부터 post key를 받았다 가정)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = PostViewModel(postKey, uid)
        binding()
    }

    private fun binding() {
        viewModel.post.observe(this) { post ->
            bindPost(post)
        }
        viewModel.user.observe(this) { user ->
            setImage(user.profileImage, binding.ivUserImage)
        }
    }

    private fun bindPost(post: Post) {
        setImage(post.postImage, binding.ivPostImage)

        binding.tvPostTitle.text = post.title
        binding.tvPostContent.text = post.content
    }

    private fun setImage(url: String, view: ImageView) {
        Glide.with(this)
            .load(url)
            .into(view)
    }
}
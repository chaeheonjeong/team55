package com.example.project.ui.post

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.project.databinding.ActivityPostBinding
import com.example.project.model.Comment
import com.example.project.model.Post
import com.example.project.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: PostViewModel

    // private val uid = Firebase.auth.currentUser?.uid // todo 나중에 exception 처리 해주기
    private val user = Firebase.auth.currentUser
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postKey = intent.getStringExtra("postKey")!!
        viewModel = PostViewModel(postKey, user!!.uid)
        val commentAdapter = CommentAdapter(this)
        binding.rvComments.adapter = commentAdapter

        binding(commentAdapter)
        setWriteComment(postKey)
    }

    private fun setWriteComment(postKey: String) {
        // - 데이터 쓰기 (댓글 작성)
        binding.tvWriteComment.setOnClickListener {
            // comment key 만들어주고, (add)
            // 작성해주고
            val commentValue = Comment(
                content = binding.etWriteComment.text.toString(),
                name = viewModel.user.value!!.name,
                profileImage = viewModel.user.value!!.profileImage
            )
            db.collection("posts/$postKey/comments").add(commentValue)
                .addOnSuccessListener { document ->
                    document.update(mapOf("comment_key" to document.id))
                    commentValue.commentKey = document.id
                    //viewModel.updateComments(commentValue)
                    // 그냥 요거 하면 불러오지 않을까?
                    viewModel.loadComments()
                    // 키보드 내리기
                    binding.etWriteComment.text = null
                    val inputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.etWriteComment.windowToken,
                        0)
                }
        }
    }

    private fun binding(commentAdapter: CommentAdapter) {
        viewModel.post.observe(this) { post ->
            bindPost(post)
        }
        viewModel.user.observe(this) { user ->
            setImage(user.profileImage, binding.ivUserImage)
        }
        viewModel.writer.observe(this) { writer ->
            bindWriter(writer)
        }
        viewModel.comments.observe(this) { comments ->
            Log.d("comments", "아니아니 작동해야된다구")
            commentAdapter.submitList(comments)
        }

    }

    private fun bindPost(post: Post) {
        setImage(post.postImage, binding.ivPostImage)

        binding.tvPostTitle.text = post.title
        binding.tvPostContent.text = post.content
    }

    private fun bindWriter(writer: User) {
        setImage(writer.profileImage, binding.ivWriterImage)
        binding.tvWriterName.text = writer.name
        binding.tvWriterFollower.text = "친구 수 ${writer.friends?.size}"
    }

    private fun setImage(url: String, view: ImageView) {
        Glide.with(this)
            .load(url)
            .into(view)
    }
}
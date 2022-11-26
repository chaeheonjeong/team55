package com.example.project.ui.post

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.project.databinding.ActivityPostBinding
import com.example.project.model.Comment
import com.example.project.model.Post
import com.example.project.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: PostViewModel

    // private val uid = Firebase.auth.currentUser?.uid // todo 나중에 exception 처리 해주기
    private val currentUser = Firebase.auth.currentUser
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postKey = intent.getStringExtra("postKey")!!
        viewModel = PostViewModel(postKey, currentUser!!.uid)
        val commentAdapter = CommentAdapter(this)
        binding.rvComments.adapter = commentAdapter

        bind(commentAdapter)
        setWriteComment(postKey)

        setFollowingButton(currentUser)
        setBackButton()

        binding.layoutWriter.setOnClickListener {

        }
    }

    private fun setBackButton() {
        binding.buttonBack.setOnClickListener {
            this.finish()
        }
    }

    private fun setFollowingButton(currentUser: FirebaseUser) {
        binding.buttonWriterFollow.setOnClickListener {
            FirebaseFirestore.getInstance().collection("users").document(currentUser.uid)
                .update("friends", FieldValue.arrayUnion(viewModel.writer.value?.uid))
                .addOnSuccessListener {
                    Toast.makeText(this, "팔로잉 하셨습니다.", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "팔로잉 실패", Toast.LENGTH_SHORT).show()
                }
        }
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
                    // 키보드 내리기
                    binding.etWriteComment.text = null
                    val inputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.etWriteComment.windowToken,
                        0)
                }
        }
    }

    private fun bind(commentAdapter: CommentAdapter) {
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
        binding.tvWriterFollower.text = "following ${writer.friends?.size}"
    }

    private fun setImage(url: String, view: ImageView) {
        Glide.with(this)
            .load(url)
            .into(view)
    }
}


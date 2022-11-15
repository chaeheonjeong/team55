package com.example.project.ui.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.project.databinding.FragmentPostBinding
import com.example.project.repositoty.post.PostRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PostFragment: Fragment(){
    private lateinit var binding: FragmentPostBinding
    // private val uid = Firebase.auth.currentUser?.uid // todo 나중에 exception 처리 해주기
    private val uid = "0ZlSZEXqQnTW9q2KSVgttT8voTg1"
    private val postKey = "ttZ72mpZhaarPXQGxge1" // TODO(어디로부터 post key를 받았다 가정)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = PostViewModel(postKey, uid)
        val repository = PostRepository(postKey)
        val storage = Firebase.storage

        viewModel.post.observe(viewLifecycleOwner) { post ->
            // TODO(이미지 설정하는 부분 -> xml에서 하지 않고 여기서 하기로 하자.)
            Glide.with(this)
                .load(post.postImages?.get(0))
                .into(binding.ivPostImage)
            binding.tvPostTitle.text = post.title
            binding.tvPostContent.text = post.content
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            Glide.with(this)
                .load(user.profileImage)
                .into(binding.ivUserImage)
        }

        
        // 1. post 추가
        // set -> document에서 사용, key 내가 지정
        // add -> collection에서 사용 auto Id 값 지정
    }
}
package com.example.project.ui.timeline

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.project.databinding.ItemLoadingBinding
import com.example.project.databinding.ItemPostBinding
import com.example.project.model.Post
import com.example.project.ui.post.PostActivity
import com.example.project.ui.timeline.ModalBottomSheet.Companion.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TimelineAdapter2(val fragment: TimelineFragment) :
    RecyclerView.Adapter<TimelineViewHolder>() {
    var differ = AsyncListDiffer(this, TimelineDiffUtil2())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(layoutInflater, parent, false)
        return TimelineViewHolder(binding, fragment)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val post = differ.currentList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}

class TimelineViewHolder(val binding: ItemPostBinding, val fragment: TimelineFragment) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    private lateinit var postKey: String
    private val currentUid = Firebase.auth.currentUser!!.uid

    // post 클릭 시 이동하도록 설정 해보기
    override fun onClick(view: View) {
        view.setOnClickListener {
            fragment.activity?.let {
                val intent = Intent(it, PostActivity::class.java)
                intent.putExtra("postKey", postKey)
                it.startActivity(intent)
            }
        }
    }

    fun bind(post: Post) {
        this.postKey = post.postKey
        binding.ivPostImage.setOnClickListener(this)
        binding.tvPostTitle.text = post.title
        setItemSettingButton(post.writerUid)
        setImageUI(post)
    }


    private fun setImageUI(post: Post) {
        Glide.with(fragment)
            .load(post.postImage)
            .into(binding.ivPostImage)
    }

    private fun setItemSettingButton(writerUid: String) {
        binding.buttonSettingPost.setOnClickListener {
            if(writerUid == currentUid)
                ModalBottomSheet(postKey).show(fragment.parentFragmentManager, TAG)
            else
                Toast.makeText(fragment.requireContext(), "작성자만 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show()
        }
    }

}

class TimelineDiffUtil2 : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.postKey == newItem.postKey
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
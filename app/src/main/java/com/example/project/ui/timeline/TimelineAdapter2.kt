package com.example.project.ui.timeline

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.project.databinding.ItemPostBinding
import com.example.project.model.Post
import com.example.project.ui.post.PostActivity
import com.example.project.ui.timeline.ModalBottomSheet.Companion.TAG

class TimelineAdapter2(val fragment: TimelineFragment) :
    RecyclerView.Adapter<TimelineViewHolder2>() {
    var differ = AsyncListDiffer(this, TimelineDiffUtil2())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder2 {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimelineViewHolder2(binding, fragment)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder2, position: Int) {
        val post = differ.currentList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}

class TimelineViewHolder2(val binding: ItemPostBinding, val fragment: TimelineFragment) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    private lateinit var postKey: String

    fun bind(post: Post) {
        this.postKey = post.postKey
        binding.ivPostImage.setOnClickListener(this)
        binding.tvPostTitle.text = post.title
        //setImageUI(post)
        setImageUI2(post)
        setItemSettingButton()
    }

    // todo 이미지 세로 크기에 따른 다르게 세팅하기
    private fun setImageUI2(post: Post) {
        binding.root.afterMeasured {
            Log.d("height", "hh ${this.height} ${this.maxHeight}" +
                    " // ${binding.cardViewPost.height} // ${binding.ivPostImage.height}")
            Glide.with(context)
                .load(post.postImage)
                .override(200, 1000)
                .into(binding.ivPostImage)
        }
    }

    /*

    }*/
    // define 'afterMeasured' layout listener:
    private inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }

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

    private fun setItemSettingButton() {
        binding.buttonSettingPost.setOnClickListener {
            ModalBottomSheet(postKey).show(fragment.parentFragmentManager, TAG)
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
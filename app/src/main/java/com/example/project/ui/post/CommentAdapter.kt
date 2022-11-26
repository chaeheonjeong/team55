package com.example.project.ui.post

import android.content.Context
import android.icu.text.RelativeDateTimeFormatter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.databinding.ItemCommentBinding
import com.example.project.model.Comment
import java.util.*

class CommentAdapter(val context: Context): ListAdapter<Comment, CommentAdapter.CommentViewHolder>(CommentDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommentBinding.inflate(inflater, parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CommentViewHolder(val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            setImage(comment.profileImage, binding.ivWriterImage)
            binding.tvComment.text = comment.content
            binding.tvName.text = comment.name
            createdAt(binding.tvCreatedDate, comment.createdAt.toDate().time)
            // binding.executePendingBindings()
        }

        private fun setImage(uri: String, view: ImageView) {
            Glide.with(context)
                .load(uri)
                .into(view)
        }
    }

    // 시간 계산 함수
    fun createdAt(view: TextView, createdTime: Long) {
        val formatter = RelativeDateTimeFormatter.getInstance(Locale("ko_KR"))
        var currentTime = (System.currentTimeMillis() - createdTime) / 1000
        val direction= RelativeDateTimeFormatter.Direction.LAST
        val relativeUnit: RelativeDateTimeFormatter.RelativeUnit

        if(currentTime < 60) {
            view.text = "방금 전"
            return
        }
        else if (currentTime >= 60 && currentTime < 60*60 ) {
            relativeUnit = RelativeDateTimeFormatter.RelativeUnit.MINUTES
            currentTime /= 60
        }
        else if (currentTime >= 60*60 && currentTime < 60*60*24){
            relativeUnit = RelativeDateTimeFormatter.RelativeUnit.HOURS
            currentTime /= (60 * 60)
        }
        else {
            relativeUnit = RelativeDateTimeFormatter.RelativeUnit.DAYS
            currentTime /= (60*60*24)
        }

        val formatTime = formatter.format(currentTime.toDouble(), direction, relativeUnit)
        Log.d("file", "format: $formatTime")
        view.text = formatTime
    }
}

class CommentDiffUtil: DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.commentKey == newItem.commentKey
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}
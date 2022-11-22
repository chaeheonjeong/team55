package com.example.project.ui.timeline

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.project.databinding.ItemPostBinding
import com.example.project.model.Post

// TODO item들 조금 띄워주고, 간격 맞춰주기. 그리고 이미지 여러개 넣어서 불규칙 하게 되는지 확인
class TimelineAdapter(val context: TimelineFragment): ListAdapter<Post, TimelineAdapter.TimelineViewHolder>(TimelineDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimelineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(getItem(position))
        //holder.itemClicked()
    }

    inner class TimelineViewHolder(private val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.tvPostTitle.text = post.title
            binding.tvPostContent.text = post.content

            setImageUI(post)
            // binding.executePendingBindings()
        }

        // 출처: https://stackoverflow.com/questions/74092706/android-how-to-get-image-width-and-height-from-url
        private fun setImageUI(post: Post) {
            Glide.with(context)
                .asBitmap()
                .load(post.postImage)
                .into(object : SimpleTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?,
                    ) {
                        val imgView = binding.ivPromotionImage
                        val w = resource.width
                        val h = resource.height

                        // 세로 길이가 1000이 넘으면 1000으로 고정
                        if (h > MAX_HEIGHT) {
                            binding.ivPromotionImage.layoutParams =
                                CoordinatorLayout.LayoutParams(imgView.width, MAX_HEIGHT)
                        }
                        binding.ivPromotionImage.setImageBitmap(resource);
                    }
                })
        }

        // post 클릭 시 이동하도록 설정 해보기
//        fun itemClicked() {
//            binding.root.setOnClickListener {
//                val action = SearchFragmentDirections.actionSearchToUserProfile(getItem(adapterPosition).userUid)
//                it.findNavController().navigate(action)
//            }
//        }

        // 또는 이걸로
//        private fun setMoveUserProfile(post: Post) {
//            val action = HomeFragmentDirections.actionHomeToUserProfile(post.writer.userUid)
//            val clickListener = View.OnClickListener {
//                context.findNavController().navigate(action)
//            }
//
//            binding.ivProfileImage.setOnClickListener(clickListener)
//            binding.tvProfileName.setOnClickListener(clickListener)
//        }

    }

    companion object {
        const val MAX_HEIGHT = 1000
    }

}

class TimelineDiffUtil: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.postKey == newItem.postKey
    }
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
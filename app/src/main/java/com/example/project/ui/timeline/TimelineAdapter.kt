//package com.example.project.ui.timeline
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.constraintlayout.widget.Constraints
//import androidx.core.content.ContextCompat.startActivity
//import androidx.recyclerview.widget.*
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.target.SimpleTarget
//import com.bumptech.glide.request.transition.Transition
//import com.example.project.databinding.ItemPostBinding
//import com.example.project.model.Post
//import com.example.project.ui.post.PostActivity
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class TimelineAdapter(val fragment: TimelineFragment) :
//    ListAdapter<Post, TimelineViewHolder>(TimelineDiffUtil()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
//        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return TimelineViewHolder(binding, fragment)
//    }
//
//    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
//        if (holder.adapterPosition != RecyclerView.NO_POSITION)
//            holder.bind(getItem(holder.adapterPosition))
//    }
//}
//
//class TimelineViewHolder(private val binding: ItemPostBinding, val fragment: TimelineFragment) :
//    RecyclerView.ViewHolder(binding.root), View.OnClickListener {
//    private lateinit var postKey: String
//
//    fun bind(post: Post) {
//        this.postKey = post.postKey
//        itemView.setOnClickListener(this)
//
//        binding.tvPostTitle.text = post.title
//        //setImageUI(post)
//        setSimpleImageUI(post)
//
//        // binding.executePendingBindings()
//    }
//
//    fun setSimpleImageUI(post: Post) {
//        Glide.with(fragment)
//            .load(post.postImage)
//            .into(binding.ivPostImage)
//    }
//
//    // 출처: https://stackoverflow.com/questions/74092706/android-how-to-get-image-width-and-height-from-url
//    private fun setImageUI(post: Post) {
//        Glide.with(fragment)
//            .asBitmap()
//            .load(post.postImage)
//            .into(object : SimpleTarget<Bitmap?>() {
//                override fun onResourceReady(
//                    resource: Bitmap,
//                    transition: Transition<in Bitmap?>?,
//                ) {
//                    val imgView = binding.ivPostImage
//                    val w = resource.width
//                    val h = resource.height
//
//                    // 세로 길이가 1000이 넘으면 1000으로 고정
//                    if (h > MAX_HEIGHT) {
//                        binding.ivPostImage.layoutParams =
//                            Constraints.LayoutParams(imgView.width, MAX_HEIGHT)
//                    }
//                    binding.ivPostImage.setImageBitmap(resource);
//                }
//            })
//
//    }
//
//    // post 클릭 시 이동하도록 설정 해보기
//    override fun onClick(view: View) {
//        view.setOnClickListener {
//            fragment.activity?.let {
//                val intent = Intent(it, PostActivity::class.java)
//                intent.putExtra("postKey", postKey)
//                it.startActivity(intent)
//            }
//        }
//    }
//
//    companion object {
//        const val MAX_HEIGHT = 1000
//    }
//}
//
//class TimelineDiffUtil : DiffUtil.ItemCallback<Post>() {
//    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
//        return oldItem.postKey == newItem.postKey
//    }
//
//    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
//        return oldItem == newItem
//    }
//}
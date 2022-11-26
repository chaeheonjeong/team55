package com.example.project.MS


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.project.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val db = Firebase.firestore
//private val items: ArrayList<String>
//(private val context: Fragment?)
class RecyclerItemAdapter : RecyclerView.Adapter<RecyclerItemAdapter.ViewHolder>() {

    var datas = mutableListOf<PostData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgProfile: ImageView = itemView.findViewById(R.id.postImage)
        private val text: TextView = itemView.findViewById(R.id.textView3)

        fun bind(item: PostData) {
            if (user != null) {
                Firebase.firestore.collection("posts").document(item.postImg)
                    .addSnapshotListener { documentSnapshot, _ ->
                        if (documentSnapshot == null) {
                            Glide.with(itemView).load(R.drawable.pro2).into(imgProfile)
                            text.text = "1"
                            return@addSnapshotListener
                        }
                        if (documentSnapshot.data != null) {
                            val url = documentSnapshot.data!!["post_image"]
                            Glide.with(itemView).load(url).apply(RequestOptions()).into(imgProfile)
                            text.text = "2"
                        }
                    }
            }
            //Glide.with(itemView).load(item.postImg).into(imgProfile)
        }
    }

/*
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]
        val listener = View.OnClickListener { it ->
            //Toast.makeText(it.context, "Clicked -> ID : ${item.postTitle}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            //bind(listener, item)
            itemView.tag = item
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(private val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            db.collection("posts")
                .whereEqualTo("writer_uid", user)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                        if (user != null) {
                            Firebase.firestore.collection("posts").document(document.id)
                                .addSnapshotListener { documentSnapshot, _ ->
                                    if (documentSnapshot == null) return@addSnapshotListener
                                    if (documentSnapshot.data != null) {
                                        val url = documentSnapshot.data!!["post_image"]
                                        Glide.with(binding.root).load(url).apply(RequestOptions())
                                            .into(binding.postImage)
                                    }
                                }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                }
        }
    }

 */
}
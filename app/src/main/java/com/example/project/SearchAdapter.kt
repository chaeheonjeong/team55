package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.ItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class MyItem(val id: String, val name: String,val email:String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id, doc["name"].toString(), doc["email"].toString()
            )
    constructor(key: String, map: Map<*, *>) :
            this(key, map["name"].toString(), map["email"].toString())
}

class SearchViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

class SearchAdapter(private var items: List<MyItem>)
    : RecyclerView.Adapter<SearchViewHolder>() {

    private val db: FirebaseFirestore = Firebase.firestore
    private var auth: FirebaseAuth = Firebase.auth

    fun interface OnItemClickListener {
        fun onItemClick(student_id: String)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemBinding = ItemBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val itemsCollectionRef = db.collection("test")
        val item = items[position]

        holder.binding.textUserName.text = item.name
        holder.binding.textEmail.text = item.email
        val uid = auth.currentUser?.uid

        holder.binding.AddBtn.setOnClickListener {
            FirebaseFirestore.getInstance().collection("test").document(uid!!)
                .update("friends", FieldValue.arrayUnion(item.id))
                .addOnSuccessListener {
                    println("친구 추가 성공 + ${item.name}")
                }.addOnFailureListener { println("친구 추가 실패 + name : ${item.name}") }

            holder.binding.AddBtn.isSelected = holder.binding.AddBtn.isSelected != true
        }
    }
    override fun getItemCount() = items.size

    fun updateList(newList: List<MyItem>) {
        items = newList
        notifyDataSetChanged()
    }
}
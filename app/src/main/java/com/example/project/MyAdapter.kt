package com.example.project

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.ItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Item(val id: String, val name: String,val email:String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id, doc["name"].toString(), doc["email"].toString()
            )
    constructor(key: String, map: Map<*, *>) :
            this(key, map["name"].toString(), map["email"].toString())
}

class MyViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(private val context: Context, private var items: List<Item>)
    : RecyclerView.Adapter<MyViewHolder>() {

    private val db: FirebaseFirestore = Firebase.firestore
    private var auth: FirebaseAuth = Firebase.auth

    fun interface OnItemClickListener {
        fun onItemClick(student_id: String)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun updateList(newList: List<Item>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemBinding = ItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemsCollectionRef = db.collection("test")
        val item = items[position]

        holder.binding.textUserName.text = item.name
        holder.binding.textEmail.text = item.email
        val uid = auth.currentUser?.uid

        if(uid == itemsCollectionRef.document(item.id).toString()) {
            holder.binding.AddBtn.isEnabled = false
        }

        holder.binding.AddBtn.setOnClickListener {
            /*var map:String = item.name
            itemsCollectionRef.document(uid!!).update("friends",map)
                .addOnSuccessListener { println("친구 추가 성공") }.addOnFailureListener { println("친구 추가 실패 + name : ${item.name}")  }

             */
            var map= mutableMapOf<String,Any>()
            map["name"] =item.name


            FirebaseFirestore.getInstance().collection("test").document(uid!!)
                .update("friends", FieldValue.arrayUnion(item.name))
                .addOnSuccessListener {
                    println("친구 추가 성공 + ${item.name}")
                }. addOnFailureListener{ println("친구 추가 실패 + name : ${item.name}")  }
        }
    }

    override fun getItemCount() = items.size
}
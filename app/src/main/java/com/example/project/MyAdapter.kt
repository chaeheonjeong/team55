package com.example.project

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.ItemBinding
import com.google.firebase.firestore.QueryDocumentSnapshot

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
        val item = items[position]
        holder.binding.textUserName.text = item.name
        holder.binding.textEmail.text = item.email
        holder.binding.AddBtn.setOnClickListener {
            println("======친구 추가 구현=======")
        }
    }

    override fun getItemCount() = items.size
}
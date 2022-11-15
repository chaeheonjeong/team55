package com.example.project

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class FriendsList: Fragment() {
    val db = Firebase.firestore

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.friendslist, container, false)

        var recyclerview = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.adapter = RecyclerViewAdapter()
        recyclerview.layoutManager = LinearLayoutManager(context as Activity)

        return view
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val userUid = "x3kojX3jFWX9O5MJoStwXiMBNB83"
        // Person 클래스 ArrayList 생성성
        var FriendsList : ArrayList<FriendUser> = arrayListOf()
        var tempFriendsList : ArrayList<String> = arrayListOf()

        init {
            db?.collection("users")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                FriendsList.clear()
                tempFriendsList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    if (snapshot.id == userUid) {
                        tempFriendsList = snapshot["friends"] as ArrayList<String>
                    }
                    notifyDataSetChanged()
                }
                for (snapshot in querySnapshot!!.documents) {
                    for(i in tempFriendsList) {
                        if (snapshot.id == i) {
                            var item = snapshot.toObject(FriendUser::class.java)
                            FriendsList.add(item!!)
                        }
                        notifyDataSetChanged()
                    }
                }
            }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.friendslist_item, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView

            var name = viewHolder.findViewById<TextView>(R.id.name)
            name.text = FriendsList[position].name
        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return FriendsList.size
        }
    }
}

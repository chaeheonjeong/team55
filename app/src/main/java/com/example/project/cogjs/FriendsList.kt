package com.example.project.cogjs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.cogjs.FriendProfile
import com.example.project.MainActivity
import com.example.project.R
import com.example.project.databinding.FriendslistBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FriendsList: Fragment() {
    val db = Firebase.firestore
    val storage = Firebase.storage
    private var fragmentFriendProfile = FriendProfile()
    lateinit var profile:ImageView
    lateinit var  mainActivity: MainActivity
    lateinit var binding: FriendslistBinding
    lateinit var deleteUid1: String
    lateinit var deleteUid2: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FriendslistBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.recyclerview
        recyclerView.adapter = RecyclerViewAdapter()

    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val userUid = Firebase.auth.currentUser?.uid

        // Person 클래스 ArrayList 생성성
        var friendsList: ArrayList<FriendUser> = arrayListOf()
        var tempFriendsList: ArrayList<String>? = arrayListOf()
        var forDeleteList: HashMap<String, Any>? = HashMap()

        init {
            db.collection("users")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    friendsList.clear()
                    tempFriendsList?.clear()
                    forDeleteList?.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        if (snapshot.id == userUid) {
                            tempFriendsList = snapshot["friends"] as ArrayList<String>?
                        }
                        notifyDataSetChanged()
                    }

                    for (snapshot in querySnapshot!!.documents) {
                        for (i in tempFriendsList!!) {
                            if (snapshot.id == i) {
                                var item = snapshot.toObject(FriendUser::class.java)
                                friendsList.add(item!!)
                                forDeleteList?.put(i, item)
                            }
                            notifyDataSetChanged()
                        }
                    }
                }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.friendslist_item, parent, false)

            /*profile = view.findViewById(R.id.friend_profile)
            profile.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    val intent = Intent(context, FriendProfile::class.java)
                    intent.putExtra("friendUid", deleteUid2)
                    startActivity(intent)
                }
            })*/

            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
            var viewHolder = (holder as ViewHolder).itemView

            var name = viewHolder.findViewById<TextView>(R.id.name)
            name.text = friendsList[position].name

            var profile = viewHolder.findViewById<ImageView>(R.id.friend_profile)
            Glide.with(holder.itemView.context).load(friendsList[position].profileImage)
                .into(profile)

            var delete = viewHolder.findViewById<Button>(R.id.delete)
            delete.setOnClickListener {
            //Toast.makeText(context, "${deleteUid2}", Toast.LENGTH_SHORT).show()
                deleteUid1 = forDeleteList?.filterValues { it == friendsList[position]}?.keys.toString()
                deleteUid2 = deleteUid1.replace("[","").replace("]","")
                if (userUid != null) {
                    db.collection("users").document(userUid)
                        .update("friends", FieldValue.arrayRemove("$deleteUid2"))
                }
            }

           profile.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    deleteUid1 = forDeleteList?.filterValues { it == friendsList[position]}?.keys.toString()
                    deleteUid2 = deleteUid1.replace("[","").replace("]","")
                    val intent = Intent(context, FriendProfile::class.java)
                    intent.putExtra("friendUid", deleteUid2)
                    startActivity(intent)
                }
            })
        }

            // 리사이클러뷰의 아이템 총 개수 반환
            override fun getItemCount(): Int {
                return friendsList.size
            }

        }
}


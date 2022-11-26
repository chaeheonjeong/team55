package com.example.project.cogjs

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.project.CustomDialog
import com.example.project.MS.PostData
import com.example.project.MS.db
import com.example.project.MS.user
import com.example.project.R
import com.example.project.databinding.ActivityFriendProfileBinding
import com.example.project.databinding.FragmentProfile2Binding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FriendProfile: AppCompatActivity() {
    val data = mutableListOf<PostData>()
    private lateinit var binding: ActivityFriendProfileBinding
    private lateinit var postImages: List<String>
    var friendUid : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        friendUid = intent.getStringExtra("friendUid").toString()

        val data = mutableListOf<PostData>()
        var editMode = false
        val db = Firebase.firestore
        val itemsCollectionRef = db.collection("users")
        val user = Firebase.auth.currentUser?.uid

        val list = ArrayList<PostData>()
        //list.add(PostData(, "1"))


        fun getProfileImage() {
            Firebase.firestore.collection("users").document("$friendUid")
                .addSnapshotListener { documentSnapshot, _ ->
                    if (documentSnapshot == null) return@addSnapshotListener
                    if (documentSnapshot.data != null) {
                        val url = documentSnapshot.data!!["profile_image"]
                        Glide.with(applicationContext).load(url).apply(RequestOptions()).into(binding.profilePic)
                    }
                }
        }

        fun atStart() = itemsCollectionRef.document("$friendUid").get().addOnSuccessListener {
            getProfileImage()
            binding.nicknameTextview.text = it["name"] as CharSequence?
            binding.IntroduceTextview.text = it["introduce"] as CharSequence?
        }

        atStart()
        initRecycler()

    }

    private fun initRecycler() {
        //binding.recyclerView.layoutManager = LinearLayoutManager(context)
        //binding.recyclerView.adapter = RecyclerItemAdapter(context)
        //recyclerItemAdapter = RecyclerItemAdapter()
        data.apply {
            db.collection("posts")
                .whereEqualTo("writer_uid", "$friendUid")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        add(PostData(postImg = document.id))
                        //binding.textView.text = data.size.toString()

                        for (i in 0 until data.size) {
                            if (i == 0) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(binding.root).load(url).apply(RequestOptions()).into(binding.imageView2)
                                        }
                                    }
                            }

                            if (i == 1) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(binding.root).load(url).apply(RequestOptions()).into(binding.imageView3)
                                        }
                                    }
                            }

                            if (i == 2) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(binding.root).load(url).apply(RequestOptions()).into(binding.imageView4)
                                        }
                                    }
                            }

                            if (i == 3) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(binding.root).load(url).apply(RequestOptions()).into(binding.imageView5)
                                        }
                                    }
                            }

                            if (i == 4) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(binding.root).load(url).apply(RequestOptions()).into(binding.imageView6)
                                        }
                                    }
                            }

                            if (i == 5) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(binding.root).load(url).apply(RequestOptions()).into(binding.imageView7)
                                        }
                                    }
                            }

                            if (i == 6) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(binding.root).load(url).apply(RequestOptions()).into(binding.imageView8)
                                        }
                                    }
                            }

                            if (i == 7) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(binding.root).load(url).apply(RequestOptions()).into(binding.imageView9)
                                        }
                                    }
                            }

                            if (i == 8) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(binding.root).load(url).apply(RequestOptions()).into(binding.imageView10)
                                        }
                                    }
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
            //recyclerItemAdapter.datas = data
            //recyclerItemAdapter.notifyDataSetChanged()
            //binding.recyclerView.adapter = recyclerItemAdapter
        }
    }
}
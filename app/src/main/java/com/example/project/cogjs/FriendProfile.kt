package com.example.project.cogjs

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.project.CustomDialog
import com.example.project.MS.PostData
import com.example.project.R
import com.example.project.databinding.ActivityFriendProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FriendProfile: AppCompatActivity() {
    private lateinit var postImages: List<String>
    var friendUid : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFriendProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        friendUid = intent.getStringExtra("friendUid").toString()

        var editMode = false
        val dialog = CustomDialog(this)
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
            binding.recyclerView
        }

        atStart()

        //binding.profilePic.setImageResource(R.drawable.pro2)

        binding.profilePic.setOnClickListener {
            if (editMode) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                launcher.launch(intent)
            }
        }

        binding.nicknameTextview.setOnClickListener {
            if (editMode) {
                dialog.myDig()
                dialog.setOnClickListener(object : CustomDialog.ButtonClickListener {
                    override fun onClicked(myName: String) {
                        if(myName != "") {
                            binding.nicknameTextview.text = myName
                            db.collection("users").document("$friendUid")
                                .update(mapOf("name" to myName))
                        }
                    }
                })
            }
        }

        binding.IntroduceTextview.setOnClickListener {
            if (editMode) {
                dialog.myDig()
                dialog.setOnClickListener(object : CustomDialog.ButtonClickListener {
                    override fun onClicked(myName: String) {
                        if (myName != "") {
                            binding.IntroduceTextview.text = myName
                            db.collection("users").document("$friendUid")
                                .update(mapOf("introduce" to myName))
                        }
                    }
                })
            }
        }
    }

    private fun loadPosts() {
        Firebase.firestore.collection("users").document("$friendUid")
            .addSnapshotListener { documentSnapshot, _ ->
                if (documentSnapshot == null) return@addSnapshotListener
                if (documentSnapshot.data != null) {
                    val postImages: List<String>? = documentSnapshot.toObject<List<String>>()
                }
            }
    }

    private var launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.e(TAG, "result : $result")
                val intent: Intent? = result.data
                Log.e(TAG, "intent : $intent")
                val uri = intent?.data
                Log.e(TAG, "uri : $uri")
                findViewById<ImageView>(R.id.profilePic).setImageURI(uri)
                if (uri != null) {
                    uploadToStorage(uri, "$friendUid")
                }
            }
        }
    private fun uploadToStorage(uri: Uri, postKey: String) {
        val fileName = System.currentTimeMillis()
        val ref = Firebase.storage.reference.child("post_image/$fileName.jpg")
        ref.putFile(uri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                // 첫 번째 downloadUri가 Complete 됐을 때 database의 image를 업데이트 해준다.
                val db = Firebase.firestore
                db.collection("users").document("$postKey").update(mapOf("profile_image" to downloadUri))
            }
        }
    }
}
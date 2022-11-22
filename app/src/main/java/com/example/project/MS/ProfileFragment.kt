package com.example.project.MS

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.project.R
import com.example.project.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileFragment: Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var editMode = false
        val dialog = CustomDialog(requireContext())
        val db = Firebase.firestore
        val itemsCollectionRef = db.collection("users")
        val user = Firebase.auth.currentUser?.uid

        val list = ArrayList<PostData>()
        //list.add(PostData())

        val adapter = RecyclerItemAdapter(list)
        //gridView.adapter = adapter
        //gridView.layoutManager = GridLayoutManager(this, 3)

        fun getProfileImage() {
            Firebase.firestore.collection("users").document("Uk9OMZwoSPXY46VKbfxG")
                .addSnapshotListener { documentSnapshot, _ ->
                    if (documentSnapshot == null) return@addSnapshotListener
                    if (documentSnapshot.data != null) {
                        val url = documentSnapshot.data!!["profile_image"]
                        Glide.with(requireContext()).load(url).apply(RequestOptions()).into(binding.profilePic)
                    }
                }
        }

        fun atStart() = itemsCollectionRef.document("Uk9OMZwoSPXY46VKbfxG").get().addOnSuccessListener {
            getProfileImage()
            binding.nicknameTextview.text = it["name"] as CharSequence?
            binding.IntroduceTextview.text = it["introduce"] as CharSequence?
            binding.recyclerView
        }

        atStart()

        binding.deleteAccountButton.visibility = View.INVISIBLE
        binding.logoutButton.visibility = View.INVISIBLE

        binding.editButton.setOnClickListener {
            if(!editMode) {
                editMode = true
                binding.editButton.text = "수정 완료"
                binding.deleteAccountButton.visibility = View.VISIBLE
                binding.logoutButton.visibility = View.VISIBLE
            }
            else {
                editMode = false
                binding.editButton.text = "프로필 수정"
                binding.deleteAccountButton.visibility = View.INVISIBLE
                binding.logoutButton.visibility = View.INVISIBLE
            }
        }

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
                            db.collection("users").document("Uk9OMZwoSPXY46VKbfxG")
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
                            db.collection("users").document("Uk9OMZwoSPXY46VKbfxG")
                                .update(mapOf("introduce" to myName))
                        }
                    }
                })
            }
        }

        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            //finish()
        }
    }

    private fun loadPosts() {
        Firebase.firestore.collection("users").document("Uk9OMZwoSPXY46VKbfxG")
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
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                Log.e(ContentValues.TAG, "result : $result")
                val intent: Intent? = result.data
                Log.e(ContentValues.TAG, "intent : $intent")
                val uri = intent?.data
                Log.e(ContentValues.TAG, "uri : $uri")
                view?.findViewById<ImageView>(R.id.profilePic)?.setImageURI(uri)
                if (uri != null) {
                    uploadToStorage(uri, "Uk9OMZwoSPXY46VKbfxG")
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
                db.collection("users").document("$postKey")
                    .update(mapOf("profile_image" to downloadUri))
            }
        }

    }
}
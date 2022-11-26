package com.example.project.MS

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.project.LoginActivity
import com.example.project.R
import com.example.project.databinding.FragmentProfile2Binding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

val user = Firebase.auth.currentUser?.uid

class ProfileFragment: Fragment() {
    //lateinit var recyclerItemAdapter: RecyclerItemAdapter
    val data = mutableListOf<PostData>()
    private lateinit var binding: FragmentProfile2Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfile2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var editMode = false
        val dialog = CustomDialog(requireContext())
        val db = Firebase.firestore
        val itemsCollectionRef = db.collection("test")

        fun getProfileImage() {
            if (user != null) {
                Firebase.firestore.collection("test").document(user)
                    .addSnapshotListener { documentSnapshot, _ ->
                        if (documentSnapshot == null) return@addSnapshotListener
                        if (documentSnapshot.data != null) {
                            val url = documentSnapshot.data!!["profile_image"]
                            Glide.with(requireContext()).load(url).apply(RequestOptions()).into(binding.profilePic)
                        }
                    }
            }
        }

        fun atStart() = user?.let { it ->
            itemsCollectionRef.document(it).get().addOnSuccessListener {
                getProfileImage()
                binding.nicknameTextview.text = it["name"] as CharSequence?
                binding.IntroduceTextview.text = it["introduce"] as CharSequence?
            }
        }

        atStart()
        initRecycler()

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

        binding.profilePic.setOnClickListener {
            if (editMode) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.action = Intent.ACTION_PICK
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
                            if (user != null) {
                                db.collection("test").document(user)
                                    .update(mapOf("name" to myName))
                            }
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
                            if (user != null) {
                                db.collection("test").document(user)
                                    .update(mapOf("introduce" to myName))
                            }
                        }
                    }
                })
            }
        }

        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(
                Intent(activity, LoginActivity::class.java)
            )
        }

        binding.deleteAccountButton.setOnClickListener {
            val user = Firebase.auth.currentUser!!

            user.delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }
        }
    }

    private fun initRecycler() {
        //binding.recyclerView.layoutManager = LinearLayoutManager(context)
        //binding.recyclerView.adapter = RecyclerItemAdapter(context)
        //recyclerItemAdapter = RecyclerItemAdapter()
        data.apply {
            db.collection("posts")
                .whereEqualTo("writer_uid", user)
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
                                            Glide.with(requireContext()).load(url).apply(RequestOptions()).into(binding.imageView2)
                                        }
                                    }
                            }

                            if (i == 1) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(requireContext()).load(url).apply(RequestOptions()).into(binding.imageView3)
                                        }
                                    }
                            }

                            if (i == 2) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(requireContext()).load(url).apply(RequestOptions()).into(binding.imageView4)
                                        }
                                    }
                            }

                            if (i == 3) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(requireContext()).load(url).apply(RequestOptions()).into(binding.imageView5)
                                        }
                                    }
                            }

                            if (i == 4) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(requireContext()).load(url).apply(RequestOptions()).into(binding.imageView6)
                                        }
                                    }
                            }

                            if (i == 5) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(requireContext()).load(url).apply(RequestOptions()).into(binding.imageView7)
                                        }
                                    }
                            }

                            if (i == 6) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(requireContext()).load(url).apply(RequestOptions()).into(binding.imageView8)
                                        }
                                    }
                            }

                            if (i == 7) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(requireContext()).load(url).apply(RequestOptions()).into(binding.imageView9)
                                        }
                                    }
                            }

                            if (i == 8) {
                                Firebase.firestore.collection("posts").document(data[i].postImg)
                                    .addSnapshotListener { documentSnapshot, _ ->
                                        if (documentSnapshot == null) return@addSnapshotListener
                                        if (documentSnapshot.data != null) {
                                            val url = documentSnapshot.data!!["post_image"]
                                            Glide.with(requireContext()).load(url).apply(RequestOptions()).into(binding.imageView10)
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

    private var launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                Log.e(TAG, "result : $result")
                val intent: Intent? = result.data
                Log.e(TAG, "intent : $intent")
                val uri = intent?.data
                Log.e(TAG, "uri : $uri")
                view?.findViewById<ImageView>(R.id.profilePic)?.setImageURI(uri)
                if (uri != null) {
                    if (user != null) {
                        uploadToStorage(uri, user)
                    }
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
                db.collection("test").document(postKey)
                    .update(mapOf("profile_image" to downloadUri))
            }
        }
    }
}
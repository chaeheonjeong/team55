package com.example.project

import android.content.ContentUris
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Profile: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityProfileBinding.inflate(layoutInflater)
    setContentView(binding.root)

        var editMode = false
        var dialog = CustomDialog(this)
        val rootRef = Firebase.storage.reference
        var imgRef = R.drawable.pro2

        binding.editButton.setOnClickListener {
            if(editMode == false) {
                editMode = true
                binding.editButton.setText("수정 완료")
            }
            else {
                editMode = false
                binding.editButton.setText("프로필 수정")
            }
        }

        binding.profilePic.setImageResource(R.drawable.pro2)

        binding.profilePic.setOnClickListener {
            if (editMode == true) {
                listPhotosDialog()
            }
        }

        binding.nicknameTextview.setOnClickListener {
            if (editMode == true) {
                dialog.myDig()
                dialog.setOnClickListener(object : CustomDialog.ButtonClickListener {
                    override fun onClicked(myName: String) {
                        binding.nicknameTextview.text = myName
                    }
                })
            }
        }

        binding.IntroduceTextview.setOnClickListener {
            if (editMode == true) {
                dialog.myDig()
                dialog.setOnClickListener(object : CustomDialog.ButtonClickListener {
                    override fun onClicked(myName: String) {
                        binding.IntroduceTextview.text = myName
                    }
                })
            }
        }
    }

    companion object {
        const val UPLOAD_FOLDER = "upload_images/"
    }

    private fun listPhotosDialog() {
        Firebase.storage.reference.child(UPLOAD_FOLDER).listAll()
            .addOnSuccessListener {
                val itemsString = mutableListOf<String>()
                for (i in it.items) {
                    itemsString.add(i.name)
                }
                AlertDialog.Builder(this)
                    .setTitle("Uploaded Photos")
                    .setItems(itemsString.toTypedArray(), {_, i -> })
                    .show()
            }.addOnFailureListener {

            }
    }

    /*
    private fun uploadFile(file_id: Long, fileName: String) {
        val imageRef = Firebase.storage.reference.child("${UPLOAD_FOLDER}${fileName}") // StorageReference
        val contentUri = ContentUris.withAppendedId(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            file_id)
        imageRef.putFile(contentUri).addOnCompleteListener {
            if (it.isSuccessful) {// upload success
                Snackbar.make(binding.root, "Upload completed.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    */
}
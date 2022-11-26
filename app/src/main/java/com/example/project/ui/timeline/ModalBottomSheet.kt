package com.example.project.ui.timeline
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.project.databinding.ModalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class ModalBottomSheet(private val postKey: String) : BottomSheetDialogFragment() {
    private lateinit var binding: ModalBottomSheetBinding
    private val user = Firebase.auth.currentUser
    private val db = Firebase.firestore

    companion object {
        const val TAG = "ModalBottomSheet"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ModalBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = "이 게시물을 삭제하시겠어요?"
        val message = "삭제하시면 다시 복구하실 수 없습니다. 그래도 삭제하시겠습니까?"
        val negative = "삭제 안함"
        val positive = "삭제"
        binding.tvDeletePost.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(negative) { dialog, which ->
                    // Respond to negative button press
                    dialog.dismiss()
                    dismiss()
                }
                .setPositiveButton(positive) { dialog, which ->
                    // Respond to positive button press
                    Toast.makeText(requireContext(), "삭제되셨습니다.", Toast.LENGTH_SHORT).show()
                    acceptDelete()
                    dismiss()
                }
                .show()
        }
    }

    private fun acceptDelete() {
        val uid = user!!.uid
        val userRef = db.document("users/$uid")
        val postRef = db.document("posts/$postKey")
        db.runBatch {
            postRef.get().addOnSuccessListener { document ->
                val writerUid = document.getField<String>("writer_uid")
                if(writerUid != uid) {
                    Toast.makeText(requireContext(), "작성자가 아닙니다.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                userRef.update("posts", FieldValue.arrayRemove(postKey))
                postRef.update(mapOf("exists" to false))
            }
        }
    }
}
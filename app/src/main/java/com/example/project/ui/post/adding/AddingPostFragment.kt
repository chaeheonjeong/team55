package com.example.project.ui.post.adding

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.project.databinding.FragmentAddingPostBinding
import com.example.project.model.Post
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

// todo 크기에 따른 압축 로직 만들기
class AddingPostFragment : Fragment() {
    private lateinit var binding: FragmentAddingPostBinding
    private val user = Firebase.auth.currentUser
    lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var db: FirebaseFirestore
    private lateinit var fireStorage: FirebaseStorage
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddingPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.firestore
        fireStorage = Firebase.storage

        setLauncher()

        setButtonAddingImage()
        setButtonAddingPost()
    }


    private fun setButtonAddingImage() {
        binding.ivAddingImage.setOnClickListener { v ->
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            // todo mediaStore 쓰지 말라고 했던가? 어차피 사용 안 할거긴 한데..
            // intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            launcher.launch(intent)
        }
    }

    private fun setButtonAddingPost() {
        binding.buttonAddingPost.setOnClickListener {
            if (imageUri == null) {
                Toast.makeText(activity, "이미지를 선택해 주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 키보드 내리기 todo 함수처리
            if (activity != null && requireActivity().currentFocus != null) {
                // 프래그먼트기 때문에 getActivity() 사용
                val inputManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
            }


            // 기본적인 정보를 일단 db에 올린다.
            val postValue = Post(
                title = binding.etAddingTitle.text.toString(),
                content = binding.etAddingContent.text.toString(),
                writerUid = user!!.uid
            )

            showProgressBar()
            // postKey를 auto로 만든 후, 나중에 필드 값에 postKey, imageUri 저장
            val userRef = db.document("users/${user.uid}")
            db.collection("posts").add(postValue)
                .addOnSuccessListener { document ->
                    document.update(mapOf("post_key" to document.id))
                    userRef.update("posts", FieldValue.arrayUnion(document.id)) // user에도 추가
                    uploadToStorage(imageUri!!, document.id)
                    // 뒤로가기 다시 구현(규민님거 보고)
                    // requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
                }
        }
    }


    private fun setLauncher() {
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent: Intent? = result.data
                if (intent != null)
                    imageUri = intent.data

                Glide.with(this@AddingPostFragment)
                    .load(imageUri)
                    .into(binding.ivAddingImage)
            }
        }
    }

    // button ->
    private fun uploadToStorage(uri: Uri, postKey: String) {
        val fileName = System.currentTimeMillis()
        val ref = fireStorage.reference.child("post_image/$fileName.jpg")
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
                db.document("posts/$postKey").update(mapOf("post_image" to downloadUri))
                // 로딩화면 띄워주기 (UI 추가하고 여기에 어케하지>>??)
                hideProgressBar()
                Toast.makeText(activity, "Post 업로드가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 프로그레스바 보이기
    private fun showProgressBar() {
        val pBar = binding.progressBar
        pBar.isVisible = true
        blockLayoutTouch()
    }

    // 프로그레스바 숨기기
    private fun hideProgressBar() {
        val pBar = binding.progressBar
        clearBlockLayoutTouch()
        pBar.isVisible = false
    }

    // 화면 터치 막기
    private fun blockLayoutTouch() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    // 화면 터치 풀기
    private fun clearBlockLayoutTouch() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
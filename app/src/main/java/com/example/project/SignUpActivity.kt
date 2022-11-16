package com.example.project

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.Year
import java.util.*
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("test")
    val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]"
    val pattern = Pattern.compile(pwPattern)
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.joinButton.setOnClickListener {

            val userEmail = binding.joinEmail.text.toString()
            val password = binding.joinPasswd2.text.toString()
            val name = binding.joinName2.text.toString()
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val matcher = pattern.matcher(password)

            if(TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)) {
                Toast.makeText(this, "이메일, 이름, 비밀번호는 필수 입니다.", Toast.LENGTH_SHORT).show()
            }
            else if(password.length < 8 || password.length > 12) {
                Toast.makeText(this, "비밀번호는 8~12자로 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else if(!matcher.find()) {
                Toast.makeText(this, "영어와 숫자를 사용하여 비밀번호를 작성해주세요", Toast.LENGTH_SHORT).show()
            }
            else { //잘 받아와지는지 확인용 print문
                doSignUp(userEmail, password, name, year, month, day)
                println("====year : $year, month : $month, day : $day=====")
            }
        }
    }

    private fun doSignUp(userEmail: String, password: String, name:String, year:Int
    , month : Int , day :Int) {
        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    var uid = auth.currentUser?.uid
                    val itemMap = hashMapOf(
                        "uid" to uid,
                        "email" to userEmail,
                        "name" to name,
                        "passwd" to password,
                        "year" to year,
                        "month" to month+1,
                        "day" to day
                    )

                    itemsCollectionRef.document(uid!!).set(itemMap)
                        .addOnSuccessListener{ }.addOnFailureListener { }

                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()
                } else {
                    Log.w("LoginActivity", "signInWithEmail", it.exception)
                    Toast.makeText(this, "비밀번호나 아이디를 다시 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
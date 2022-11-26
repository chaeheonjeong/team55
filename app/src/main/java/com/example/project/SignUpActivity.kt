package com.example.project

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
    private val itemsCollectionRef = db.collection("users")
    private val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]"
    private val pattern: Pattern = Pattern.compile(pwPattern)
    private lateinit var auth: FirebaseAuth
    private var Useryear:Int = 0
    private var Usermonth:Int = 0
    private var Userday:Int = 0
    private lateinit var cal:Calendar

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.setDate.setOnClickListener {
            cal = Calendar.getInstance()
            var dateString:String
            //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
                binding.resultDate.text = "입력날짜 : $dateString"
                Useryear = year
                Usermonth = month
                Userday = dayOfMonth
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show() }


        binding.joinButton.setOnClickListener {

            val userEmail = binding.joinEmail.text.toString()
            val password = binding.joinPasswd2.text.toString()
            val name = binding.joinName2.text.toString()

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
                doSignUp(userEmail, password, name)
            }
        }
    }

    private fun doSignUp(userEmail: String, password: String, name:String) {
        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    var uid = auth.currentUser?.uid
                    val itemMap = hashMapOf(
                        "uid" to uid,
                        "email" to userEmail,
                        "name" to name,
                        "passwd" to password,
                        "year" to Useryear,
                        "month" to Usermonth+1,
                        "day" to Userday,
                        "introduce" to "자기 소개",
                        "profile_image" to "https://firebasestorage.googleapis.com/v0/b/team55-65c21.appspot.com/o/upload_images%2Fpro2.bmp?alt=media&token=eb9c3b4f-0e34-4af8-bac8-2cbafc1160b4"
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
package com.example.project

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivitySignupBinding

class SignUp: AppCompatActivity() {
    val binding by lazy{ActivitySignupBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.joinButton.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder
                .setMessage("이미 생성된 계정입니다")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, id ->
                    binding.joinEmail.setSelection(0)
        })
            builder.create()
            builder.show()
        }
    }

}


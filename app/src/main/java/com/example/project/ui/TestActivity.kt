package com.example.project.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project.R
import com.example.project.databinding.ActivityTestBinding
import com.example.project.ui.post.PostFragment

class TestActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFragmentA.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, PostFragment(), null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }
    }
}
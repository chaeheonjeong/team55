package com.example.project.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project.MS.ProfileFragment
import com.example.project.R
import com.example.project.databinding.ActivityTestBinding
import com.example.project.ui.timeline.TimelineFragment

class TestActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFragmentA.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, ProfileFragment(), null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }
    }
}
package com.example.project.cogjs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityMainBinding
import com.example.project.databinding.ActivityTestcoBinding

class CogjsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestcoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
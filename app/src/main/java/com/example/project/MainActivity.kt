package com.example.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.project.databinding.ActivityMainBinding
import com.example.project.HomeFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG_HOME = "home_fragment"
private const val TAG_SEARCH = "search_fragment"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(TAG_HOME, HomeFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> setFragment(TAG_HOME, HomeFragment())
                R.id.searchFragment-> setFragment(TAG_SEARCH, SearchFragment())
            }
            true
        }
        binding.testbutton.setOnClickListener {
            startActivity(
                Intent(this, SearchActivity::class.java)
            )

        }
        //테스트용 mainactivity입니다.
        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

    }
    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val search = manager.findFragmentByTag(TAG_SEARCH)

        if (home != null){
            fragTransaction.hide(home)
        }
        if (search != null) {
            fragTransaction.hide(search)
        }
        else if (tag == TAG_HOME) {
            if (home != null) {
                fragTransaction.show(home)
            }
        }
        else if (tag == TAG_SEARCH) {
            if (search != null) {
                fragTransaction.show(search)
            }
        }
        fragTransaction.addToBackStack(tag).commitAllowingStateLoss()
    }

}
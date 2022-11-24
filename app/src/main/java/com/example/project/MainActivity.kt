package com.example.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.add
import com.example.project.databinding.ActivityMainBinding
import com.example.project.HomeFragment
import com.example.project.MS.ProfileFragment
import com.example.project.cogjs.FriendsList
import com.example.project.ui.post.adding.AddingPostFragment
import com.example.project.ui.timeline.TimelineFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG_HOME = "home_fragment"
private const val TAG_SEARCH = "search_fragment"
private const val TAG_FRIEND = "FriendsList"
private const val TAG_PROFILE = "Profile"
private const val TAG_ADD = "add_post"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(TAG_HOME, TimelineFragment())
        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

        binding.navigationView.setOnItemSelectedListener { item->
            when(item.itemId) {
                R.id.timeLine -> {
                    setFragment(TAG_HOME, TimelineFragment())
                }
                R.id.Search -> {
                    setFragment(TAG_SEARCH, SearchFragment())
                }
                R.id.Friend -> {
                    setFragment(TAG_FRIEND, FriendsList())
                }
                R.id.Profile -> {
                    setFragment(TAG_PROFILE, ProfileFragment())
                }
                R.id.AddPost -> {
                    setFragment(TAG_ADD, AddingPostFragment())
                }
            }
            true
        }
    }
    override fun onBackPressed() {
        //super.onBackPressed()
    }
    private fun setFragment(tag: String, fragment: Fragment) {
        var manager:FragmentManager= supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        if(manager.findFragmentByTag(tag) == null){
            ft.add(R.id.mainFrameLayout, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val search = manager.findFragmentByTag(TAG_SEARCH)
        val friend = manager.findFragmentByTag(TAG_FRIEND)
        val profile = manager.findFragmentByTag(TAG_PROFILE)
        val add = manager.findFragmentByTag(TAG_ADD)

        //모든 프래그먼트 hide
        if(home!=null){
            ft.hide(home)
        }
        if(search!=null){
            ft.hide(search)
        }
        if(friend!=null){
            ft.hide(friend)
        }
        if(profile!=null){
            ft.hide(profile)
        }
        if(add!=null) {
            ft.hide(add)
        }

        //선택한 항목에 따라 그에 맞는 프래그먼트만 show
        if(tag == TAG_HOME){
            if(home!=null){
                ft.show(home)
            }
        }
        else if(tag == TAG_SEARCH){
            if(search!=null){
                ft.show(search)
            }
        }
        else if(tag == TAG_FRIEND){
            if(friend!=null){
                ft.show(friend)
            }
        }
        else if(tag == TAG_PROFILE){
            if(profile!=null){
                ft.show(profile)
            }
        }
        else if(tag == TAG_ADD){
            if(add!=null){
                ft.show(add)
            }
        }
        //마무리
        ft.commitAllowingStateLoss()

    }
    private fun updateBottomMenu(navigation: BottomNavigationView) {
        val tag1: Fragment? = supportFragmentManager.findFragmentByTag(TAG_HOME)
        val tag2: Fragment? = supportFragmentManager.findFragmentByTag(TAG_SEARCH)
        val tag3: Fragment? = supportFragmentManager.findFragmentByTag(TAG_PROFILE)
        val tag4: Fragment? = supportFragmentManager.findFragmentByTag(TAG_FRIEND)
        val tag5: Fragment? = supportFragmentManager.findFragmentByTag(TAG_ADD)

        if(tag1 != null && tag1.isVisible) {navigation.menu.findItem(R.id.timeLine).isChecked = true }
        if(tag2 != null && tag2.isVisible) {navigation.menu.findItem(R.id.Search).isChecked = true }
        if(tag3 != null && tag3.isVisible) {navigation.menu.findItem(R.id.Profile).isChecked = true }
        if(tag4 != null && tag4.isVisible) {navigation.menu.findItem(R.id.Friend).isChecked = true }
        if(tag5 != null && tag5.isVisible) {navigation.menu.findItem(R.id.AddPost).isChecked = true }

    }
}

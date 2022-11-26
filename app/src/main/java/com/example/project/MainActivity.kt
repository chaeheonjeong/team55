package com.example.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

        setFragment(TAG_HOME,TimelineFragment())

        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }
        binding.navigationView.setOnItemSelectedListener { item->
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            when(item.itemId){
                R.id.timeLine ->{
                    val fragmentA = TimelineFragment()
                    transaction.replace(R.id.mainFrameLayout,fragmentA, TAG_HOME)
                }
                R.id.Search -> {
                    val fragmentB = SearchFragment()
                    transaction.replace(R.id.mainFrameLayout,fragmentB, TAG_SEARCH)
                }
                R.id.Profile -> {
                    val fragmentC = ProfileFragment()
                    transaction.replace(R.id.mainFrameLayout,fragmentC, TAG_PROFILE)
                }
                R.id.Friend -> {
                    val fragmentD = FriendsList()
                    transaction.replace(R.id.mainFrameLayout,fragmentD, TAG_FRIEND)
                }
                R.id.AddPost -> {
                    val fragmentE = AddingPostFragment()
                    transaction.replace(R.id.mainFrameLayout,fragmentE, TAG_ADD)

                }
            }
            transaction.addToBackStack(null)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()

            true
        }
    }
    fun onNavigationItemSelected(p0 : MenuItem) : Boolean{
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        when(p0.itemId){
            R.id.timeLine ->{
                val fragmentA = TimelineFragment()
                transaction.replace(R.id.mainFrameLayout,fragmentA, TAG_HOME)
            }
            R.id.Search -> {
                val fragmentB = SearchFragment()
                transaction.replace(R.id.frame_layout,fragmentB, TAG_SEARCH)
            }
            R.id.Profile -> {
                val fragmentC = FriendsList()
                transaction.replace(R.id.frame_layout,fragmentC, TAG_PROFILE)
            }
            R.id.Friend -> {
                val fragmentD = ProfileFragment()
                transaction.replace(R.id.frame_layout,fragmentD, TAG_FRIEND)
            }
            R.id.AddPost -> {
                val fragmentE = AddingPostFragment()
                transaction.replace(R.id.frame_layout,fragmentE, TAG_ADD)

            }
        }
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
        return true
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val bnv = findViewById<View>(R.id.navigationView) as BottomNavigationView
        updateBottomMenu(bnv)

    }
    private fun setFragment(tag: String, fragment: Fragment) {
        var manager:FragmentManager= supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        if(manager.findFragmentByTag(tag) == null){
            ft.add(R.id.mainFrameLayout, fragment, tag)
        }

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

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setFragment(TAG_HOME, HomeFragment())
        /*binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.timeLine -> setFragment(TAG_HOME, HomeFragment())
                R.id.Search-> setFragment(TAG_SEARCH, SearchFragment())
            }
            true
        }
         */

        //테스트용 mainactivity입니다.
        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

        binding.navigationView.setOnItemSelectedListener { item->
            when(item.itemId) {
                R.id.timeLine -> setFragment(TAG_HOME, HomeFragment())
                R.id.Search -> setFragment(TAG_SEARCH, SearchFragment())
            }
            true
            }
        }
    private fun setFragment(tag: String, fragment: Fragment) {
        var manager:FragmentManager= supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        //트랜잭션에 tag로 전달된 fragment가 없을 경우 add
        if(manager.findFragmentByTag(tag) == null){
            ft.add(R.id.mainFrameLayout, fragment, tag)
        }

        //작업이 수월하도록 manager에 add되어있는 fragment들을 변수로 할당해둠
        val category = manager.findFragmentByTag(TAG_HOME)
        val home = manager.findFragmentByTag(TAG_SEARCH)
        //val myPage = manager.findFragmentByTag(TAG_MY_PAGE)

        //모든 프래그먼트 hide
        if(category!=null){
            ft.hide(category)
        }
        if(home!=null){
            ft.hide(home)
        }

        //선택한 항목에 따라 그에 맞는 프래그먼트만 show
        if(tag == TAG_HOME){
            if(category!=null){
                ft.show(category)
            }
        }
        else if(tag == TAG_SEARCH){
            if(home!=null){
                ft.show(home)
            }
        }
        //마무리
        ft.commitAllowingStateLoss()
        //ft.commit()
    }//seFragment함수 끝
    }
    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navi_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        when(item.itemId) {
            R.id.timeLine -> HomeFragment().show()
            R.id.Search -> transaction.replace(R.id.mainFrameLayout, SearchFragment())
            R.id.Profile -> startActivity(
                Intent(this, SearchActivity::class.java)
            )
        }
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
        return true
    }

     */



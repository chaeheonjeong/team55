package com.example.project

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.FragmentSearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SearchFragment : Fragment() {
    private var firestore : FirebaseFirestore? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("test")
    private var adapter: SearchAdapter? = null
    private var auth: FirebaseAuth = Firebase.auth
    private var isSame:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        val recyclerview = binding.FragrecyclerView

        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(emptyList())
        recyclerview.adapter = adapter
        firestore = FirebaseFirestore.getInstance()

        //updateList()

        binding.search.setOnClickListener {
            queryWhere(binding)
        }

        return binding.root
    }

    private fun queryWhere(binding: FragmentSearchBinding) {
        val p = binding.searchWord.text.toString()
        println(isSame)
        itemsCollectionRef.get().addOnSuccessListener {
            val uid = auth.currentUser?.uid
            for (doc in it) {
                if(p == doc["name"].toString()) {
                    println("=====${doc["name"].toString()}====")
                    println("=====${p}=====")
                    isSame = true
                    println(isSame)
                }
            }
        }
        if(!isSame) {
            Toast.makeText(this.activity, "정확하게 다시 검색해주세요(자기자신 검색X).", Toast.LENGTH_SHORT).show()
        }
        else {
            println(isSame)
            itemsCollectionRef.whereEqualTo("name", p).get()
                .addOnSuccessListener {
                    val items = arrayListOf<String>()
                    val results = mutableListOf<MyItem>()
                    for (doc in it) {
                        items.add("${doc["name"]} - ${doc["email"]}")
                        results.add(MyItem(doc))
                    }
                    adapter?.updateList(results)
                    //binding.result.text = items.toString()

                }
                .addOnFailureListener {
                }
        }
    }
    private fun updateList() {
        itemsCollectionRef.get().addOnSuccessListener {
            val items = mutableListOf<MyItem>()
            for (doc in it) {
                items.add(MyItem(doc))
            }
            adapter?.updateList(items)
        }
    }
    companion object {

    }
}
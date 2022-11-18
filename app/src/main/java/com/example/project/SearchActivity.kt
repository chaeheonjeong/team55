package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.databinding.ActivitySearchBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var adapter: MyAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("test")
    private var snapshotListener: ListenerRegistration? = null
    private var auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(this, emptyList())
        adapter?.setOnItemClickListener {
            queryItem(it)
        }
        binding.recyclerViewItems.adapter = adapter

        updateList()  // list items on recyclerview

        binding.buttonQuery.setOnClickListener {
            queryWhere()
        }
        binding.TestButton.setOnClickListener {

            /*itemsCollectionRef.whereEqualTo("name", p).get()
                .addOnSuccessListener {
                    binding.progressWait.visibility = View.GONE
                    val items = arrayListOf<String>()
                    for (doc in it) {
                        items.add("${doc["name"]} - ${doc["email"]}")
                    }
                    AlertDialog.Builder(this)
                        .setTitle("검색하신 이름의 이름과 아이디")
                        .setItems(items.toTypedArray()) { dialog, which -> }.show()
                    binding.showText.text = items.toString()
                }
                .addOnFailureListener {
                    binding.progressWait.visibility = View.GONE
                }

             */
            itemsCollectionRef.get().addOnSuccessListener {
                val uid = auth.currentUser?.uid
                val items = arrayListOf<String>()
                for (doc in it) {
                    if(uid == doc["uid"].toString())
                        items.add(doc["name"].toString())
                }
                println("=====${uid}")
                println("=====${items}")
            }
        }
    }

    override fun onStart() {
        super.onStart()

        itemsCollectionRef.document("1").addSnapshotListener { snapshot, error ->
            Log.d(TAG, "${snapshot?.id} ${snapshot?.data}")
        }
    }

    override fun onStop() {
        super.onStop()
        snapshotListener?.remove()
    }

    private fun updateList() {
        itemsCollectionRef.get().addOnSuccessListener {
            val items = mutableListOf<Item>()
            for (doc in it) {
                items.add(Item(doc))
            }
            adapter?.updateList(items)
        }
    }

    private fun queryItem(itemID: String) {
        itemsCollectionRef.document(itemID).get()
            .addOnSuccessListener {
                //binding.editID.setText(it.id)
                //binding.checkAutoID.isChecked = false
                //binding.editID.isEnabled = true
                //binding.editItemName.setText(it["name"].toString())
                //binding.editPrice.setText(it["price"].toString())
            }.addOnFailureListener {
            }
    }

    private fun queryWhere() {
        val p = binding.editItemName.text.toString()
        //binding.progressWait.visibility = View.VISIBLE
        itemsCollectionRef.whereEqualTo("name", p).get()
            .addOnSuccessListener {
                binding.progressWait.visibility = View.GONE
                val items = arrayListOf<String>()
                for (doc in it) {
                    items.add("${doc["name"]} - ${doc["email"]}")
                }
                AlertDialog.Builder(this)
                  .setTitle("검색하신 이름의 이름과 아이디")
                    .setItems(items.toTypedArray()) { dialog, which -> }.show()
                binding.showText.text = items.toString()
            }
            .addOnFailureListener {
                binding.progressWait.visibility = View.GONE
            }
    }


    companion object {
        const val TAG = "SearchActivity"
    }
}
package com.example.project

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.FragmentSearchBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SearchFragment : Fragment() {
    private var searchOption = "name"
    private var firestore : FirebaseFirestore? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("users")
    private lateinit var buttonSearch: Button
    private lateinit var buttonAdd:Button
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        val spinner = binding.spinner
        var text = spinner.selectedItem.toString()

        binding.search.setOnClickListener {
            var text = spinner.selectedItem.toString()
            if(text == "이름") {
                val username = editText.text
            }
        }
        return binding.root
    }

    companion object {

    }
}
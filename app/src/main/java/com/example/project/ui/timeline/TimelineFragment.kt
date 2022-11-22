package com.example.project.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project.databinding.FragmentTimelineBinding

class TimelineFragment: Fragment() {
    private lateinit var binding: FragmentTimelineBinding
    private lateinit var viewModel: TimelineViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = TimelineViewModel()
        val timelineAdapter = TimelineAdapter(this)
        binding.rvTimeline.adapter = timelineAdapter

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            timelineAdapter.submitList(posts)
        }
    }
}
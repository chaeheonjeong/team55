package com.example.project.ui.timeline

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView.LayoutManager
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
        //val timelineAdapter = TimelineAdapter(this)
        //binding.rvTimeline.adapter = timelineAdapter

        /* 나중에 그냥 설정해주자. post 많이 늘어나면 이게 효과가 있을 수도 있으니까.
        binding.rvTimeline.setHasFixedSize(true)
        binding.rvTimeline.setItemViewCacheSize(30)
        binding.rvTimeline.layoutManager?.isItemPrefetchEnabled = false
        binding.rvTimeline.itemAnimator = null
        */

        //val adapter = TimelineAdapter(this)
        //binding.rvTimeline.adapter = adapter
        val adapter2 = TimelineAdapter2(this)
        binding.rvTimeline.adapter = adapter2

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            //adapter.submitList(posts)
            adapter2.differ.submitList(posts)
        }
    }
}
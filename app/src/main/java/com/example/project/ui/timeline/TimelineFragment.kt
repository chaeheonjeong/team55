package com.example.project.ui.timeline

import android.os.Bundle
import android.util.Log
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

        // 나중에 그냥 설정해주자. post 많이 늘어나면 이게 효과가 있을 수도 있으니까.
        binding.rvTimeline.setItemViewCacheSize(30)
        binding.rvTimeline.itemAnimator = null

        val adapter2 = TimelineAdapter2(this)
        binding.rvTimeline.adapter = adapter2

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            //adapter.submitList(posts)
            adapter2.differ.submitList(posts)
        }

    }
}
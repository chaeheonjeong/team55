package com.example.project.ui.timeline

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.project.databinding.FragmentTimelineBinding
import com.example.project.model.Post
import com.example.project.utils.Constants.Companion.PAGE_NUM

class TimelineFragment: Fragment() {
    private lateinit var binding: FragmentTimelineBinding
    private lateinit var viewModel: TimelineViewModel
    private var page = 1
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
            // adapter2.differ.submitList(posts)
            adapter2.setList(posts as MutableList<Post>)

            // 한 페이지당 게시물이 10개씩 들어있음.
            // 새로운 게시물이 추가되었다는 것을 알려줌 (추가된 부분만 새로고침)
            adapter2.notifyItemRangeInserted((page - 1) * PAGE_NUM, PAGE_NUM)
        }

        setUpSwipeRefresh()
    }

    // 스와이프 이벤트 생성 메서드
    private fun setUpSwipeRefresh() {
        //새로고침 리사이클러뷰의 어댑터를 통해 불러온 List와 원래 refreshItemList이 다른 주소를 가지고 있었음.
        binding.layoutSwipe.setOnRefreshListener {
            // 이미 addSnapshotListener 해놔서 굳이 필요없는 기능이긴 함. (혹시 비교해서 안되면 여기 만들어주기)
            Toast.makeText(requireContext(), "갱신되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setScrolled(adapter: TimelineAdapter2) {
        binding.rvTimeline.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 화면에 보이는 마지막 아이템의 position
                val lastVisibleItemPosition =
                    (binding.rvTimeline.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                // 어댑터에 등록된 아이템의 총 개수 -1
                val itemTotalCount = binding.rvTimeline.adapter!!.itemCount - 1

                // 스크롤이 끝에 도달했는지 확인 (1은 화면의 위치가 최하단, -1은 최상단)
                if (binding.rvTimeline.canScrollVertically(1).not() && lastVisibleItemPosition == itemTotalCount) {
                    adapter.deleteLoading()
                    // viewModel에서 데이터 필요한 만큼 데베에서 가져오도록 지시

                }

                // if()
            }
        })
    }
}
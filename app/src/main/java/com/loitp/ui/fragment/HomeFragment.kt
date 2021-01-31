package com.loitp.ui.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.loitp.R
import com.loitp.adapter.OpenCageDataResultAdapter
import com.loitp.ui.activity.MainActivity
import com.loitp.ui.activity.SearchActivity
import com.loitp.viewmodels.MainViewModel
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_home.*

@LogTag("HomeFragment")
class HomeFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    private var mainViewModel: MainViewModel? = null
    private val concatAdapter = ConcatAdapter()
    private var openCageDataResultAdapter: OpenCageDataResultAdapter? = null
    private var previousTimeSearch = SystemClock.elapsedRealtime()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()

        loadData()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    private fun setupViews() {
        openCageDataResultAdapter = OpenCageDataResultAdapter { dummyItems, layoutItemRssTransformation ->
//            context?.let { c ->
//                val now = SystemClock.elapsedRealtime()
//                if (now - previousTimeSearch >= layoutItemRssTransformation.duration) {
//                    DetailActivity.startActivity(context = c, transformationLayout = layoutItemRssTransformation, dummyItem = dummyItems)
//                    previousTimeSearch = now
//                }
//            }
        }
        openCageDataResultAdapter?.let {
            concatAdapter.addAdapter(it)
        }

        val gridLayoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = concatAdapter
        swRefresh.setOnRefreshListener(this)

        btSearch.setSafeOnClickListener {
            context?.let { c ->
                val now = SystemClock.elapsedRealtime()
                if (now - previousTimeSearch >= layoutItemSearchTransformation.duration) {
                    SearchActivity.startActivity(context = c, transformationLayout = layoutItemSearchTransformation, keySearch = btSearch.text.toString())
                    previousTimeSearch = now
                }
            }
        }
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
//            mvm.listDummyItemLiveData.observe(viewLifecycleOwner, Observer { listDummyItem ->
//                logD("<<<listRssItemLiveData " + BaseApplication.gson.toJson(listRssItem))

//                resultAdapter?.setItems(listDummyItem)
//                if (recyclerView.visibility != View.VISIBLE) {
//                    recyclerView.visibility = View.VISIBLE
//                }
//                tvNoData.visibility = View.GONE
//                animationView.visibility = View.GONE
//            })
        }

    }

    private fun loadData() {
        mainViewModel?.loadData()
    }

    private fun showLoading() {
        swRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        swRefresh.isRefreshing = false
    }

    override fun onRefresh() {
        if (activity is MainActivity) {
            (activity as MainActivity).requestLocation()
            hideLoading()
        }
    }

    fun updateCurrentLocation(keySearch: String) {
        btSearch.text = keySearch
        mainViewModel?.setCurrentLocation(location = keySearch)
        //TODO call api
    }
}

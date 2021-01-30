package com.loitp.ui.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LSharedPrefsUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.adapter.LoadMoreAdapter
import com.loitp.adapter.RssItemsAdapter
import com.loitp.constant.Cons
import com.loitp.ui.activity.ReadNewsActivity
import com.loitp.viewmodels.MainViewModel
import com.rss.RssItem
import kotlinx.android.synthetic.main.frm_home.*

@LogTag("HomeFragment")
class HomeFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    private val listFeed = ArrayList<String>()
    private var mainViewModel: MainViewModel? = null
    private val concatAdapter = ConcatAdapter()
    private var rssItemsAdapter: RssItemsAdapter? = null
    private var loadMoreAdapter = LoadMoreAdapter()
    private var currentIndex = 0
    private var previousTime = SystemClock.elapsedRealtime()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setupViews()
        setupViewModels()

        fetchRss()
    }

    private fun setupData() {
        listFeed.add("https://vnexpress.net/rss/tin-moi-nhat.rss")
        listFeed.add("https://vnexpress.net/rss/tin-noi-bat.rss")
        listFeed.add("https://vnexpress.net/rss/giao-duc.rss")
        listFeed.add("https://vnexpress.net/rss/phap-luat.rss")
        listFeed.add("https://vnexpress.net/rss/the-thao.rss")
        listFeed.add("https://vnexpress.net/rss/giai-tri.rss")
        listFeed.add("https://vnexpress.net/rss/startup.rss")
        listFeed.add("https://vnexpress.net/rss/kinh-doanh.rss")
        listFeed.add("https://vnexpress.net/rss/thoi-su.rss")
        listFeed.add("https://vnexpress.net/rss/the-gioi.rss")
        listFeed.add("https://vnexpress.net/rss/tin-xem-nhieu.rss")
        listFeed.add("https://vnexpress.net/rss/cuoi.rss")
        listFeed.add("https://vnexpress.net/rss/tam-su.rss")
        listFeed.add("https://vnexpress.net/rss/y-kien.rss")
        listFeed.add("https://vnexpress.net/rss/oto-xe-may.rss")
        listFeed.add("https://vnexpress.net/rss/so-hoa.rss")
        listFeed.add("https://vnexpress.net/rss/khoa-hoc.rss")
        listFeed.add("https://vnexpress.net/rss/du-lich.rss")
        listFeed.add("https://vnexpress.net/rss/gia-dinh.rss")
        listFeed.add("https://vnexpress.net/rss/suc-khoe.rss")
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    private fun setupViews() {
        rssItemsAdapter = RssItemsAdapter { rssItem, layoutItemRssTransformation ->
            context?.let { c ->
                val now = SystemClock.elapsedRealtime()
                if (now - previousTime >= layoutItemRssTransformation.duration) {
                    ReadNewsActivity.startActivity(context = c, transformationLayout = layoutItemRssTransformation, rssItem = rssItem)
                    previousTime = now
                }
            }
        }
        rssItemsAdapter?.let {
            concatAdapter.addAdapter(it)
        }

        val isGridView = LSharedPrefsUtil.instance.getBoolean(Cons.IS_GRID_VIEW, false)
        val spanCount = if (isGridView) {
            2
        } else {
            1
        }
        val gridLayoutManager = GridLayoutManager(context, spanCount)
        if (isGridView) {
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == concatAdapter.itemCount - 1) {
                        if (isContainLoadMoreAdapter()) {
                            2
                        } else {
                            1
                        }
                    } else {
                        1
                    }
                }
            }
        }
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = concatAdapter
        LUIUtil.setScrollChange(
                recyclerView = recyclerView,
                onTop = {
                },
                onBottom = {
                    logD("onBottom")
                    val isContainLoadMoreAdapter = isContainLoadMoreAdapter()
                    logD("isContainLoadMoreAdapter $isContainLoadMoreAdapter")
                    if (!isContainLoadMoreAdapter) {
                        concatAdapter.addAdapter(loadMoreAdapter)
                    }
                    concatAdapter.itemCount.let {
                        recyclerView.scrollToPosition(it - 1)
                    }

                    if (currentIndex < (listFeed.size - 1)) {
                        logD("if")
                        currentIndex++
                        fetchRss()
                    } else {
                        logD("else")
                        concatAdapter.removeAdapter(loadMoreAdapter)
                        showSnackBarInfor("Bạn đã cuộn đến trang cuối")
                    }
                },
                onScrolled = {
                }
        )
        swRefresh.setOnRefreshListener(this)
    }

    private fun isContainLoadMoreAdapter(): Boolean {
        var isContainLoadMoreAdapter = false
        concatAdapter.adapters.forEach {
            logD(">>> " + it.javaClass.simpleName + "~" + LoadMoreAdapter::class.java.simpleName)
            if (it.javaClass.simpleName == LoadMoreAdapter::class.java.simpleName) {
                isContainLoadMoreAdapter = true
            }
        }
        return isContainLoadMoreAdapter
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            mvm.eventLoading.observe(viewLifecycleOwner, Observer { isLoading ->
                logD("eventLoading isLoading $isLoading")
                if (isLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }
            })
            mvm.eventErrorMessage.observe(viewLifecycleOwner, Observer { msg ->
                logE("eventErrorMessage observe $msg")
                msg?.let {
                    recyclerView.visibility = View.GONE
                    tvNoData.visibility = View.VISIBLE
                }

            })
            mvm.listRssItemLiveData.observe(viewLifecycleOwner, Observer { listRssItem ->
//                logD("<<<listRssItemLiveData " + BaseApplication.gson.toJson(listRssItem))
                onRssItemsLoaded(rssItems = listRssItem)
            })
        }

    }

    private fun fetchRss() {
        val feedUrl = listFeed[currentIndex]
        logD("fetchRss feedUrl $feedUrl, currentIndex: $currentIndex")
        mainViewModel?.loadDataRss(feedUrl)
    }

    private fun onRssItemsLoaded(rssItems: List<RssItem>) {
        concatAdapter.removeAdapter(loadMoreAdapter)
        rssItemsAdapter?.setItems(rssItems)
        if (recyclerView.visibility != View.VISIBLE) {
            recyclerView.visibility = View.VISIBLE
        }
        tvNoData.visibility = View.GONE
        animationView.visibility = View.GONE
    }

    private fun showLoading() {
        swRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        swRefresh.isRefreshing = false
    }

    override fun onRefresh() {
        currentIndex = 0
        fetchRss()
    }
}

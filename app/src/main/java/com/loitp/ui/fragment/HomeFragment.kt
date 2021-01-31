package com.loitp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFragment
import com.loitp.R
import com.loitp.adapter.OpenCageDataResultAdapter
import com.loitp.model.opencagedata.Result
import com.loitp.ui.activity.MainActivity
import com.loitp.ui.activity.SearchActivity
import com.loitp.viewmodels.MainViewModel
import com.skydoves.transformationlayout.TransformationCompat
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_home.*

@LogTag("HomeFragment")
class HomeFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    companion object {
        const val REQUEST_CODE = 1234
    }

    private var mainViewModel: MainViewModel? = null
    private val concatAdapter = ConcatAdapter()
    private var openCageDataResultAdapter: OpenCageDataResultAdapter? = null
    private var previousTimeSearch = SystemClock.elapsedRealtime()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    private fun setupViews() {
        openCageDataResultAdapter = OpenCageDataResultAdapter { dummyItems ->
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

                    val intent = Intent(context, SearchActivity::class.java)
                    intent.putExtra(SearchActivity.KEY_SEARCH, btSearch.text.toString())
                    TransformationCompat.startActivityForResult(layoutItemSearchTransformation, intent, REQUEST_CODE)

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

    fun updateCurrentLocation(keySearch: String, lat: Double?, lon: Double?) {
        logD("updateCurrentLocation keySearch $keySearch, lat $lat, lon $lon")
        btSearch.text = keySearch
//        mainViewModel?.setCurrentLocation(location = keySearch)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                val result = data?.getSerializableExtra(SearchActivity.KEY_RESULT)
                logD("onActivityResult result " + BaseApplication.gson.toJson(result))
                if (result == null) {
                    //do nothing
                } else {
                    if (result is Result) {
                        logD(">>>onActivityResult " + result.formatted)
                        updateCurrentLocation(keySearch = result.formatted, lat = result.geometry?.lat, lon = result.geometry?.lng)
                    }
                }
            }
        }
    }
}

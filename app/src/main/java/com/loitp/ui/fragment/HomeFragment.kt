package com.loitp.ui.fragment

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
import com.core.utilities.LConnectivityUtil
import com.loitp.BuildConfig
import com.loitp.R
import com.loitp.adapter.OpenCageDataResultAdapter
import com.loitp.service.OpenWeatherService
import com.loitp.ui.activity.MainActivity
import com.loitp.ui.activity.SearchActivity
import com.loitp.viewmodels.MainViewModel
import com.restapi.restclient.RestClient2
import com.skydoves.transformationlayout.TransformationCompat
import com.views.setSafeOnClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.frm_home.*

@LogTag("loitppHomeFragment")
class HomeFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    companion object {
        const val REQUEST_CODE = 1234
    }

    private var openWeatherService: OpenWeatherService? = null
    private var mainViewModel: MainViewModel? = null
    private val concatAdapter = ConcatAdapter()
    private var openCageDataResultAdapter: OpenCageDataResultAdapter? = null
    private var previousTimeSearch = SystemClock.elapsedRealtime()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()
    }

    override fun onResume() {
        super.onResume()
        logD("onResume")

        RestClient2.init(BuildConfig.BASE_URL_OPEN_WEATHER)
        openWeatherService = RestClient2.createService(OpenWeatherService::class.java)
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
            if (!LConnectivityUtil.isConnected()) {
                showSnackBarError(msg = getString(R.string.check_ur_connection))
                return@setSafeOnClickListener
            }
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
        mainViewModel?.setCurrentLocation(location = keySearch)
        logD(">>>>>>>>>>>>>>>>>>>>>>>>>updateCurrentLocation")

        openWeatherService?.let { sv ->
            compositeDisposable.clear()
            animationView.visibility = View.VISIBLE
            showLoading()
            compositeDisposable.add(
                    sv.getWeather(
                            lat = lat ?: 0.0,
                            lon = lon ?: 0.0,
                            exclude = "hourly,current,minutely,alerts",
                            appid = BuildConfig.APP_ID_OPEN_WEATHER
                    )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ openWeather ->
                                logD("<<< success " + BaseApplication.gson.toJson(openWeather))
                                val listDaily = openWeather.daily
                                logD("listDaily ${listDaily.size} -> " + BaseApplication.gson.toJson(listDaily))


                                animationView.visibility = View.GONE
                                hideLoading()
                            }, {
                                logE("<<< error $it")
                                animationView.visibility = View.GONE
                                hideLoading()
                                val msg = if (LConnectivityUtil.isConnected()) {
                                    getString(R.string.no_data_eng)
                                } else {
                                    getString(R.string.check_ur_connection)
                                }
                                showDialogError(errMsg = msg, runnable = Runnable {
                                    //do nothing
                                })
                            }))
        }
    }
}

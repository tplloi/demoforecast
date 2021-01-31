package com.loitp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFragment
import com.loitp.BuildConfig
import com.loitp.R
import com.loitp.adapter.OpenCageDataResultAdapter
import com.loitp.service.OpenCageDataService
import com.loitp.viewmodels.MainViewModel
import com.restapi.restclient.RestClient2
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.frm_online_search.*

@LogTag("loitppOnlineSearchFragment")
class OnlineSearchFragment : BaseFragment() {

    private var mainViewModel: MainViewModel? = null
    private var openCageDataService: OpenCageDataService? = null
    private val concatAdapter = ConcatAdapter()
    private var openCageDataResultAdapter: OpenCageDataResultAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RestClient2.init(BuildConfig.BASE_URL_OPEN_CAGE_DATA)
        openCageDataService = RestClient2.createService(OpenCageDataService::class.java)
        setupViews()
        setupViewModels()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_online_search
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
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            mvm.keySearchLiveData.observe(viewLifecycleOwner, Observer { keySearch ->
                logD("keySearchLiveData observe keySearch $keySearch")
                getCageData(keySearch = keySearch)
            })
        }

    }

    private fun getCageData(keySearch: String) {
        logD("getCageData keySearch $keySearch")
        openCageDataService?.let { sv ->
            indicatorView.smoothToShow()
            compositeDisposable.clear()
            compositeDisposable.add(
                    sv.getGeoCode(
                            pretty = 1,
                            noAnnotations = 1,
                            noDedUpe = 1,
                            noRecord = 1,
                            limit = 1,
                            key = BuildConfig.KEY_OPEN_CAGE_DATA,
                            q = keySearch
                    )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ openCageData ->
                                logD("loadData success " + BaseApplication.gson.toJson(openCageData))
                                openCageDataResultAdapter?.setItems(openCageData.results)
                                indicatorView.smoothToHide()
                            }, {
                                logE("loadData error $it")
                                indicatorView.smoothToHide()
                                showDialogError(errMsg = getString(R.string.err_unknow_en), runnable = Runnable {
                                    //do nothing
                                })
                            }))
        }
    }

}

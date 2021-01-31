package com.loitp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFragment
import com.loitp.BuildConfig
import com.loitp.R
import com.loitp.service.OpenCageDataService
import com.loitp.viewmodels.MainViewModel
import com.restapi.restclient.RestClient2
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@LogTag("loitppOnlineSearchFragment")
class OnlineSearchFragment : BaseFragment() {

    private var mainViewModel: MainViewModel? = null
    private var openCageDataService: OpenCageDataService? = null

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
                            .subscribe({
                                logD("loadData success " + BaseApplication.gson.toJson(it))
                            }, {
                                logE("loadData error $it")
                            }))
        }
    }

}

package com.loitp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFragment
import com.loitp.R
import com.loitp.model.RetroCrypto
import com.loitp.service.SampleService
import com.loitp.viewmodels.MainViewModel
import com.restapi.restclient.RestClient2
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@LogTag("loitppOnlineSearchFragment")
class OnlineSearchFragment : BaseFragment() {

    private var mainViewModel: MainViewModel? = null
    private var retroCryptoArrayList = ArrayList<RetroCrypto>()
    private val BASE_URL = "https://api.nomics.com/v1/"
    private lateinit var sampleService: SampleService
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RestClient2.init(BASE_URL)
        sampleService = RestClient2.createService(SampleService::class.java)
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

                a()
            })

        }

    }

    private fun a() {
        logD("aaaaaaaaaaaaaaaaaaaaa")
        compositeDisposable.add(sampleService.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    logD("loadData success " + BaseApplication.gson.toJson(it))
                }, {
                    logE("loadData error $it")
                }))
    }

}

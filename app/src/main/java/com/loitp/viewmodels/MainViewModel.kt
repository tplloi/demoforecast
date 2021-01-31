package com.loitp.viewmodels

import androidx.lifecycle.MutableLiveData
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseViewModel
import com.core.helper.mup.girl.model.GirlPage
import com.core.helper.ttt.db.TTTDatabase
import com.loitp.db.AppDatabase
import com.loitp.model.opencagedata.Result
import com.service.livedata.ActionData
import com.service.livedata.ActionLiveData
import kotlinx.coroutines.launch

@LogTag("loitppMainViewModel")
class MainViewModel(

) : BaseViewModel() {

    val currentLocationLiveData: MutableLiveData<String> = MutableLiveData()

    //    val listDummyItemLiveData: MutableLiveData<List<DummyItem>> = MutableLiveData()
    val keySearchLiveData: MutableLiveData<String> = MutableLiveData()
    val keySearchChangeLiveData: MutableLiveData<String> = MutableLiveData()

    val pageActionLiveData: ActionLiveData<ActionData<ArrayList<GirlPage>>> = ActionLiveData()

    fun setCurrentLocation(location: String) {
        currentLocationLiveData.postValue(location)
    }

    fun setKeySearchLiveData(keySearch: String) {
        keySearchLiveData.postValue(keySearch)
    }

    fun setKeySearchChangeLiveData(keySearch: String) {
        keySearchChangeLiveData.postValue(keySearch)
    }

    fun loadData() {
        ioScope.launch {
            logD(">>>loadDataRss urlRss")
//            showLoading(true)
//
//            delay(1000)
//
//            val retrofit = Retrofit.Builder()
//                    .baseUrl("https://github.com")
//                    .addConverterFactory(RssConverterFactory.create())
//                    .build()
//
//            val service = retrofit.create(RssService::class.java)
//            service.getRss(url)
//                    .enqueue(object : Callback<RssFeed> {
//                        override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {
//                            val listRssItem = response.body()?.items ?: emptyList()
//                            listDummyItemLiveData.postValue(listRssItem)
//                            showLoading(false)
//                        }
//
//                        override fun onFailure(call: Call<RssFeed>, t: Throwable) {
//                            setErrorMessage("Failed to fetchRss RSS feed!")
//                            showLoading(false)
//                        }
//                    })
        }
    }

    fun saveOpenCageData(result: Result) {
        logD("saveOpenCageData result " + result.formatted)
        ioScope.launch {
            val id = AppDatabase.instance?.openCageDataDao()?.insert(t = result)
            logD("save id $id")

            getListOpenCageData("")
        }
    }

    fun getListOpenCageData(formatted: String) {
        logD("getListOpenCageData formatted $formatted")
        ioScope.launch {
//            AppDatabase.instance?.openCageDataDao()?.getListResult(formatted = formatted)
            val list = AppDatabase.instance?.openCageDataDao()?.getListResult()
            logD("getListOpenCageData list " + BaseApplication.gson.toJson(list))
        }
    }
}

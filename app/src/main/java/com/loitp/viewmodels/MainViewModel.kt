package com.loitp.viewmodels

import androidx.lifecycle.MutableLiveData
import com.annotation.LogTag
import com.core.base.BaseViewModel
import com.loitp.model.DummyItem
import com.loitp.service.RssService
import com.rss.RssConverterFactory
import com.rss.RssFeed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

@LogTag("MainViewModel")
class MainViewModel : BaseViewModel() {

    val listDummyItemLiveData: MutableLiveData<List<DummyItem>> = MutableLiveData()

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
}

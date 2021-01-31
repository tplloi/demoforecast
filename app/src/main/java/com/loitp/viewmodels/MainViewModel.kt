package com.loitp.viewmodels

import androidx.lifecycle.MutableLiveData
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseViewModel
import com.loitp.db.AppDatabase
import com.loitp.model.opencagedata.Result
import kotlinx.coroutines.launch

@LogTag("loitppMainViewModel")
class MainViewModel(

) : BaseViewModel() {

    val currentLocationLiveData: MutableLiveData<String> = MutableLiveData()

    val keySearchLiveData: MutableLiveData<String> = MutableLiveData()
    val keySearchChangeLiveData: MutableLiveData<String> = MutableLiveData()

    val offlineListOpenCageDataLiveData: MutableLiveData<List<Result>> = MutableLiveData()

    fun setCurrentLocation(location: String) {
        currentLocationLiveData.set(location)
    }

    fun setKeySearchLiveData(keySearch: String) {
        keySearchLiveData.postValue(keySearch)
    }

    fun setKeySearchChangeLiveData(keySearch: String) {
        keySearchChangeLiveData.postValue(keySearch)
    }

    fun saveOpenCageData(result: Result) {
        logD("saveOpenCageData result " + result.formatted)
        ioScope.launch {
            val id = AppDatabase.instance?.openCageDataDao()?.insert(t = result)
            logD("save id $id")
        }
    }

    fun getListOpenCageData(formatted: String) {
        logD("getListOpenCageData formatted $formatted")
        ioScope.launch {
            val list = AppDatabase.instance?.openCageDataDao()?.getListResult(formatted = formatted)
//            val list = AppDatabase.instance?.openCageDataDao()?.getListResult()
            logD(">>>>>>>>>>>>>getListOpenCageData list ${list?.size}" + BaseApplication.gson.toJson(list))
            try {
                offlineListOpenCageDataLiveData.postValue(list?.subList(0, 5))
            } catch (e: Exception) {
                offlineListOpenCageDataLiveData.postValue(list)
            }
        }
    }
}

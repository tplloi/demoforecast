package com.loitp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.loitp.R
import com.loitp.adapter.OfflineResultAdapter
import com.loitp.adapter.OpenCageDataResultAdapter
import com.loitp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.frm_offline_key.*

@LogTag("loitppSearchKeyFragment")
class OfflineKeyFragment : BaseFragment() {

    private var mainViewModel: MainViewModel? = null
    private val concatAdapter = ConcatAdapter()
    private var offlineResultAdapter: OfflineResultAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()

    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_offline_key
    }

    private fun setupViews() {
        offlineResultAdapter = OfflineResultAdapter { result ->
            logD("offlineResultAdapter result " + result.formatted + " - " + result.geometry?.lat + " - " + result.geometry?.lng)
            //TODO
        }
        offlineResultAdapter?.let {
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
            })
            mvm.keySearchChangeLiveData.observe(viewLifecycleOwner, Observer { keySearch ->
                logD("keySearchChangeLiveData observe keySearch $keySearch")
                mvm.getListOpenCageData(keySearch)
            })
            mvm.offlineListOpenCageDataLiveData.observe(viewLifecycleOwner, Observer { listResult ->
                offlineResultAdapter?.setItems(items = listResult)
            })
        }

    }
}

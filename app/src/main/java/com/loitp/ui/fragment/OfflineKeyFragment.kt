package com.loitp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.loitp.R
import com.loitp.viewmodels.MainViewModel

@LogTag("loitppSearchKeyFragment")
class OfflineKeyFragment : BaseFragment() {

    private var mainViewModel: MainViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()

    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_offline_key
    }

    private fun setupViews() {
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            mvm.keySearchLiveData.observe(viewLifecycleOwner, Observer { keySearch ->
                logD("keySearchLiveData observe keySearch $keySearch")
            })

        }

    }
}

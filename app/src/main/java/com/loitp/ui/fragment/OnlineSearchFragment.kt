package com.loitp.ui.fragment

import android.os.Bundle
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.loitp.R

@LogTag("SearchKeyFragment")
class OnlineSearchFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_online_search
    }

    private fun setupViews() {
    }

}

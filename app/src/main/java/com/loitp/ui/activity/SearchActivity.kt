package com.loitp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.annotation.IsFullScreen
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LScreenUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.ui.fragment.OfflineKeyFragment
import com.loitp.ui.fragment.OnlineSearchFragment
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_layout_search.*

@LogTag("DetailActivity")
@IsFullScreen(false)
class SearchActivity : BaseFontActivity() {

    companion object {
        private const val KEY_SEARCH = "KEY_SEARCH"

        fun startActivity(
                context: Context,
                transformationLayout: TransformationLayout,
                keySearch: String
        ) {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra(KEY_SEARCH, keySearch)
            TransformationCompat.startActivity(transformationLayout, intent)
        }
    }

    private var prevKeySearch: String = ""
    private val offlineKeyFragment = OfflineKeyFragment()
    private val onlineSearchFragment = OnlineSearchFragment()

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_layout_search
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainer(intent.getParcelableExtra(Constants.activityTransitionName))
        super.onCreate(savedInstanceState)

        prevKeySearch = intent?.getStringExtra(KEY_SEARCH) ?: ""
        logD("onCreate keySearch $prevKeySearch")

        setupViews()
    }

    private fun setupViews() {
        LScreenUtil.replaceFragment(
                activity = this,
                containerFrameLayoutIdRes = R.id.layoutContainerOfflineKey,
                fragment = offlineKeyFragment,
                isAddToBackStack = false
        )
        LScreenUtil.replaceFragment(
                activity = this,
                containerFrameLayoutIdRes = R.id.layoutContainerOnlineSearch,
                fragment = onlineSearchFragment,
                isAddToBackStack = false
        )
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    btClearText.visibility = View.GONE
                } else {
                    btClearText.visibility = View.VISIBLE
                }
            }

        })
        etSearch.setText(prevKeySearch)
        LUIUtil.setImeiActionSearch(editText = etSearch, actionSearch = Runnable {
            search()
        })
        btClearText.setSafeOnClickListener {
            etSearch.setText("")
        }
        btSearch.setSafeOnClickListener {
            search()
        }
    }

    private fun search() {
        val keyword = etSearch.text.toString()
        if (keyword.isEmpty()) {
            showShortInformation(getString(R.string.pls_input_your_location))
            return
        }
    }


}

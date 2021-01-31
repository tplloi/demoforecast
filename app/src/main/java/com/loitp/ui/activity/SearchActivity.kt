package com.loitp.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.annotation.IsFullScreen
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LAppResource
import com.loitp.R
import com.loitp.model.DummyItem
import com.rss.RssItem
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
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

    private var keySearch: String = ""

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_layout_search
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainer(intent.getParcelableExtra(Constants.activityTransitionName))
        super.onCreate(savedInstanceState)

        keySearch = intent?.getStringExtra(KEY_SEARCH) ?: ""
        logD("onCreate keySearch $keySearch")

        setupViews()
    }

    private fun setupViews() {

    }
}

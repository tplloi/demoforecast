package com.loitp.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.annotation.IsFullScreen
import com.annotation.IsShowAdWhenExit
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LAppResource
import com.loitp.R
import com.rss.RssItem
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import kotlinx.android.synthetic.main.activity_layout_detail.*

@LogTag("DetailActivity")
@IsFullScreen(false)
@IsShowAdWhenExit(true)
class DetailActivity : BaseFontActivity() {

    companion object {
        private const val KEY_DATA = "KEY_DATA"

        fun startActivity(
                context: Context,
                transformationLayout: TransformationLayout,
                rssItem: RssItem
        ) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(KEY_DATA, rssItem)
            TransformationCompat.startActivity(transformationLayout, intent)
        }
    }

//    private var rssItem: RssItem? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_layout_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainer(intent.getParcelableExtra(Constants.activityTransitionName))
        super.onCreate(savedInstanceState)

//        rssItem = intent?.getSerializableExtra(KEY_DATA) as RssItem?
//        logD("onCreate rssItem " + BaseApplication.gson.toJson(rssItem))

        setupViews()
    }

    private fun setupViews() {
//        rssItem?.let { item ->
//            LImageUtil.load(context = this, any = item.image, imageView = ivBkg)
//            collapsingToolbarLayout.title = item.title
//        }

        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)

        toolbar.navigationIcon = LAppResource.getDrawable(R.drawable.ic_keyboard_backspace_white_48dp)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}

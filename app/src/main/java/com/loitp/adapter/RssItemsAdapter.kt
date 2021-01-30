package com.loitp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.core.utilities.LImageUtil
import com.core.utilities.LScreenUtil
import com.core.utilities.LSharedPrefsUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.constant.Cons
import com.rss.RssItem
import com.skydoves.transformationlayout.TransformationLayout
import kotlinx.android.synthetic.main.row_rss_item.view.*
import java.util.*

@LogTag("RssItemsAdapter")
class RssItemsAdapter(
        private val onClick: ((RssItem, TransformationLayout) -> Unit)? = null
) : BaseAdapter() {

    private val itemList = ArrayList<RssItem>()
    private val isSmallThumb = LSharedPrefsUtil.instance.getBoolean(Cons.IS_SMALL_THUMB, false)
    private var height = 0

    init {
        height = if (isSmallThumb) {
            LScreenUtil.screenHeight / 5
        } else {
            LScreenUtil.screenHeight / 2
        }
    }

    fun setItems(items: List<RssItem>) {

        fun isContain(rssItem: RssItem): Boolean {
            itemList.forEach {
                if (it.link == rssItem.link) {
                    return true
                }
            }
            return false
        }

        items.forEach {
            val isContain = isContain(it)
//            logD("setItems isContain $isContain -> ${it.title}")
            if (!isContain) {
                itemList.add(it)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RSSViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_rss_item, parent, false)
        return RSSViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RSSViewHolder) {
            holder.bind(itemList[position])
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class RSSViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(rssItem: RssItem) {
            LImageUtil.load(context = itemView.ivThumb.context, any = rssItem.image, imageView = itemView.ivThumb)
            LUIUtil.setSizeOfView(view = itemView.ivThumb, height = height)

            itemView.tvTitle.text = rssItem.title
            itemView.tvPubDate.text = rssItem.publishDate
            LUIUtil.setTextFromHTML(textView = itemView.tvDes, bodyData = rssItem.description ?: "")

            itemView.setOnClickListener {
                onClick?.invoke(rssItem, itemView.layoutItemRssTransformation)
            }
        }
    }
}

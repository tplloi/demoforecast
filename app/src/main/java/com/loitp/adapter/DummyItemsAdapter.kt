package com.loitp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.loitp.R
import com.loitp.model.DummyItem
import com.skydoves.transformationlayout.TransformationLayout
import kotlinx.android.synthetic.main.view_row_dummy_item.view.*
import java.util.*

@LogTag("DummyItemsAdapter")
class DummyItemsAdapter(
    private val onClick: ((DummyItem, TransformationLayout) -> Unit)? = null
) : BaseAdapter() {

    private val itemList = ArrayList<DummyItem>()

    fun setItems(items: List<DummyItem>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.view_row_dummy_item, parent, false)
        return DummyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DummyViewHolder) {
            holder.bind(itemList[position])
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class DummyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(dummyItem: DummyItem) {

            itemView.tvTitle.text = dummyItem.title

            itemView.setOnClickListener {
                onClick?.invoke(dummyItem, itemView.layoutItemRssTransformation)
            }
        }
    }
}

package com.loitp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.loitp.R

@LogTag("LoadMoreAdapter")
class LoadMoreAdapter(
) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_loading, parent, false)
        return LoadingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LoadingViewHolder) {
            //do nothing
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {

        }
    }
}

package com.loitp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.loitp.R
import com.loitp.model.opencagedata.Result
import kotlinx.android.synthetic.main.view_row_offline_result.view.*
import java.util.*

@LogTag("OfflineResultAdapter")
class OfflineResultAdapter(
        private val onClick: ((Result) -> Unit)? = null
) : BaseAdapter() {

    private val listResult = ArrayList<Result>()

    fun setItems(items: List<Result>) {
        listResult.clear()
        listResult.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.view_row_offline_result, parent, false)
        return ResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ResultViewHolder) {
            holder.bind(listResult[position])
        }
    }

    override fun getItemCount(): Int {
        return listResult.size
    }

    inner class ResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(result: Result) {
            itemView.tvTitle.text = result.formatted
            itemView.setOnClickListener {
                onClick?.invoke(result)
            }
        }
    }
}

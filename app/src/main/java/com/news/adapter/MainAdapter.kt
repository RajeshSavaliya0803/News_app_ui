package com.news.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.news.R
import com.news.model.News
import com.squareup.picasso.Picasso

class MainAdapter(
    private val context: Context,
    var listener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<News> = ArrayList()

    interface OnItemClickListener {
        fun category(cateName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_small_list, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val bean = list[position]

        if (holder is ItemViewHolder) {
            holder.tvNews.text = bean.headLine
            holder.tvCName.text = bean.cName
            holder.tvTime.text = bean.hour
            Picasso.get().load(bean.img).into(holder.ivImg)
            Picasso.get().load(bean.cLogo).into(holder.ivCLogo)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(ruler: List<News>) {
        list = ruler
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),

        View.OnClickListener {
        val tvNews: TextView = itemView.findViewById(R.id.tv_main)
        val tvCName: TextView = itemView.findViewById(R.id.tv_ch_name)
        val ivImg: ImageView = itemView.findViewById(R.id.iv_main)
        val ivCLogo: ImageView = itemView.findViewById(R.id.iv_ch_logo)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.category(tvNews.text.toString())
        }
    }

}
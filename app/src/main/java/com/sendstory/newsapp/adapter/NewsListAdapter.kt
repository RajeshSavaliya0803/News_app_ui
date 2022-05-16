package com.sendstory.newsapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sendstory.newsapp.Constants
import com.sendstory.newsapp.databinding.ItemNewsHeaderBinding
import com.sendstory.newsapp.databinding.ItemSmallListBinding
import com.sendstory.newsapp.model.NewsItem
import com.squareup.picasso.Picasso

class NewsListAdapter(private var list: ArrayList<NewsItem>, val onClick: (item: NewsItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewTypeSmall(private var binding: ItemSmallListBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindItem(currentItem: NewsItem) {
            val seconds = (currentItem.pubtimestamp!!.toInt() / 1000) % 60
            val minutes = (currentItem.pubtimestamp.toInt() / (1000 * 60) % 60)
            val hours = (currentItem.pubtimestamp.toInt() / (1000 * 60 * 60) % 24)

            binding.tvMain.text = currentItem.headline
            binding.tvChName.text = currentItem.publisher!!.pubname
            binding.tvTime.text = hours.toString() + "h"
            Picasso.get().load(currentItem.imageurl).into(binding.ivMain)
            Picasso.get().load(currentItem.publisher.favicon).into(binding.ivChLogo)

            binding.root.setOnClickListener {
                onClick(currentItem)
            }
        }

    }

    inner class ViewTypeHeader(private var binding: ItemNewsHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItem(currentItem: NewsItem) {
            val seconds = (currentItem.pubtimestamp!!.toInt() / 1000) % 60
            val minutes = (currentItem.pubtimestamp.toInt() / (1000 * 60) % 60)
            val hours = (currentItem.pubtimestamp.toInt() / (1000 * 60 * 60) % 24)

            binding.tvMain.text = currentItem.headline
            binding.tvChName.text = currentItem.publisher!!.pubname
            binding.tvTime.text = hours.toString() + "h"
            Picasso.get().load(currentItem.imageurl).into(binding.ivMain)
            Picasso.get().load(currentItem.publisher.favicon).into(binding.ivChLogo)
            binding.root.setOnClickListener {
                onClick(currentItem)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewOne = ItemNewsHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewTwo = ItemSmallListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == Constants.VIEW_TYPE_ONE) {
            ViewTypeHeader(viewOne)
        } else {
            ViewTypeSmall(viewTwo)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = list[position]
        if (list[position].featured == Constants.VIEW_TYPE_ONE) {
            (holder as ViewTypeHeader).bindItem(currentItem)
        } else {
            (holder as ViewTypeSmall).bindItem(currentItem)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return list[position].featured!!
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(ruler: ArrayList<NewsItem>) {
        list = ruler
        notifyDataSetChanged()
    }
}
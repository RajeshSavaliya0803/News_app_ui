package com.sendstory.newsapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sendstory.newsapp.Constants
import com.sendstory.newsapp.R
import com.sendstory.newsapp.comman.getAge
import com.sendstory.newsapp.data.NewsItem
import com.sendstory.newsapp.databinding.ItemNewsHeaderBinding
import com.sendstory.newsapp.databinding.ItemSmallListBinding

class NewsPagingAdapter(val context: Context, val onClick: (item: NewsItem) -> Unit, val onShared : (item: NewsItem) -> Unit) :
    PagingDataAdapter<NewsItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewTypeSmall(private var binding: ItemSmallListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindItem(currentItem: NewsItem) {
            binding.tvMain.text = currentItem.headline
            binding.tvChName.text = currentItem.publisher!!.pubname
            binding.tvTime.text = context.getAge(currentItem.pubtimestamp!!.toLong())

            Glide.with(context).load(currentItem.imageurl)
                .placeholder(R.drawable.ic_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(binding.ivMain)
            if (currentItem.publisher.favicon!!.isEmpty()) {
                Glide.with(context).load(R.drawable.ic_placeholder).into(binding.ivChLogo)
            } else {
                Glide.with(context).load(currentItem.publisher.favicon).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(binding.ivChLogo)
            }

            binding.ivShare.setOnClickListener {
                onShared(currentItem)
            }

            binding.root.setOnClickListener {
                onClick(currentItem)
            }
        }

    }

    inner class ViewTypeHeader(private var binding: ItemNewsHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItem(currentItem: NewsItem) {
            binding.tvMain.text = currentItem.headline
            binding.tvChName.text = currentItem.publisher!!.pubname
            binding.tvTime.text = context.getAge(currentItem.pubtimestamp!!.toLong())
            Glide.with(context).load(currentItem.imageurl).diskCacheStrategy(
                DiskCacheStrategy.AUTOMATIC
            ).into(binding.ivMain)
            if (currentItem.publisher.favicon!!.isEmpty()) {
                Glide.with(context).load(R.drawable.ic_placeholder).into(binding.ivChLogo)
            } else {
                Glide.with(context).load(currentItem.publisher.favicon).diskCacheStrategy(
                    DiskCacheStrategy.AUTOMATIC
                ).into(binding.ivChLogo)
            }
            binding.ivShare.setOnClickListener {
                onShared(currentItem)
            }
            binding.root.setOnClickListener {
                onClick(currentItem)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem?.featured == Constants.VIEW_TYPE_ONE) {
            (holder as NewsPagingAdapter.ViewTypeHeader).bindItem(currentItem)
        } else {
            (holder as NewsPagingAdapter.ViewTypeSmall).bindItem(currentItem!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewOne =
            ItemNewsHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewTwo =
            ItemSmallListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == Constants.VIEW_TYPE_ONE) {
            ViewTypeHeader(viewOne)
        } else {
            ViewTypeSmall(viewTwo)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.featured!!
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(
                oldItem: NewsItem,
                newItem: NewsItem
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: NewsItem,
                newItem: NewsItem
            ) = oldItem == newItem
        }
    }
}
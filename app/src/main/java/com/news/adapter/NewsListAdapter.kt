package com.news.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.news.Constants
import com.news.databinding.ItemNewsHeaderBinding
import com.news.databinding.ItemSmallListBinding
import com.news.model.News
import com.squareup.picasso.Picasso

class NewsListAdapter(private var list: ArrayList<News>, val onClick: (item: News) -> Unit ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewTypeSmall(private var binding: ItemSmallListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(currentItem: News) {
            binding.tvMain.text = currentItem.headLine
            binding.tvChName.text = currentItem.cName
            binding.tvTime.text = currentItem.hour
            Picasso.get().load(currentItem.img).into(binding.ivMain)
            Picasso.get().load(currentItem.cLogo).into(binding.ivChLogo)

            binding.root.setOnClickListener {
                onClick(currentItem)
            }
        }

    }


    inner class ViewTypeHeader(private var binding: ItemNewsHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(currentItem: News) {
            binding.tvMain.text = currentItem.headLine
            binding.tvChName.text = currentItem.cName
            binding.tvTime.text = currentItem.hour
            Picasso.get().load(currentItem.img).into(binding.ivMain)
            Picasso.get().load(currentItem.cLogo).into(binding.ivChLogo)
            binding.root.setOnClickListener {
                onClick(currentItem)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewOne = ItemNewsHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val viewTwo = ItemSmallListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return if(viewType == Constants.VIEW_TYPE_ONE){
            ViewTypeHeader(viewOne)
        }else{
            ViewTypeSmall(viewTwo)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = list[position]
        if (list[position].type == Constants.VIEW_TYPE_ONE) {
            (holder as ViewTypeHeader).bindItem(currentItem)
        } else {
            (holder as ViewTypeSmall).bindItem(currentItem)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(ruler: ArrayList<News>) {
        list = ruler
        notifyDataSetChanged()
    }
}
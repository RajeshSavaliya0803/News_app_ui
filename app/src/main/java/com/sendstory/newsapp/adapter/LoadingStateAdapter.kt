package com.sendstory.newsapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sendstory.newsapp.databinding.ItemPagerStateBinding

class LoadingStateAdapter(private val retry : ()-> Unit) : LoadStateAdapter<LoadingStateAdapter.StateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) : StateViewHolder{
        return StateViewHolder(ItemPagerStateBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: StateViewHolder, loadState: LoadState){
        holder.bind(loadState)
    }
    inner class StateViewHolder(
        private val binding: ItemPagerStateBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                spinKitLoader.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible =
                    !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
                errorMsg.text = (loadState as? LoadState.Error)?.error?.message
            }
        }
    }
}
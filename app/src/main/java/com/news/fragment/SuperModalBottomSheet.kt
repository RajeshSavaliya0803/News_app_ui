package com.news.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.news.databinding.ModalBottomSheetContentBinding

class SuperModalBottomSheet : SuperBottomSheetFragment(){

    companion object {
        const val TAG = "SuperModal"
    }

    lateinit var binding: ModalBottomSheetContentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = ModalBottomSheetContentBinding.inflate(inflater,container,false)

        return  binding.root
    }

    override fun getExpandedHeight(): Int {

        val displayMetrics = requireActivity().resources.displayMetrics

        val height = displayMetrics.heightPixels

        val maxHeight = (height * 0.80).toInt()
        val offsetHeight = (height * 0.20).toInt()

        return maxHeight
    }
}
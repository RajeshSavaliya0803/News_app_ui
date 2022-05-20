package com.sendstory.newsapp.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sendstory.newsapp.Constants
import com.sendstory.newsapp.R
import com.sendstory.newsapp.data.News
import com.sendstory.newsapp.databinding.ShareBottomSheetContentBinding

class ShareBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    lateinit var binding: ShareBottomSheetContentBinding
    lateinit var dialog: BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ShareBottomSheetContentBinding.inflate(inflater, container, false)

        val data = arguments?.getSerializable(Constants.news) as News
        showNewsDialog(data)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) ?: return@setOnShowListener

            bottomSheet.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext, android.R.color.transparent))
            bottomSheet.background = ContextCompat.getDrawable(requireActivity().applicationContext, R.drawable.rounded_dialog)

            bottomSheet.apply {
                val maxDesiredHeight = (resources.displayMetrics.heightPixels * 0.80).toInt()

                if (this.height > maxDesiredHeight) {
                    val bottomSheetLayoutParams = this.layoutParams
                    bottomSheetLayoutParams.height = maxDesiredHeight
                    this.layoutParams = bottomSheetLayoutParams
                }
                BottomSheetBehavior.from(this).apply {
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                    this.skipCollapsed = true
                }
            }

        }
        return dialog
    }

    fun showNewsDialog(news: News?) {

    }


}
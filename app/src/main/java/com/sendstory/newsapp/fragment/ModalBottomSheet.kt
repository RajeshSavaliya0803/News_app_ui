package com.sendstory.newsapp.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
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
import com.sendstory.newsapp.databinding.ModalBottomSheetContentBinding
import com.sendstory.newsapp.detail.DetailActivity
import com.sendstory.newsapp.model.News
import com.squareup.picasso.Picasso


class ModalBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    lateinit var binding: ModalBottomSheetContentBinding
    lateinit var dialog: BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ModalBottomSheetContentBinding.inflate(inflater, container, false)

        val data = arguments?.getSerializable(Constants.news) as News
        showNewsDialog(data)

        binding.tvReadFullHistory.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(Constants.newsLink, data.sourceurl)
            startActivity(intent)
        }

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
                val maxDesiredHeight =
                    (resources.displayMetrics.heightPixels * 0.80).toInt()
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

    @SuppressLint("SetTextI18n")
    fun showNewsDialog(news: News?) {
        val hours = (news!!.pubtimestamp!!.toInt() / (1000 * 60 * 60) % 24)

        Picasso.get().load(news.imageurl).into(binding.ivMain)
        binding.tvMain.text = news.headline
        binding.tvChName.text = news.publisher!!.pubname
        binding.tvTime.text = hours.toString() + "h"
        binding.tvSummaryO.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(news.formattedSummary, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(news.formattedSummary)
        }
        Picasso.get().load(news.publisher.favicon).into(binding.ivChLogo)
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

}
package com.sendstory.newsapp.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
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
import com.sendstory.newsapp.comman.getAge
import com.sendstory.newsapp.data.News
import com.sendstory.newsapp.databinding.ModalBottomSheetContentBinding
import com.sendstory.newsapp.detail.DetailActivity
import com.squareup.picasso.Picasso


class ModalBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    lateinit var binding: ModalBottomSheetContentBinding
    lateinit var dialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                    ?: return@setOnShowListener

            bottomSheet.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity().applicationContext,
                    android.R.color.transparent
                )
            )
            bottomSheet.background = ContextCompat.getDrawable(
                requireActivity().applicationContext,
                R.drawable.rounded_dialog
            )

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
        binding.tvTime.text = requireContext().getAge(news.pubtimestamp!!.toLong())

        val formatted =
            "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=no\" /><style type=\"text/css\">body{font-family: '-apple-system','HelveticaNeue';} img{max-width:100%%; padding:0px; margin:0px;} div.newscontent h1{margin-top:18px; margin-bottom:18px; line-height:30px; font-size:25px; color:black;} div.newscontent h2{margin-top:15px; margin-bottom:15px; line-height:24px; font-size:16px; color:black;} div.newscontent h3{margin-top:25px; margin-bottom:25px; line-height:32px; font-size:17px; color:black;} div.newscontent blockquote{margin:15px; border-left:3px solid #eeeeee; color:#666666; font-size:15px; padding-left:25px;} div.newscontent{font-size:16px; line-height:23px; background-color:white;} div.newscontent a{color:#1761F9;} div.newscontent a:hover{color:#666666;} div.newscontent figcaption{color:#888888; font-size:13px; text-align:center;} iframe{background-color:#f9f9f9;} ul{margin:0px; list-style-type: square; padding-left:16px;} li{color:#1761F9; margin-left:0px; padding:0px; padding-left:10px; margin-bottom:8px;} li span{color:black;}</style></head><body style=\"margin:0px;\"><div align=\"center\"><div style=\"max-width:680px; margin:15px;\" align=\"left\" class=\"newscontent\"><h2>Summary:</h2>" + news.formattedSummary + "</div></div></body></html>"
//        binding.tvSummaryO.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Html.fromHtml(htmlString,
//                Html.FROM_HTML_MODE_COMPACT
//            )
//        } else {
//            Html.fromHtml(htmlString)
//        }
        binding.tvSummaryO.loadDataWithBaseURL("", formatted, "text/html", "UTF-8", "")
        Picasso.get().load(news.publisher.favicon).into(binding.ivChLogo)
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

}
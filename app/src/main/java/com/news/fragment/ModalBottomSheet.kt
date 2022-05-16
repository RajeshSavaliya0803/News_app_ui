package com.news.fragment

import android.app.Activity
import android.app.Dialog
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
import com.news.R
import com.news.databinding.ModalBottomSheetContentBinding


class ModalBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    lateinit var binding: ModalBottomSheetContentBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ModalBottomSheetContentBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

//        dialog.setOnShowListener { dialogInterface ->
//            val layout: FrameLayout? = (dialogInterface as BottomSheetDialog).
//            findViewById(com.google.android.material.R.id.design_bottom_sheet)
//
//
//            layout?.let {
//                val behavior = BottomSheetBehavior.from(it)
////                behavior.isFitToContents = false
////                behavior.expandedOffset = 300
////                behavior.peekHeight = 200
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                behavior.skipCollapsed = true
//
//
//
//                val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
//
//                    override fun onStateChanged(bottomSheet: View, newState: Int) {
//                        // Do something for new state.
//                        Log.e(ModalBottomSheet.TAG, "onStateChanged: $newState ")
//                    }
//
//                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                        // Do something for slide offset.
//                    }
//                }
//
//                behavior.addBottomSheetCallback(bottomSheetCallback)
//            }
////            val bottomSheetDialog = dialogInterface as BottomSheetDialog
////            setupFullHeight(bottomSheetDialog)
//        }

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                    ?: return@setOnShowListener

            bottomSheet.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext, android.R.color.transparent))
            bottomSheet.background = ContextCompat.getDrawable(requireActivity().applicationContext,
                R.drawable.rounded_dialog)

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

//            val displayMetrics = requireActivity().resources.displayMetrics
//
//            val height = displayMetrics.heightPixels
//
//            val maxHeight = (height * 0.80).toInt()
//            val offsetHeight = (height * 0.20).toInt()



//            bottomSheet.let {
//                val behavior = BottomSheetBehavior.from(it)
////                behavior.isFitToContents = false
////                behavior.expandedOffset = offsetHeight
////                behavior.peekHeight = maxHeight
////                behavior.peekHeight = maxHeight
////                behavior.expandedOffset = offsetHeight
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                behavior.skipCollapsed = true
//            }
        }
        return dialog
    }


//    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
//        val bottomSheet =
//            bottomSheetDialog.findViewById<View>(R.id.bottomSheet) as CoordinatorLayout?
//        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
//        val layoutParams = bottomSheet.layoutParams
//        val windowHeight: Int = getWindowHeight()
//        if (layoutParams != null) {
//            layoutParams.height = windowHeight
//        }
//        bottomSheet.layoutParams = layoutParams
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

}
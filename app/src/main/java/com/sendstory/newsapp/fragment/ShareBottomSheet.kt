package com.sendstory.newsapp.fragment

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sendstory.newsapp.Constants
import com.sendstory.newsapp.R
import com.sendstory.newsapp.data.NewsItem
import com.sendstory.newsapp.databinding.ShareBottomSheetContentBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream


class ShareBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ShareBottomSheet"
    }

    lateinit var binding: ShareBottomSheetContentBinding
    lateinit var dialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ShareBottomSheetContentBinding.inflate(inflater, container, false)

        val data = arguments?.getSerializable(Constants.news) as NewsItem
        Log.e(TAG, "onCreateView: data => $data")
        showShareDialog(data)

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
//            bottomSheet.background = ContextCompat.getDrawable(requireActivity().applicationContext, R.drawable.rounded_dialog)

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

    private fun showShareDialog(data: NewsItem) {
        Picasso.get().load(data.imageurl).into(binding.ivMain)
        binding.tvDes.text = data.headline
        binding.tvPub.text = data.publisher!!.pubname
//        binding.tvTime.text = requireContext().getAge(news.pubtimestamp!!.toLong())

        if (data.publisher.favicon!!.isEmpty()) {
            Picasso.get().load(R.drawable.ic_placeholder).into(binding.ivChLogo)
        } else {
            Picasso.get().load(data.publisher.favicon).into(binding.ivChLogo)
        }

        binding.ivInsta.setOnClickListener {
            //TODO: uncomment after successful open instagram
            val bitmap = getScreenShotFromView(binding.shareImageView)
            if (bitmap != null) {
                val uri = getImageUri(requireContext(), bitmap)
                if (uri != null) {
                    shareFileToInstagram(uri)
                }

            }
//            shareFileToInstagram(Uri.parse("content://media/external/images/media/287"))
        }
        binding.ivLink.setOnClickListener {
            val clipboard: ClipboardManager? =
                requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("","${data.sourceurl}")
            clipboard?.setPrimaryClip(clip)
            if(clipboard?.primaryClip != null){
                Toast.makeText(requireContext(),"Link copied successfully",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getScreenShotFromView(v: View): Bitmap? {
        // create a bitmap object
        var screenshot: Bitmap? = null
        try {
            // inflate screenshot object
            // with Bitmap.createBitmap it
            // requires three parameters
            // width and height of the view and
            // the background color
            screenshot =
                Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            // Now draw this bitmap on a canvas
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        return screenshot
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Share",
            "Sharing image"
        )
        return Uri.parse(path)
    }

    private fun shareFileToInstagram(uri: Uri) {
//        val feedIntent = Intent(Intent.ACTION_SEND)
//        feedIntent.type = "image/*"
//        feedIntent.putExtra(Intent.EXTRA_STREAM, uri)
//        feedIntent.setPackage(Constants.INSTAGRAM_PACKAGE)
//        val storiesIntent = Intent("com.instagram.share.ADD_TO_STORY")
//        storiesIntent.setDataAndType(uri, "jpg")
//        storiesIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        storiesIntent.setPackage(Constants.INSTAGRAM_PACKAGE)
//        requireActivity().grantUriPermission(
//            "com.instagram.android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
//        )
////        val chooserIntent = Intent.createChooser(feedIntent, "Share to Instagram")
////        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(storiesIntent))
////        startActivity(chooserIntent)
        val intent = Intent("com.instagram.share.ADD_TO_STORY")

        intent.setDataAndType(uri, "image/*")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val activity: Activity = requireActivity()
        Log.e(TAG, "shareFileToInstagram: Implicit intent => $activity")
        Log.e(TAG, "shareFileToInstagram: Implicit intent => ${activity.packageManager}")
        Log.e(
            TAG,
            "shareFileToInstagram: Implicit intent => ${
                activity.packageManager.resolveActivity(
                    intent,
                    0
                )
            }"
        )
        if (activity.packageManager.resolveActivity(intent, 0) != null) {
            activity.startActivityForResult(intent, 0)
        }else{
            Toast.makeText(requireContext(),"Instagram is not installed",Toast.LENGTH_SHORT).show()
        }

    }


}
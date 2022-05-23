package com.sendstory.newsapp.fragment

import android.app.Activity
import android.app.Dialog
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sendstory.newsapp.Constants
import com.sendstory.newsapp.R
import com.sendstory.newsapp.data.NewsItem
import com.sendstory.newsapp.databinding.ShareBottomSheetContentBinding
import com.squareup.picasso.Picasso
import java.io.OutputStream
import java.util.*


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

        if (data.publisher.favicon!!.isEmpty()) {
            Picasso.get().load(R.drawable.ic_placeholder).into(binding.ivChLogo)
        } else {
            Picasso.get().load(data.publisher.favicon).into(binding.ivChLogo)
        }

        binding.ivInsta.setOnClickListener {
            val bitmap = getScreenShotFromView(binding.cardView)
            if (bitmap != null) {
                val uri = getImageUri(requireContext(), bitmap)
                if (uri != null) {
                    shareFileToInstagram(uri)
                }else{
                    Log.e(TAG, "showShareDialog: URI is null ", )
                }
            }
        }
        binding.ivLink.setOnClickListener {
            val clipboard: ClipboardManager? =
                requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("", "${data.sourceurl}")
            clipboard?.setPrimaryClip(clip)
            if (clipboard?.primaryClip != null) {
                Toast.makeText(requireContext(), "Link copied successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun getScreenShotFromView(v: View): Bitmap? {
        // create a bitmap object
        var screenshot: Bitmap? = null
        try {
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
        var returnUri: Uri ? = null
        //TODO: Working on Android 10-11 but not on Android 6.0.1
//        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "IMG_" + Calendar.getInstance().time, null)
//        MediaScannerConnection.scanFile(inContext, arrayOf(path),null
//        ) { p0, p1 ->
//            Log.i("ExternalStorage", "Scanned $p0:");
//            Log.i("ExternalStorage", "-> uri=$p1");
//        }
//        Log.e(TAG, "getImageUri: $path")
//        return Uri.parse(path)


        val fileName = "IMG_" + Calendar.getInstance().time
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val valueImage = ContentValues()
            valueImage.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/")
            valueImage.put(MediaStore.Images.Media.TITLE, fileName)
            valueImage.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            valueImage.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            valueImage.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            valueImage.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            valueImage.put(MediaStore.Images.Media.IS_PENDING, 1)
            val resolver: ContentResolver = inContext.contentResolver
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valueImage)
            val fos: OutputStream? = resolver.openOutputStream(imageUri!!)
//            val savedBitmap: Bitmap = ImageUtil.getBitmapFromView(postImageView)
//                .compress(Bitmap.CompressFormat.PNG, 100, fos)
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos?.flush()
            fos?.close()
            valueImage.clear()
            valueImage.put(MediaStore.Audio.Media.IS_PENDING, 0)
            resolver.update(imageUri, valueImage, null, null)
            returnUri = imageUri
        } else {
            val url: String = MediaStore.Images.Media.insertImage(
                requireContext().contentResolver,
                inImage,
                "IMG_" + Calendar.getInstance().time,
                null
            )
            if (url.isNotEmpty()) {
//                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url))
//                shareIntent.setType("image/*")
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                context!!.startActivity(Intent.createChooser(shareIntent, "Select the app"))
                returnUri = Uri.parse(url)
            } else {
                Toast.makeText(context, "Failed to create shareable image", Toast.LENGTH_LONG)
                    .show()
            }
        }
                Log.e(TAG, "getImageUri: $returnUri")
        return returnUri
    }

    private fun shareFileToInstagram(uri: Uri) {
        val intent = Intent("com.instagram.share.ADD_TO_STORY")

        intent.setDataAndType(uri, "image/*")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val activity: Activity = requireActivity()

        if (activity.packageManager.resolveActivity(intent, 0) != null) {
            activity.startActivityForResult(intent, 0)
        } else {
            Toast.makeText(requireContext(), "Instagram is not installed", Toast.LENGTH_SHORT)
                .show()
        }

    }

}
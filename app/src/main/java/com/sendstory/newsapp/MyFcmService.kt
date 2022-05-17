package com.sendstory.newsapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.net.URL


class MyFcmService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("TAG", "onMessageReceived: data = ${message.data}")

        if (message.data.isNotEmpty()) {
            val notificationTitle: String? = message.data["title"]
            val notificationContent: String? = message.data["message"]
            val imageUrl: String? = message.data["imageurl"]
            val newsId = message.data["newsid"]
            createNotification(notificationTitle!!, notificationContent!!, imageUrl!!, newsId!!)
        }

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(
        notificationTitle: String,
        notificationContent: String,
        imageUrl: String,
        newsId: String
    ) {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(Constants.newsId, newsId)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        // Let's create a notification builder object
        val builder =
            NotificationCompat.Builder(
                this,
                resources.getString(R.string.default_notification_channel_id)
            )
        // Create a notificationManager object
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // If android version is greater than 8.0 then create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create a notification channel
            val notificationChannel = NotificationChannel(
                resources.getString(R.string.default_notification_channel_id),
                resources.getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            // Set properties to notification channel
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300)

            // Pass the notificationChannel object to notificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        // Set the notification parameters to the notification builder object
        builder.setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setSmallIcon(R.drawable.ic_notification)
            .setSound(defaultSoundUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        // Set the image for the notification
        val bitmap: Bitmap? = getBitmapfromUrl(imageUrl)
        builder.setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .bigLargeIcon(null)
        ).setLargeIcon(bitmap)
        notificationManager.notify(1, builder.build())


//        Glide.with(this)
//            .asBitmap()
//            .load("$imageUrl?auto=compress&w=500")
//            .into(object : CustomTarget<Bitmap?>() {
//                override fun onResourceReady(
//                    resource: Bitmap,
//                    transition: Transition<in Bitmap?>?
//                ) {
//                    builder.setStyle(
//                        NotificationCompat.BigPictureStyle()
//                            .bigPicture(resource)
//                            .bigLargeIcon(null)
//                    ).setLargeIcon(resource)
//                    notificationManager.notify(1, builder.build())
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    TODO("Not yet implemented")
//                }
//            })
    }

    private fun getBitmapfromUrl(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
//            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//            connection.doInput = true
//            connection.connect()
//            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: Exception) {
            Log.e("TAG", "getBitmapfromUrl: $e ")
            Log.e("awesome", "Error in getting notification image: " + e.localizedMessage)
            null
        }
    }

}
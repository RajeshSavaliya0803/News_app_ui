package com.sendstory.newsapp.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.sendstory.newsapp.Constants.newsLink
import com.sendstory.newsapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var webView: WebView
    private lateinit var summaryText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        summaryText = intent.getStringExtra(newsLink) ?: ""
        Log.e("TAG", "onCreate: $summaryText")
        init()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init() {
        /*binding.tvSummaryText.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(summaryText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(summaryText)
        }*/

        webView = binding.webView
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        webView.loadUrl(summaryText)
    }
}
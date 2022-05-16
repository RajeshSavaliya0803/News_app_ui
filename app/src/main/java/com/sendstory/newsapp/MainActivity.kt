package com.sendstory.newsapp

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.firebase.messaging.FirebaseMessaging
import com.sendstory.newsapp.adapter.NewsListAdapter
import com.sendstory.newsapp.databinding.ActivityMainBinding
import com.sendstory.newsapp.fragment.ModalBottomSheet
import com.sendstory.newsapp.model.NewsItem
import com.sendstory.newsapp.retrofit.NewsAPI
import com.sendstory.newsapp.retrofit.NewsHelper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    lateinit var tabLayout: TabLayout

    lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsList: ArrayList<NewsItem>
    private lateinit var adapter: NewsListAdapter
    private lateinit var modalBottomSheet: ModalBottomSheet
    lateinit var country: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initList()
        initTab()
    }


    private fun init() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        tabLayout = binding.tabLayout
        recyclerView = binding.mainRecycler
        newsList = ArrayList()
        modalBottomSheet = ModalBottomSheet()
    }

    private fun initList() {
        country = resources.configuration.locale.country

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            Log.e(TAG, "initList: $token")
        })
        getNewsList(true, "", country, "1")
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getNewsList(firstTime: Boolean, category: String, country: String, page: String) {
        val progressDialog = ProgressDialog(this@MainActivity)
        if (firstTime) {
            progressDialog.setTitle("Please wait..")
            progressDialog.setMessage("Getting the latest news")
            progressDialog.show()
        }

        val quotesApi = NewsHelper.getInstance().create(NewsAPI::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val result = quotesApi.getNewsList(category, country, page)
            newsList = result.body()!!.news as ArrayList<NewsItem>
            runOnUiThread {
                progressDialog.dismiss()
                setupRecyclerView()
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = NewsListAdapter(newsList) { currentNews ->
            showModal(currentNews.id)
        }
        recyclerView.adapter = adapter
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun showModal(id: String?) {
        val quotesApi = NewsHelper.getInstance().create(NewsAPI::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val result = id?.let { quotesApi.getNewsDetails(it) }
            runOnUiThread {
                if (result!!.body()!!.statusCode == 200) {
                    val bundle = Bundle()
                    bundle.putSerializable(Constants.news, result.body()!!.news)
                    modalBottomSheet.arguments = bundle

                    modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
                }
            }
        }

        /*val mBottomSheetDialog = RoundedBottomSheetDialog(this@MainActivity)
        val sheetView = layoutInflater.inflate(R.layout.modal_bottom_sheet_content, null)
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                    ?: return@setOnShowListener

            bottomSheet.setBackgroundColor(ContextCompat.getColor(this@MainActivity, android.R.color.transparent))
            bottomSheet.background = ContextCompat.getDrawable(this@MainActivity, R.drawable.rounded_dialog)

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

        mBottomSheetDialog.show()*/

    }

    private fun initTab() {

        tabLayout.addTab(tabLayout.newTab().setText("Today"))
        tabLayout.addTab(tabLayout.newTab().setText(Constants.World.substring(0, 1).uppercase() + Constants.World.substring(1).lowercase()))
        tabLayout.addTab(tabLayout.newTab().setText(Constants.Business.substring(0, 1).uppercase() + Constants.Business.substring(1).lowercase()))
        tabLayout.addTab(tabLayout.newTab().setText(Constants.Tech.substring(0, 1).uppercase() + Constants.Tech.substring(1).lowercase()))
        tabLayout.addTab(tabLayout.newTab().setText(Constants.Entertainment.substring(0, 1).uppercase() + Constants.Entertainment.substring(1).lowercase()))
        tabLayout.addTab(tabLayout.newTab().setText(Constants.Nation.substring(0, 1).uppercase() + Constants.Nation.substring(1).lowercase()))
        tabLayout.addTab(tabLayout.newTab().setText(Constants.Lifestyle.substring(0, 1).uppercase() + Constants.Lifestyle.substring(1).lowercase()))
        tabLayout.addTab(tabLayout.newTab().setText(Constants.Health.substring(0, 1).uppercase() + Constants.Health.substring(1).lowercase()))
        tabLayout.addTab(tabLayout.newTab().setText(Constants.Sports.substring(0, 1).uppercase() + Constants.Sports.substring(1).lowercase()))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        getNewsList(true, Constants.Today, country, "1")
                    }
                    1 -> {
                        getNewsList(true, Constants.World, country, "1")
                    }
                    2 -> {
                        getNewsList(true, Constants.Business, country, "1")
                    }
                    3 -> {
                        getNewsList(true, Constants.Tech, country, "1")
                    }
                    4 -> {
                        getNewsList(true, Constants.Entertainment, country, "1")
                    }
                    5 -> {
                        getNewsList(true, Constants.Nation, country, "1")
                    }
                    6 -> {
                        getNewsList(true, Constants.Lifestyle, country, "1")
                    }
                    7 -> {
                        getNewsList(true, Constants.Health, country, "1")
                    }
                    8 -> {
                        getNewsList(true, Constants.Sports, country, "1")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

}
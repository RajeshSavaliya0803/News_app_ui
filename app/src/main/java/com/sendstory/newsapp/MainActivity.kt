package com.sendstory.newsapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.firebase.messaging.FirebaseMessaging
import com.sendstory.newsapp.adapter.LoadingStateAdapter
import com.sendstory.newsapp.adapter.NewsPagingAdapter
import com.sendstory.newsapp.databinding.ActivityMainBinding
import com.sendstory.newsapp.fragment.NewsBottomSheet
import com.sendstory.newsapp.fragment.ShareBottomSheet
import com.sendstory.newsapp.retrofit.ApiResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    lateinit var tabLayout: TabLayout
    lateinit var recyclerView: RecyclerView
    lateinit var spinKit: SpinKitView
    lateinit var emptyText: TextView
    private lateinit var binding: ActivityMainBinding
    private lateinit var pagingAdapter: NewsPagingAdapter
    private lateinit var newsBottomSheet: NewsBottomSheet
    private lateinit var shareBottomSheet: ShareBottomSheet
    lateinit var country: String
    private lateinit var viewModel: NewsViewModel

    private var PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initList()
        initTab()
        initObserver()
    }


    private fun initObserver() {
        viewModel.responseObserve.observe(this) { response ->
            when (response) {
                is ApiResponse.Error -> {
                    Toast.makeText(this, "Error while getting News details", Toast.LENGTH_SHORT)
                        .show()
                }
                is ApiResponse.Loading -> {

                }
                is ApiResponse.Success -> {
                    val bundle = Bundle()
                    bundle.putSerializable(Constants.news, response.data?.news)
                    newsBottomSheet.arguments = bundle
                    newsBottomSheet.show(supportFragmentManager, NewsBottomSheet.TAG)
                }
            }

        }
    }


    private fun init() {
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        tabLayout = binding.tabLayout
        recyclerView = binding.mainRecycler
        spinKit = binding.spinKit
        emptyText = binding.emptyText
        newsBottomSheet = NewsBottomSheet()
        shareBottomSheet = ShareBottomSheet()
        val newsId = intent.getStringExtra(Constants.newsId)
        Log.e(TAG, "init: Intent Data => $newsId")
        if (newsId != null) {
            viewModel.getNewsDetail(id = newsId)
        }
    }


    private fun initList() {
        country = resources.configuration.locale.country
        Log.e(TAG, ":initList Country => $country")

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            Log.e(TAG, "initList: $token")
        })
        setupRecyclerView()
        getNewsList("", country)
    }

    private fun getNewsList(category: String, country: String) {
        lifecycleScope.launch {
            viewModel.fetchNews(category, country)
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                pagingAdapter.submitData(it)
            }
        }
    }

    private fun setupRecyclerView() {

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        pagingAdapter = NewsPagingAdapter(context = applicationContext, onClick = { news ->
            viewModel.getNewsDetail(id = news.id!!)
        }, onShared = { shareItem ->
            //Check permission
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !isHaveStoragePermission(*PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1)

            } else {
                val bundle = Bundle()
                bundle.putSerializable(Constants.news, shareItem)
                shareBottomSheet.arguments = bundle
                shareBottomSheet.show(supportFragmentManager, ShareBottomSheet.TAG)
            }


        })
        recyclerView.adapter =
            pagingAdapter.withLoadStateFooter(LoadingStateAdapter { pagingAdapter.retry() })
        initAdapterListener()
    }

    private fun initAdapterListener() {
        pagingAdapter.addLoadStateListener { listener ->
            when (listener.refresh) {
                is LoadState.NotLoading -> {
                    Log.e(TAG, "initAdapterListener: Not Loading")
                    Log.e(TAG, "initAdapterListener: data => ${pagingAdapter.itemCount}")
                    if (spinKit.visibility == View.VISIBLE) {
                        spinKit.visibility = View.GONE
                    }
                    if (pagingAdapter.itemCount == 0) {
                        if (emptyText.visibility == View.GONE) {
                            emptyText.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                        }

                    } else {
                        if (recyclerView.visibility == View.GONE) {
                            recyclerView.visibility = View.VISIBLE
                            emptyText.visibility = View.GONE
                        }
                    }
                }
                LoadState.Loading -> {
                    Log.e(TAG, "initAdapterListener: Loading")
                    spinKit.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                is LoadState.Error -> {
                    Log.e(TAG, "initAdapterListener: Error")
                    if (spinKit.visibility == View.VISIBLE) {
                        spinKit.visibility = View.GONE
                    }
                    Toast.makeText(
                        this,
                        ((listener.refresh as LoadState.Error).error.toString()),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initTab() {

        tabLayout.addTab(tabLayout.newTab().setText("Today"))
        tabLayout.addTab(
            tabLayout.newTab().setText(
                Constants.World.substring(0, 1).uppercase() + Constants.World.substring(1)
                    .lowercase()
            )
        )
        tabLayout.addTab(
            tabLayout.newTab().setText(
                Constants.Business.substring(0, 1).uppercase() + Constants.Business.substring(1)
                    .lowercase()
            )
        )
        tabLayout.addTab(
            tabLayout.newTab().setText(
                Constants.Tech.substring(0, 1).uppercase() + Constants.Tech.substring(1).lowercase()
            )
        )
        tabLayout.addTab(
            tabLayout.newTab().setText(
                Constants.Entertainment.substring(0, 1)
                    .uppercase() + Constants.Entertainment.substring(
                    1
                ).lowercase()
            )
        )
        tabLayout.addTab(
            tabLayout.newTab().setText(
                Constants.Nation.substring(0, 1).uppercase() + Constants.Nation.substring(1)
                    .lowercase()
            )
        )
        tabLayout.addTab(
            tabLayout.newTab().setText(
                Constants.Lifestyle.substring(0, 1).uppercase() + Constants.Lifestyle.substring(1)
                    .lowercase()
            )
        )
        tabLayout.addTab(
            tabLayout.newTab().setText(
                Constants.Politics.substring(0, 1).uppercase() + Constants.Politics.substring(1)
                    .lowercase()
            )
        )
        tabLayout.addTab(
            tabLayout.newTab().setText(
                Constants.Sports.substring(0, 1).uppercase() + Constants.Sports.substring(1)
                    .lowercase()
            )
        )

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        getNewsList(Constants.Today, country)
                    }
                    1 -> {
                        getNewsList(Constants.World, country)
                    }
                    2 -> {
                        getNewsList(Constants.Business, country)
                    }
                    3 -> {
                        getNewsList(Constants.Tech, country)
                    }
                    4 -> {
                        getNewsList(Constants.Entertainment, country)
                    }
                    5 -> {
                        getNewsList(Constants.Nation, country)
                    }
                    6 -> {
                        getNewsList(Constants.Lifestyle, country)
                    }
                    7 -> {
                        getNewsList(Constants.Politics, country)
                    }
                    8 -> {
                        getNewsList(Constants.Sports, country)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun isHaveStoragePermission(vararg permissions: String): Boolean {
        val value = permissions.all {
            ActivityCompat.checkSelfPermission(
                this@MainActivity,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
        Log.e(TAG, "isHaveStoragePermission: $value")
        return value
    }


}
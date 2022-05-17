package com.sendstory.newsapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.sendstory.newsapp.fragment.ModalBottomSheet
import com.sendstory.newsapp.retrofit.ApiResponse
import com.sendstory.newsapp.retrofit.NewsAPI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    lateinit var tabLayout: TabLayout
    lateinit var recyclerView: RecyclerView
    lateinit var spinKit: SpinKitView
    private lateinit var binding: ActivityMainBinding
    private lateinit var pagingAdapter: NewsPagingAdapter
    private lateinit var modalBottomSheet: ModalBottomSheet
    lateinit var country: String
    private lateinit var viewModel : NewsViewModel

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
        viewModel.responseObserve.observe(this){ response ->
            when(response){
                is ApiResponse.Error -> {
                    Toast.makeText(this,"Error while getting News details",Toast.LENGTH_SHORT).show()
                }
                is ApiResponse.Loading -> {

                }
                is ApiResponse.Success -> {
                    val bundle = Bundle()
                    bundle.putSerializable(Constants.news, response.data?.news)
                    modalBottomSheet.arguments = bundle
                    modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
                }
            }

        }
    }


    private fun init() {
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        tabLayout = binding.tabLayout
        recyclerView = binding.mainRecycler
        spinKit = binding.spinKit
        modalBottomSheet = ModalBottomSheet()
        val newsId = intent.getStringExtra(Constants.newsId)
        Log.e(TAG, "init: Intent Data => $newsId", )
        if(newsId != null){
            viewModel.getNewsDetail(id = newsId)
        }
    }

    private fun initList() {
        country = resources.configuration.locale.country
        Log.e(TAG, ":initList Country => $country", )

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            Log.e(TAG, "initList: $token")
        })
        setupRecyclerView()
        getNewsList("", country, )
    }
    
    private fun getNewsList(category: String, country: String) {
        lifecycleScope.launch{
            viewModel.fetchNews(category,country).flowWithLifecycle(lifecycle,Lifecycle.State.STARTED).collect {
                pagingAdapter.submitData(it)
            }
        }
    }

    private fun setupRecyclerView() {
        
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        pagingAdapter = NewsPagingAdapter(context = applicationContext) { item ->
            viewModel.getNewsDetail(id = item.id!!)
        }
        recyclerView.adapter = pagingAdapter.withLoadStateFooter(LoadingStateAdapter { pagingAdapter.retry() })

        initAdapterListener()
    }

    private fun initAdapterListener() {
        pagingAdapter.addLoadStateListener { listener ->
            when (listener.refresh) {
                is LoadState.NotLoading -> {
                    Log.e(TAG, "initAdapterListener: Not Loading", )
                    if(spinKit.visibility == View.VISIBLE){
                        spinKit.visibility = View.GONE
                    }
                    if(recyclerView.visibility == View.GONE){
                        recyclerView.visibility = View.VISIBLE
                    }
                }
                LoadState.Loading -> {
                    Log.e(TAG, "initAdapterListener: Loading", )
                    spinKit.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                is LoadState.Error -> {
                    Log.e(TAG, "initAdapterListener: Error", )
                    if(spinKit.visibility == View.VISIBLE){
                        spinKit.visibility = View.GONE
                    }
                    Toast.makeText(this, ((listener.refresh as LoadState.Error).error.toString()),Toast.LENGTH_SHORT).show()
                }
            }
        }
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
                        getNewsList(Constants.Health, country)
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

}
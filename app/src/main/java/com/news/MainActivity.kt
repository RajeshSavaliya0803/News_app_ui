package com.news

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.news.adapter.NewsListAdapter
import com.news.databinding.ActivityMainBinding
import com.news.fragment.ModalBottomSheet
import com.news.model.News


class MainActivity : AppCompatActivity() {

    lateinit var tabLayout: TabLayout

    //    lateinit var viewPager: ViewPager
    lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsList: ArrayList<News>
    private lateinit var newsList2: ArrayList<News>
    private lateinit var adapter: NewsListAdapter
    private lateinit var modalBottomSheet: ModalBottomSheet
//    private lateinit var superModalBottomSheet: SuperModalBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initList()
        initTab()
        setupRecyclerView()
    }


    private fun init() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        tabLayout = binding.tabLayout
//        viewPager = binding.viewPager
        recyclerView = binding.mainRecycler
        newsList = ArrayList()
        newsList2 = ArrayList()
        modalBottomSheet = ModalBottomSheet()
//        superModalBottomSheet = SuperModalBottomSheet()

    }

    private fun initList() {

        newsList.add(News(getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._11_h), R.drawable.ic_water, Constants.VIEW_TYPE_ONE))
        newsList.add(News(getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._11_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList.add(News(getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._11_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList.add(News(getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._11_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList.add(News(getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._11_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList.add(News(getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._11_h), R.drawable.ic_water, Constants.VIEW_TYPE_ONE))
        newsList.add(News(getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._11_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList.add(News(getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._11_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList.add(News(getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._11_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList.add(News(getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._11_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))

        newsList2.add(News(getString(R.string.lorem_latin), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._09_h), R.drawable.ic_water, Constants.VIEW_TYPE_ONE))
        newsList2.add(News(getString(R.string.lorem_latin), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._09_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList2.add(News(getString(R.string.lorem_latin), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._09_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList2.add(News(getString(R.string.lorem_latin), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._09_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList2.add(News(getString(R.string.lorem_latin), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._09_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList2.add(News(getString(R.string.lorem_latin), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._09_h), R.drawable.ic_water, Constants.VIEW_TYPE_ONE))
        newsList2.add(News(getString(R.string.lorem_latin), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._09_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList2.add(News(getString(R.string.lorem_latin), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._09_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList2.add(News(getString(R.string.lorem_latin), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._09_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))
        newsList2.add(News(getString(R.string.lorem_latin), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._09_h), R.drawable.ic_water, Constants.VIEW_TYPE_TWO))

    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = NewsListAdapter(newsList) { currentNews ->
            showModal(currentNews)
        }
        recyclerView.adapter = adapter


    }

    private fun showModal(news: News) {
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
//        superModalBottomSheet.show(supportFragmentManager,SuperModalBottomSheet.TAG)

    }

    private fun initTab() {

        tabLayout.addTab(tabLayout.newTab().setText("Today"))
        tabLayout.addTab(tabLayout.newTab().setText("World"))
        tabLayout.addTab(tabLayout.newTab().setText("Business"))
        tabLayout.addTab(tabLayout.newTab().setText("Tech"))
        tabLayout.addTab(tabLayout.newTab().setText("Entertainment"))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
//        val adapter = MainTabAdapter(supportFragmentManager, tabLayout.tabCount)
//        viewPager.adapter = adapter
//        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager.currentItem = tab.position
                if (tab.position % 2 == 0) {
                    adapter.setList(newsList)
                } else {
                    adapter.setList(newsList2)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

//    override fun someEvent(percent: Float) {
//        binding.vView.alpha = 1 - percent / 100
//    }

}
package com.news.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.news.MainActivity
import com.news.R
import com.news.adapter.MainAdapter
import com.news.databinding.FragmentTodayBinding
import com.news.detail.DetailActivity
import com.news.model.News
import com.news.slideUp.SlideUp
import com.news.slideUp.SlideUpBuilder


class TodayFragment : Fragment(), MainAdapter.OnItemClickListener {

    private lateinit var mainAdapter: MainAdapter
    private lateinit var binding: FragmentTodayBinding
    private lateinit var slideUp: SlideUp
    lateinit var slideView: View

    interface onNewsListener {
        fun someEvent(percent: Float)
    }

    var someEventListener: onNewsListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        someEventListener = try {
            activity as onNewsListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onNewsListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initSetClick()
    }

    private fun initSetClick() {
        binding.ivMain.setOnClickListener {
            startActivity(Intent(context, DetailActivity::class.java))
        }

        binding.sliderLayout.tvReadFullHistory.setOnClickListener {
            startActivity(Intent(context, DetailActivity::class.java))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {

        val list = ArrayList<News>()

        binding.rvMain.layoutManager = LinearLayoutManager(context as MainActivity)
        mainAdapter = MainAdapter(context as MainActivity, this)
        binding.rvMain.adapter = mainAdapter
        mainAdapter.setList(list)

//        list.add(News(getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._11_h), R.drawable.ic_water))
//        list.add(News(getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry), R.drawable.ic_ch_logo, getString(R.string.cnn), getString(R.string._11_h), R.drawable.ic_water))

        mainAdapter.notifyDataSetChanged()

        slideView = binding.sliderLayout.constSlider
        slideUp = SlideUpBuilder(slideView)
            .withListeners(object : SlideUp.Listener.Events {
                override fun onSlide(percent: Float) {
                    someEventListener!!.someEvent(percent)
                    binding.dim.alpha = 1 - percent / 100
                    if (percent < 100) {
                        binding.sliderLayout.ivArrow.visibility = View.GONE
                    }
                }

                override fun onVisibilityChanged(visibility: Int) {
                    if (visibility == View.GONE) {
                        binding.sliderLayout.ivArrow.visibility = View.GONE
                    }
                }
            })
            .withStartGravity(Gravity.BOTTOM)
            .withLoggingEnabled(true)
            .withGesturesEnabled(true)
            .withStartState(SlideUp.State.HIDDEN)
            .withSlideFromOtherView(binding.svMain)
            .build()

        /*slideView.setOnClickListener {
            slideUp.hide()
        }*/

    }

    override fun category(cateName: String) {
        slideUp.show()
    }
}
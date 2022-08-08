package com.bbj.myapplication.view

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.bbj.myapplication.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

class MainActivity : AppCompatActivity(),AskAPIKeyDialog.EnterClickInterface {

    private val viewPager: ViewPager2 by lazy { findViewById(R.id.main_view_pager) }
    private val gifView: GifImageView by lazy { findViewById(R.id.gif_anim) }
    private val fadeOutAnim by lazy { AnimationUtils.loadAnimation(this, R.anim.fade_out) }
    lateinit var primaryToTab: Array<ColorDrawable>
    lateinit var tabToPrimary: Array<ColorDrawable>
    private val handlerMain = Handler(Looper.getMainLooper())
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }


    override fun enter(apiKey: String) {
        viewModel.setAPIKey(apiKey.replace(" ",""))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!viewModel.isAPIKeySetted())
            showAskAPIDialog()

        viewModel.liveGif.observe(this) {
            if (it != 0) {
                handlerMain.postDelayed({
                    showGIF(it)
                }, 1000)
            }
        }

        val viewPager: ViewPager2 = findViewById(R.id.main_view_pager)
        viewPager.adapter = MainPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.isUserInputEnabled = false

        tuneTabs()
    }

    private fun showAskAPIDialog(){
        val askDialog = AskAPIKeyDialog()
        askDialog.show(supportFragmentManager,"ask")
    }

    private fun tuneTabs(){
        primaryToTab = arrayOf(
            ColorDrawable(resources.getColor(R.color.colorPrimary, theme)),
            ColorDrawable(resources.getColor(R.color.colorTabs, theme))
        )
        val transitionToTab = TransitionDrawable(primaryToTab)
        tabToPrimary = arrayOf(
            ColorDrawable(resources.getColor(R.color.colorTabs, theme)),
            ColorDrawable(resources.getColor(R.color.colorPrimary, theme))
        )
        val transitionToPrimary = TransitionDrawable(tabToPrimary)

        val customTab1: View? =
            LayoutInflater.from(this).inflate(R.layout.custom_tab_forecast, null)
        val tabForecastLottieAnim: LottieAnimationView? =
            customTab1?.findViewById(R.id.tab_forecast)
        val customTab2: View? = LayoutInflater.from(this).inflate(R.layout.custom_tab_plot, null)
        val tabPlotLottieAnim: LottieAnimationView? = customTab2?.findViewById(R.id.tab_plot)

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Прогноз погоды"
                }
                1 -> {
                    tab.text = "Выбрать город"
                }
            }
        }.attach()

        tabLayout.getTabAt(0)?.setCustomView(customTab1)
        tabLayout.getTabAt(1)?.setCustomView(customTab2)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when (tab.position) {
                        0 -> {
                            tabLayout.background = transitionToPrimary
                            transitionToPrimary.startTransition(1000)
                            if (!tabForecastLottieAnim?.isAnimating!!)
                                tabForecastLottieAnim.playAnimation()
                        }
                        1 -> {
                            tabLayout.background = transitionToTab
                            transitionToTab.startTransition(1000)
                            if (!tabPlotLottieAnim?.isAnimating!!) {
                                tabPlotLottieAnim.playAnimation()
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    fun returnToFirstPage() {
        if (gifView.drawable != null) {
            (gifView.drawable as GifDrawable).stop()
        }
        gifView.visibility = GifImageView.GONE
        handlerMain.removeCallbacksAndMessages(null)
        viewPager.currentItem = 0
    }

    fun showGIF(id: Int) {
        gifView.setImageResource(id)
        val gifDrawable = gifView.drawable as GifDrawable
        gifView.alpha = 0.0f
        gifView.visibility = GifImageView.VISIBLE
        gifView.animate().alpha(1.0f)
        gifDrawable.loopCount = 0
        gifDrawable.start()
        handlerMain.postDelayed({
            gifView.startAnimation(fadeOutAnim)
            gifView.animation.setAnimationListener(object :
                Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    gifDrawable.stop()
                    gifView.visibility = GifImageView.GONE
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }
            })
        }, 4500)
    }


}
package com.example.worldnewsinfo.presentation.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.core.view.WindowCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.worldnewsinfo.application.NavigatorCompat
import com.example.worldnewsinfo.R
import com.example.worldnewsinfo.presentation.headlines.HeadlinesFragment
import com.example.worldnewsinfo.presentation.navigationBar.NavBarFragment
import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val splashScreen = installSplashScreen()
        if (savedInstanceState == null) {
            setTheme(R.style.Theme_Starting)
            splashScreen.setOnExitAnimationListener { plugLottie(it) }
        }
        else{
            setTheme(R.style.Base_Theme_World_News_Info)
            animationEnd()
        }

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        NavigatorCompat.activity = this

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_bar, NavBarFragment.newInstance())
                .addToBackStack("nav_bar")
                .commit()
            supportFragmentManager.beginTransaction()
                .replace(R.id.main, HeadlinesFragment.newInstance())
                .addToBackStack("main")
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun plugLottie(vp: SplashScreenViewProvider) {
        val lottieView = findViewById<LottieAnimationView>(R.id.animationView)
        lottieView.enableMergePathsForKitKatAndAbove(true)
        val splashScreenAnimationEndTime =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Instant.ofEpochMilli(vp.iconAnimationStartMillis + vp.iconAnimationDurationMillis)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        val delay =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Instant.now(Clock.systemUTC()).until(
                    splashScreenAnimationEndTime,
                    ChronoUnit.MILLIS
                )
            } else {
                vp.iconAnimationDurationMillis
            }

        lottieView.postDelayed({
            vp.view.alpha = 0f
            vp.iconView.alpha = 0f
            lottieView!!.playAnimation()
        }, delay)

        lottieView.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                animationEnd()
            }
        })
    }

    fun animationEnd(){
        val animationView = findViewById<View>(R.id.animationView)
        val navBar = findViewById<View>(R.id.nav_bar)
        animationView.visibility = View.GONE
        navBar.visibility = View.VISIBLE
        findViewById<View>(R.id.main).visibility = View.VISIBLE
    }

    override fun onDestroy() {
        NavigatorCompat.activity = null
        super.onDestroy()
    }
}


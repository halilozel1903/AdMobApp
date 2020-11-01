package com.halil.ozel.admobapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var interstitialAd: InterstitialAd

    private lateinit var rewardedAd: RewardedAd


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        App ID : The unique ID assigned to your app. You'll need to integrate
        the app ID into your app's source code to use certain features in AdMob.
         */

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        bannerID.loadAd(adRequest) //banner load


        rewardedAd = RewardedAd(this, "ca-app-pub-3940256099942544/5224354917")
        val adLoadCallback = object: RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                // Ad successfully loaded.
            }
            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                // Ad failed to load.
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)

        //Interstitial Ad
        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712" //Interstitial Ad Id


        val request = AdRequest.Builder().build()
        interstitialAd.loadAd(request)

        interstitialAd.adListener = object : AdListener() { //listener add.

            override fun onAdClosed() { //when the ad is closed

                //go to the next screen.
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                startActivity(intent)

                //If you come to this screen again, show the ad again.
                interstitialAd.loadAd(request)

            }
        }


        btnRewardedAd.setOnClickListener {

            if (rewardedAd.isLoaded) {
                val activityContext: Activity = this@MainActivity
                val adCallback = object: RewardedAdCallback() {

                    override fun onRewardedAdOpened() {
                        // Ad opened.
                    }
                    override fun onRewardedAdClosed() {
                        // Ad closed.
                    }
                    override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                        // User earned reward.

                        println("Amount : "+reward.amount)
                    }
                    override fun onRewardedAdFailedToShow(adError: AdError) {
                        // Ad failed to display.
                    }

                }
                rewardedAd.show(activityContext, adCallback)
            }
            else {
                Log.d("TAG", "The rewarded ad wasn't loaded yet.")
            }
        }

        btnInterstitialAd.setOnClickListener {

            if (interstitialAd.isLoaded) { //when the ad is loaded
                interstitialAd.show() //show ad
            } else { //didn't load ad

                //go to the next screen.
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                startActivity(intent)
            }

        }
    }
}

package com.halil.ozel.admobapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

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


        //Interstitial Ad
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712" //Interstitial Ad Id


        val request = AdRequest.Builder().build()
        mInterstitialAd.loadAd(request)

        mInterstitialAd.adListener = object : AdListener() { //listener add.

            override fun onAdClosed() { //when the ad is closed

                //go to the next screen.
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                startActivity(intent)

                //If you come to this screen again, show the ad again.
                mInterstitialAd.loadAd(request)

            }
        }


        newActivity.setOnClickListener {

            if (mInterstitialAd.isLoaded) { //when the ad is loaded
                mInterstitialAd.show() //show ad
            } else { //didn't load ad

                //go to the next screen.
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                startActivity(intent)
            }


        }
    }
}

package com.halil.ozel.admobapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.halil.ozel.admobapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mInterstitialAd: InterstitialAd? = null
    private var mRewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /*
        App ID : The unique ID assigned to your app. You'll need to integrate
        the app ID into your app's source code to use certain features in AdMob.
         */

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.bannerID.loadAd(adRequest) //banner load

        binding.btnRewardedAd.setOnClickListener {
            RewardedAd.load(this,
                REWARDED_AD,
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d(TAG, adError.message)
                        mRewardedAd = null
                        val intent = Intent(this@MainActivity, SecondActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        Log.d(TAG, getString(R.string.ad_info))
                        mRewardedAd = rewardedAd
                        mRewardedAd?.show(this@MainActivity) { rewardItem ->
                            val rewardAmount = rewardItem.amount
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.rewarded_info) + " " + rewardAmount,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })


            binding.btnInterstitialAd.setOnClickListener {
                InterstitialAd.load(this,
                    INTERSTITIAL_AD,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.d(TAG, adError.message)
                            mInterstitialAd = null
                            val intent = Intent(this@MainActivity, SecondActivity::class.java)
                            startActivity(intent)
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            Log.d(TAG, getString(R.string.ad_info))
                            mInterstitialAd = interstitialAd
                            mInterstitialAd?.show(this@MainActivity)
                        }
                    })
            }
        }
    }

    companion object {
        const val TAG = "ANDROID ADMOB SAMPLE"
        const val INTERSTITIAL_AD = "ca-app-pub-3940256099942544/1033173712"
        const val REWARDED_AD = "ca-app-pub-3940256099942544/5224354917"
    }
}

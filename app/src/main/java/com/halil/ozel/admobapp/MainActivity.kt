package com.halil.ozel.admobapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.halil.ozel.admobapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {
            loadBannerAd()
            loadInterstitialAd()
            loadRewardedAd()
        }

        binding.btnInterstitialAd.setOnClickListener { showInterstitialAd() }
        binding.btnRewardedAd.setOnClickListener { showRewardedAd() }
    }

    override fun onResume() {
        super.onResume()
        binding.bannerAdView.resume()
    }

    override fun onPause() {
        binding.bannerAdView.pause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.bannerAdView.destroy()
        super.onDestroy()
    }

    private fun loadBannerAd() {
        binding.bannerAdView.loadAd(createAdRequest())
    }

    private fun loadInterstitialAd() {
        InterstitialAd.load(
            this,
            INTERSTITIAL_AD_UNIT_ID,
            createAdRequest(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    showMessage(adError.message)
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    showMessage(getString(R.string.ad_info))
                }
            }
        )
    }

    private fun loadRewardedAd() {
        RewardedAd.load(
            this,
            REWARDED_AD_UNIT_ID,
            createAdRequest(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                    showMessage(adError.message)
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    showMessage(getString(R.string.ad_info))
                }
            }
        )
    }

    private fun showInterstitialAd() {
        val ad = interstitialAd
        if (ad == null) {
            showMessage(getString(R.string.ad_loading))
            loadInterstitialAd()
            openSecondActivity()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                showMessage(getString(R.string.ad_closed))
                loadInterstitialAd()
                openSecondActivity()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                interstitialAd = null
                showMessage(adError.message)
                loadInterstitialAd()
                openSecondActivity()
            }
        }
        ad.show(this)
    }

    private fun showRewardedAd() {
        val ad = rewardedAd
        if (ad == null) {
            showMessage(getString(R.string.ad_loading))
            loadRewardedAd()
            openSecondActivity()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                loadRewardedAd()
                openSecondActivity()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                rewardedAd = null
                showMessage(adError.message)
                loadRewardedAd()
                openSecondActivity()
            }
        }
        ad.show(this) { rewardItem ->
            showMessage("${getString(R.string.rewarded_info)} ${rewardItem.amount} ${rewardItem.type}")
        }
    }

    private fun createAdRequest(): AdRequest = AdRequest.Builder().build()

    private fun openSecondActivity() {
        startActivity(Intent(this, SecondActivity::class.java))
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        private const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
    }
}

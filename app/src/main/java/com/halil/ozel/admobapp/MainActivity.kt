package com.halil.ozel.admobapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
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

        /* Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713") // uygulama id'si
        var adRequest = AdRequest.Builder().build()
        bannerID.loadAd(adRequest) */


        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713") // uygulama id'si
        var adRequest = AdRequest.Builder().build()
        bannerID.loadAd(adRequest)


        // geçiş reklamı
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"

        var gecisliReklam = AdRequest.Builder().build()
        mInterstitialAd.loadAd(gecisliReklam)
        mInterstitialAd.adListener = object : AdListener(){

            override fun onAdClosed() { // reklam kapatıldığında

                // bir sonraki ekrana git.
                var intent = Intent(this@MainActivity,Main2Activity::class.java)
                startActivity(intent)

                // bir daha bu ekrana gelinirse reklamı tekrardan göster.
                mInterstitialAd.loadAd(gecisliReklam)

            }
        }


        newActivity.setOnClickListener {

            if (mInterstitialAd.isLoaded){ // yüklendiyse
                mInterstitialAd.show() // reklamı göster
            }else{ // yüklenmediyse

                // intent ile bir sonraki ekrana git.
                var intent = Intent(this@MainActivity,Main2Activity::class.java)
                startActivity(intent)
            }


        }
    }
}

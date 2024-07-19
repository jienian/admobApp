package com.example.admobapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.InterstitialAd;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private Button btn,btn2;

    // Rewarded Ads
    private RewardedAd mRewardedAd;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        // Banners Ad
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // Interstitial Ad
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3207185251183279/8637745562");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });


        // Rewarded Ads
        // 1- Adding internet permission, APP ID, Dependency  [DONE] - See previous videos for more info
        // 2- Creating Second Activity [the user will be rewarded after watching
        //                                  the ad to go to second screen]

        // 3- Configuring the Rewarded Ads

        // ca-app-pub-3940256099942544/5224354917 is for testing
        // ca-app-pub-3207185251183279/8184505336 our real ads id
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback(){
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "onAdFailedToLoad");
                    }
                });

        // Loading ads on button click
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayRewardedAdsAndTakeMeToSecondActivity();
            }
        });




    }



    private void DisplayRewardedAdsAndTakeMeToSecondActivity() {
        // Going to second screen
        Intent i = new Intent(this, SecondScreen.class);
        startActivity(i);

        // Display Ads
        if (mRewardedAd != null) {
            Activity activityContext = MainActivity.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.

                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();

                    Log.d("TAG", "The user earned" + rewardAmount);
                }

                // ads need some time to load...
                // Don't worry it will work soon

                // Thanks for your time
                // Rate us 5 stars on udemy and playstore
                // Subscribe to Master Coding Channel
            });
        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
        }





    }
}
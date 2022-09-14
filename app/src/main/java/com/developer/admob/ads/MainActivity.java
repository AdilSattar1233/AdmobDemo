package com.developer.admob.ads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.developer.admob.ads.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private AdView adView;
    private ActivityMainBinding binding;
    private Dialog adDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        loadBanner();
        loadNative();
        loadInterstitial();
        loadReward();
        loadRewardInterstitial();
        eventHandling();
    }

    private void eventHandling() {
        binding.buttonInterstitial.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            showInterstitialAdDialog(intent);
        });
        binding.buttonReward.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            showRewardAdDialog(intent);
        });
        binding.buttonRewardedInterstitial.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            showRewardInterstitialAdDialog(intent);
        });

    }

    private void loadRewardInterstitial(){
        AdManager.loadRewardedInterstitial(MainActivity.this);
    }
    private void loadReward(){
        AdManager.loadRewarded(MainActivity.this);
    }
    private void loadInterstitial() {
        AdManager.loadInterstitial(MainActivity.this);
    }

    private void loadBanner() {
        adView = new AdView(MainActivity.this);
        AdManager.displayBanner(MainActivity.this, adView, binding.bannerContainer);
    }

    private void loadNative() {
        AdManager.showNative(getApplicationContext(), binding.nativeContainer);
    }

    private void showRewardInterstitialAdDialog(Intent intent) {
        adDialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        adDialog.setContentView(R.layout.ad_dialog);
        adDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        if (AdManager.isRewardInterstitialLoad()) {
            adDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adDialog.dismiss();
                    AdManager.showRewardedInterstitial(MainActivity.this, intent);
                }
            }, 2000);
        } else {
            AdManager.loadRewardedInterstitial(MainActivity.this);
            startActivity(intent);
        }
    }

    private void showRewardAdDialog(Intent intent) {
        adDialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        adDialog.setContentView(R.layout.ad_dialog);
        adDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        if (AdManager.isRewardLoad()) {
            adDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adDialog.dismiss();
                    AdManager.showReward(MainActivity.this, intent);
                }
            }, 2000);
        } else {
            AdManager.loadRewarded(MainActivity.this);
            startActivity(intent);
        }
    }

    private void showInterstitialAdDialog(Intent intent) {
        adDialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        adDialog.setContentView(R.layout.ad_dialog);
        adDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        if (AdManager.isInterstitialLoad()) {
            adDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adDialog.dismiss();
                    AdManager.showInterstitial(MainActivity.this, intent);
                }
            }, 2000);
        } else {
            AdManager.loadInterstitial(MainActivity.this);
            startActivity(intent);
        }
    }


    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        if (adDialog != null) {
            adDialog.dismiss();
        }
        super.onDestroy();
    }


}
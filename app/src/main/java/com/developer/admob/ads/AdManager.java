package com.developer.admob.ads;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

import java.util.logging.Handler;

public class AdManager {
    private static RewardedInterstitialAd mRewardedInterstitialAd;
    private static boolean isRewardedInterstitialLoaded = false;


    private static RewardedAd mRewardedAd;
    private static boolean isRewardLoaded = false;

    private static InterstitialAd mInterstitialAd;
    private static boolean isInterstitialLoaded = false;

    public static void loadRewardedInterstitial(Activity context){
        RewardedInterstitialAd.load(context, context.getString(R.string.ad_rewarded_interstitial),
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        Log.d("TAG", "Ad was loaded.");
                        mRewardedInterstitialAd = ad;
                        isRewardedInterstitialLoaded = true;
                    }
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.d("TAG", loadAdError.toString());
                        mRewardedInterstitialAd = null;
                        isRewardedInterstitialLoaded = false;
                    }
                });
    }
    public static void loadRewarded(Activity context){
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, context.getString(R.string.ad_rewarded),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("TAG", loadAdError.toString());
                        mRewardedAd = null;
                        isRewardLoaded = false;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        isRewardLoaded = true;
                        mRewardedAd = rewardedAd;
                        Log.d("TAG", "Ad was loaded.");
                    }
                });
    }
    public static void loadInterstitial(Activity context){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, context.getString(R.string.ad_interstitial), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        isInterstitialLoaded = true;
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAG", loadAdError.toString());
                        mInterstitialAd = null;
                        isInterstitialLoaded = false;
                    }
                });
    }

    public static void showRewardedInterstitial(Activity context , Intent intent){
        if (mRewardedInterstitialAd != null){
            mRewardedInterstitialAd.show(context, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                }
            });
            mRewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d("TAG", "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d("TAG", "Ad dismissed fullscreen content.");
                    mRewardedInterstitialAd = null;
                    context.startActivity(intent);
                    isRewardedInterstitialLoaded = false;
                    loadRewardedInterstitial(context);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.e("TAG", "Ad failed to show fullscreen content.");
                    mRewardedInterstitialAd = null;
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d("TAG", "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d("TAG", "Ad showed fullscreen content.");
                }
            });
        }
    }
    public static void showReward(Activity context , Intent intent){
        if (mRewardedAd != null) {
            mRewardedAd.show(context, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d("TAG", "The user earned the reward.");
                }
            });
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d("TAG", "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d("TAG", "Ad dismissed fullscreen content.");
                    mRewardedAd = null;
                    context.startActivity(intent);
                    isRewardLoaded = false;
                    loadRewarded(context);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.e("TAG", "Ad failed to show fullscreen content.");
                    mRewardedAd = null;
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d("TAG", "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d("TAG", "Ad showed fullscreen content.");
                }
            });
        }
    }
    public static void showInterstitial(Activity context , Intent intent) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(context);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d("TAG", "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d("TAG", "Ad dismissed fullscreen content.");
                    mInterstitialAd = null;
                    isInterstitialLoaded = false;
                    context.startActivity(intent);
                    loadInterstitial(context);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.e("TAG", "Ad failed to show fullscreen content.");
                    mInterstitialAd = null;
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d("TAG", "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d("TAG", "Ad showed fullscreen content.");
                }
            });

        }
    }
    public static boolean isInterstitialLoad(){
        return isInterstitialLoaded;
    }
    public static boolean isRewardLoad(){
        return isRewardLoaded;
    }
    public static boolean isRewardInterstitialLoad(){
        return isRewardedInterstitialLoaded;
    }

    public static void displayBanner(Activity context , AdView adView , FrameLayout adContainerView){
        adView.setAdUnitId(context.getString(R.string.ad_banner));
        adContainerView.removeAllViews();
        adContainerView.addView(adView);
        AdSize adSize = getAdSize(context , adContainerView);
        adView.setAdSize(adSize);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private static AdSize getAdSize(Activity context , FrameLayout adContainerView) {
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = outMetrics.density;
        float adWidthPixels = adContainerView.getWidth();
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }
    public static void showNative(Context context , TemplateView template){
        AdLoader adLoader = new AdLoader.Builder(context, context.getString(R.string.ad_native))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                      /*  NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(background).build();
                        template.setStyles(styles);*/
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }


}

package com.stem.admob;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    AdView mAdView;
    ImageView img1, img2,img3,img4,img5,img6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        img1 = findViewById(R.id.img1) ;
        img2 = findViewById(R.id.img2) ;
        img3 = findViewById(R.id.img3) ;
        img4 = findViewById(R.id.img4) ;
        img5 = findViewById(R.id.img5) ;
        img6 = findViewById(R.id.img6) ;

        img1.setOnClickListener(view -> {
            showInterstitial();
        });
        img2.setOnClickListener(view -> {
            showInterstitial();
        });
        img3.setOnClickListener(view -> {
            showInterstitial();
        });
        img4.setOnClickListener(view -> {
            showInterstitial();
        });
        img5.setOnClickListener(view -> {
            showInterstitial();
        });
        img6.setOnClickListener(view -> {
            showInterstitial();
        });

        mAdView = findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);




        if (getString(R.string.show_admob_ad).contains("ON")) {
            initAdmobAd();
            loadBannerAd();
            loadFullscreenAd();
        }



    }

    private void initAdmobAd() {
        // AdMob Initialization
        MobileAds.initialize(this, initializationStatus -> {});
    }


    // Banner Ad Load Counter
    int BANNER_AD_CLICK_COUNT = 0;

    // loadBannerAd method
    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (BANNER_AD_CLICK_COUNT >= 2) {
                    if (mAdView != null) mAdView.setVisibility(View.GONE);
                } else {
                    if (mAdView != null) mAdView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
            }

            @Override
            public void onAdClicked() {
                BANNER_AD_CLICK_COUNT++;
                if (BANNER_AD_CLICK_COUNT >= 2) {
                    if (mAdView != null) mAdView.setVisibility(View.GONE);
                }
            }
        });
    }

    // Fullscreen Ad Load Counter
    int FULLSCREEN_AD_LOAD_COUNT = 0;

    // loadFullscreenAd method
    private void loadFullscreenAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        FULLSCREEN_AD_LOAD_COUNT++;
                        if (FULLSCREEN_AD_LOAD_COUNT < 3) loadFullscreenAd();
                        Log.d("FULLSCREEN_AD", "Ad dismissed. Load count: " + FULLSCREEN_AD_LOAD_COUNT);
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
                Log.d("FULLSCREEN_AD", "Failed to load interstitial ad: " + loadAdError.getMessage());
            }
        });
    }

    // Show Interstitial Ad
    private void showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("INTERSTITIAL_AD", "Interstitial ad is not ready yet.");
        }
    }
}
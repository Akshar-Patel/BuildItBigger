package com.example.build_it_bigger.free;

import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.example.build_it_bigger.EndpointsAsyncTask;
import com.example.build_it_bigger.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
  private AdView mAdView;
  private CountingIdlingResource mCountingIdlingResource;
  private EndpointsAsyncTask mEndpointsAsyncTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mEndpointsAsyncTask = new EndpointsAsyncTask(mCountingIdlingResource);
    mEndpointsAsyncTask.execute(this);

    Button tellMeOneMoreButton = findViewById(R.id.button_tell_me_one_more);
    tellMeOneMoreButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        mEndpointsAsyncTask = new EndpointsAsyncTask(mCountingIdlingResource);
        mEndpointsAsyncTask.execute(view.getContext());
      }
    });

    //Sample App Id for Test Ads
    MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

    mAdView = findViewById(R.id.ad_view);

    AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .build();

    mAdView.loadAd(adRequest);
  }
}

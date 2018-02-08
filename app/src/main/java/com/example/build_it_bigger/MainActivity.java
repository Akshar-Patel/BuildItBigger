package com.example.build_it_bigger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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
  }

  /**
   * Only called from test.
   */
  @VisibleForTesting
  @NonNull
  public CountingIdlingResource getCountingIdlingResource() {
    if (mCountingIdlingResource == null) {
      mCountingIdlingResource = new CountingIdlingResource("JokeApiCall");
    }
    return mCountingIdlingResource;
  }

  @VisibleForTesting
  @NonNull
  public EndpointsAsyncTask getEndpointsAsyncTask() {

    return mEndpointsAsyncTask;
  }
}

package com.example.build_it_bigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.example.joke_teller_activity.JokeTellerActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.jokeApi.JokeApi;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

  private static CountingIdlingResource mCountingIdlingResource;
  private EndpointsAsyncTask mEndpointsAsyncTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mEndpointsAsyncTask = new EndpointsAsyncTask();
    mEndpointsAsyncTask.execute(this);

    Button tellMeOneMoreButton = findViewById(R.id.button_tell_me_one_more);
    tellMeOneMoreButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        mEndpointsAsyncTask = new EndpointsAsyncTask();
        mEndpointsAsyncTask.execute(view.getContext());
      }
    });
  }

  static class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {

    private static JokeApi myApiService = null;
    private Context context;

    private String joke;

    public EndpointsAsyncTask() {
      mCountingIdlingResource = null;
    }

    public EndpointsAsyncTask(
        CountingIdlingResource countingIdlingResource) {
      mCountingIdlingResource = countingIdlingResource;
      if (mCountingIdlingResource != null) {
        mCountingIdlingResource.increment();
      }
    }

    @Override
    protected String doInBackground(Context... params) {

      if (myApiService == null) {  // Only do this once
        JokeApi.Builder builder = new JokeApi.Builder(AndroidHttp.newCompatibleTransport(),
            new AndroidJsonFactory(), null)
            // options for running against local devappserver
            // - 10.0.2.2 is localhost's IP address in Android emulator
            // - turn off compression when running against local devappserver
            .setRootUrl("http://10.0.2.2:8080/_ah/api/")
            .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
              @Override
              public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                  throws IOException {
                abstractGoogleClientRequest.setDisableGZipContent(true);
              }
            });
        // end options for devappserver

        myApiService = builder.build();
      }

      context = params[0];

      try {
        return myApiService.tellMeAJoke().execute().getJoke();
      } catch (IOException e) {
        return e.getMessage();
      }
    }

    @Override
    protected void onPostExecute(String result) {
      joke = result;
      if (mCountingIdlingResource != null) {
        mCountingIdlingResource.decrement();
      }
      Intent intent = new Intent(context, JokeTellerActivity.class);
      intent.putExtra(context.getString(R.string.key_extra_joke), result);
      context.startActivity(intent);
    }

    String getJokeResult() {
      return joke;
    }
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

package com.example.build_it_bigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.test.espresso.idling.CountingIdlingResource;
import com.example.joke_teller_activity.JokeTellerActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.jokeApi.JokeApi;
import java.io.IOException;

/**
 * Created by ab on 8/2/18.
 */
public class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {

  private static JokeApi myApiService = null;
  private Context context;
  private CountingIdlingResource mCountingIdlingResource;

  private String joke;

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

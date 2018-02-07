package com.example.build_it_bigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.example.joke_teller_activity.JokeTellerActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.jokeApi.JokeApi;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    new EndpointsAsyncTask().execute(this);

    Button tellMeOneMoreButton = findViewById(R.id.button_tell_me_one_more);
    tellMeOneMoreButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        new EndpointsAsyncTask().execute(view.getContext());
      }
    });
  }

  static class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {
    private static JokeApi myApiService = null;
    private Context context;

    @Override
    protected String doInBackground(Context... params) {
      if(myApiService == null) {  // Only do this once
        JokeApi.Builder builder = new JokeApi.Builder(AndroidHttp.newCompatibleTransport(),
            new AndroidJsonFactory(), null)
            // options for running against local devappserver
            // - 10.0.2.2 is localhost's IP address in Android emulator
            // - turn off compression when running against local devappserver
            .setRootUrl("http://10.0.2.2:8080/_ah/api/")
            .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
              @Override
              public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
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
      Toast.makeText(context, result, Toast.LENGTH_LONG).show();
      Intent intent = new Intent(context,JokeTellerActivity.class);
      intent.putExtra(context.getString(R.string.key_extra_joke),result);
      context.startActivity(intent);
    }
  }
}

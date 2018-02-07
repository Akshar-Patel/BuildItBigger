package com.example.joke_teller_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class JokeTellerActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_joke_teller);

    TextView jokeTextVieW = findViewById(R.id.text_view_joke);
    Button okButton = findViewById(R.id.button_ok);

    if (getIntent() != null) {
      String joke = getIntent().getStringExtra(getString(R.string.key_extra_joke));
      jokeTextVieW.setText(joke);
    }

    okButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }
}

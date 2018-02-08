package com.example.build_it_bigger;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.text.TextUtils;
import android.util.Log;
import com.example.build_it_bigger.MainActivity.EndpointsAsyncTask;
import java.util.concurrent.CountDownLatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class JokeAsyncTaskUnitTest {
  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  private EndpointsAsyncTask mEndpointsAsyncTask;
  private CountingIdlingResource mCountingIdlingResource;

  @Before
  public void registerIdlingResource(){
    mCountingIdlingResource = mActivityTestRule.getActivity().getCountingIdlingResource();
    IdlingRegistry.getInstance().register(mCountingIdlingResource);
    mEndpointsAsyncTask = mActivityTestRule.getActivity().getEndpointsAsyncTask();
  }

  @Test
  public void asycTask_loadsJokes(){
    assertFalse(TextUtils.isEmpty(mEndpointsAsyncTask.getJokeResult()));
  }

  @After
  public void unRegisterIdlingResource() {
    if (mCountingIdlingResource != null) {
      IdlingRegistry.getInstance().unregister(mCountingIdlingResource);
    }
  }
}

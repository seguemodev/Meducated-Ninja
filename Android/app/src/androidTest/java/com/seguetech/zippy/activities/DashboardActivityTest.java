package com.seguetech.zippy.activities;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.seguetech.zippy.actions.ForkScreenshotAction;
import com.seguetech.zippy.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DashboardActivityTest  {
    @Rule
    public ActivityTestRule<DashboardActivity> mActivityRule = new ActivityTestRule<>(DashboardActivity.class);

  @Before
  public void setUp() {

  }
    @Test
    public void testDashboardDisplay() throws Exception {
        onView(withId(R.id.search_button)).check(matches(isDisplayed()));
        onView(withId(R.id.cabinet_button)).check(matches(isDisplayed()));
        onView(withId(R.id.news_button)).check(matches(isDisplayed()));
        ForkScreenshotAction.perform("initial_state",withId(R.id.news_button));
    }

    @Test
    public void testDashboardSearchButton() throws Exception {
      onView(withId(R.id.search_button)).check(matches(isDisplayed())).check(matches(isClickable()));
      ForkScreenshotAction.perform("initial_state",withId(R.id.search_button));
      onView(withId(R.id.search_button)).perform(click());
        onView(withId(R.id.inputLayout)).check(matches(isDisplayed()));
        ForkScreenshotAction.perform("final_state",withId(R.id.inputLayout));
    }

    @Test
    public void testDashboardCabinetButton() throws Exception {
        ForkScreenshotAction.perform("initial_state",withId(R.id.cabinet_button));
        onView(withId(R.id.cabinet_button))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .perform(click());
        onView(withId(R.id.cabinets)).check(matches(isDisplayed()));
        ForkScreenshotAction.perform("final_state",withId(R.id.cabinets));
    }

    @Test
    public void testDashboardNewsButton() throws Exception {
      SystemClock.sleep(128);
        ForkScreenshotAction.perform("initial_state",withId(R.id.news_button));
        onView(withId(R.id.news_button))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .perform(click());
        // there currently isn't a view in the news tab.
        ForkScreenshotAction.perform("final_state");
    }




}

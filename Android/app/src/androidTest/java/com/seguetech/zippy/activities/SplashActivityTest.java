package com.seguetech.zippy.activities;

import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.seguetech.zippy.actions.ForkScreenshotAction;
import com.seguetech.zippy.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityRule = new ActivityTestRule<>(SplashActivity.class);

    @Before
    public void setUp() {
        // ensure that espresso doesn't time out while we're intentionally idling.
        IdlingPolicies.setMasterPolicyTimeout(SplashActivity.DISPLAY_TIME * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(SplashActivity.DISPLAY_TIME * 2, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testSplashSreen() throws Exception {
        ForkScreenshotAction.perform("initial_state",withId(R.id.logo));


        // verify that the logo is displayed.
        onView(withId(R.id.logo)).check(matches(isDisplayed()));

        // wait for the amount of time configured in the activity.
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(SplashActivity.DISPLAY_TIME);
        registerIdlingResources(idlingResource);

        // verify that the dashboard is displayed.
        onView(withId(R.id.dashboard)).check(matches(isDisplayed()));

        // clean-up our idling watcher.
        unregisterIdlingResources(idlingResource);

        ForkScreenshotAction.perform("final_state");

    }


    @After
    public void tearDown() throws Exception {
    }
}

package com.seguetech.zippy.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.seguetech.zippy.actions.ForkScreenshotAction;

import com.seguetech.zippy.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AboutActivityTest {
    @Rule
    public ActivityTestRule<AboutActivity> mActivityRule = new ActivityTestRule<>(AboutActivity.class);


    @Test
    public void testAboutDisplay() throws Exception {
        ForkScreenshotAction.perform("initial_state",withId(R.id.title));
    }
}

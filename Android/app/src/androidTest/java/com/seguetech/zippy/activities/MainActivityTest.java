package com.seguetech.zippy.activities;

import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.seguetech.zippy.actions.ForkScreenshotAction;
import com.seguetech.zippy.R;
import com.seguetech.zippy.actions.OrientationChangeAction;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.seguetech.zippy.actions.OrientationChangeAction.orientationLandscape;
import static com.seguetech.zippy.actions.OrientationChangeAction.orientationPortrait;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testChangingOptionsMenu() throws Exception {
        ForkScreenshotAction.perform("initial_state",withId(R.id.viewpager));
        // verify we're on the cabinet tab, and that the about action item is displayed.
        onView(withId(R.id.cabinets)).check(matches(isDisplayed()));
        onView(withId(R.id.about)).check(matches(isDisplayed()));
        // swipe left
        onView(withId(R.id.viewpager)).check(matches(isDisplayed())).perform(swipeLeft());
        SystemClock.sleep(500);
        // verify we're on the search tab, and that the sort action item is displayed
        onView(withId(R.id.about)).check(doesNotExist());
        onView(withId(R.id.search_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.sort)).check(matches(isDisplayed()));

        ForkScreenshotAction.perform("search_tab", withId(R.id.search_layout));
        onView(withId(R.id.search_layout)).perform(swipeRight());
        SystemClock.sleep(500);
        onView(withId(R.id.cabinets)).check(matches(isDisplayed()));
        onView(withId(R.id.sort)).check(doesNotExist());
        onView(withId(R.id.about)).check(matches(isDisplayed()));
        ForkScreenshotAction.perform("final_state",withId(R.id.about));

    }

    @Test
    public void testSortDialogToggleCheckboxes() throws Exception {
        ForkScreenshotAction.perform("initial_state", withId(R.id.search_layout));
        onView(withId(R.id.cabinets)).check(matches(isDisplayed())).perform(swipeLeft());
        onView(withId(R.id.search_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.sort)).check(matches(isDisplayed())).perform(click());

        ForkScreenshotAction.perform("sort_dialog_default", withId(R.id.descending));

        // this should be the initial state on a clean install.
        onView(withId(R.id.ascending)).check(matches(isChecked()));
        onView(withId(R.id.descending)).check(matches(isNotChecked()));

        // check descending, ascending should be unchecked.
        onView(withId(R.id.descending)).perform(click());
        onView(withId(R.id.descending)).check(matches(isChecked()));
        onView(withId(R.id.ascending)).check(matches(isNotChecked()));

        ForkScreenshotAction.perform("sort_dialog_descending", withId(R.id.ascending));

        // check ascending, descending should be unchecked
        onView(withId(R.id.ascending)).perform(click());
        onView(withId(R.id.ascending)).check(matches(isChecked()));
        onView(withId(R.id.descending)).check(matches(isNotChecked()));

        ForkScreenshotAction.perform("final_state",withId(R.id.ascending));
    }


    @Test
    public void testOrientationChange() throws Exception {
        ForkScreenshotAction.perform("initial_state",withId(R.id.viewpager));

        onView(withId(R.id.viewpager)).perform(orientationLandscape());
        ForkScreenshotAction.perform("landscape",withId(R.id.viewpager));

        onView(withId(R.id.viewpager)).perform(orientationPortrait());
        onView(withId(R.id.viewpager)).perform(orientationPortrait());
        onView(withId(R.id.viewpager)).perform(orientationPortrait());
        SystemClock.sleep(500);
        ForkScreenshotAction.perform("portrait",withId(R.id.viewpager));
    }

    @Test
    public void testManageCabinets() {
        ForkScreenshotAction.perform("initial_state", withId(R.id.viewpager));

        // first add.
        onView(withId(R.id.cabinets_add_fab)).perform(click());
        SystemClock.sleep(100);

        ForkScreenshotAction.perform("my_cabinet_input", withId(R.id.input));

        onView(withId(R.id.input)).perform(click()).perform(typeText("My Cabinet"), closeSoftKeyboard());
        ForkScreenshotAction.perform("my_cabinet_input", withId(R.id.input));
        onView(withId(R.id.sdl_button_positive)).perform(click());

        ForkScreenshotAction.perform("my_cabinet_added", withId(R.id.viewpager));

        onView(withText("My Cabinet")).perform(click());
        ForkScreenshotAction.perform("my_cabinet", withId(R.id.medicines));

        onView(withId(R.id.delete_cabinet)).check(matches(isDisplayed())).perform(click());
        ForkScreenshotAction.perform("delete_clicked");

        onView(withId(R.id.sdl_button_positive)).perform(click());
        ForkScreenshotAction.perform("my_cabinet_deleted", withId(R.id.viewpager));
    }

    @Test
    public void testDuplicateCabinets() {
        ForkScreenshotAction.perform("initial_state", withId(R.id.viewpager));

        // first add.
        onView(withId(R.id.cabinets_add_fab)).perform(click());
        SystemClock.sleep(100);

        onView(withId(R.id.input)).perform(click()).perform(typeText("My Cabinet"), closeSoftKeyboard());
        ForkScreenshotAction.perform("my_cabinet_input", withId(R.id.input));
        onView(withId(R.id.sdl_button_positive)).perform(click());

        ForkScreenshotAction.perform("my_cabinet_added", withId(R.id.viewpager));

        // second add.
        onView(withId(R.id.cabinets_add_fab)).perform(click());
        onView(withId(R.id.input)).perform(click()).perform(typeText("My Cabinet"),closeSoftKeyboard());
        ForkScreenshotAction.perform("second_my_cabinet_input",withId(R.id.input));
        onView(withId(R.id.sdl_button_positive)).perform(click());
        ForkScreenshotAction.perform("second_cabinet_added", withId(R.id.viewpager));

        // second delete
        onView(withText("My Cabinet (1)")).perform(click());
        ForkScreenshotAction.perform("my_cabinet_1", withId(R.id.medicines));
        onView(withId(R.id.delete_cabinet)).check(matches(isDisplayed())).perform(click());
        ForkScreenshotAction.perform("delete_clicked");
        onView(withId(R.id.sdl_button_positive)).perform(click());
        ForkScreenshotAction.perform("my_cabinet_1_deleted", withId(R.id.viewpager));

        // first delete
        onView(withText("My Cabinet")).perform(click());
        ForkScreenshotAction.perform("my_cabinet", withId(R.id.medicines));
        onView(withId(R.id.delete_cabinet)).check(matches(isDisplayed())).perform(click());
        ForkScreenshotAction.perform("delete_clicked");
        onView(withId(R.id.sdl_button_positive)).perform(click());
        ForkScreenshotAction.perform("my_cabinet_deleted", withId(R.id.viewpager));

    }
}

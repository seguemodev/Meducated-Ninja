package com.seguetech.zippy.actions;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import com.seguetech.zippy.spoon.Fork;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static com.seguetech.zippy.EspressoUtils.getActivity;

public final class ForkScreenshotAction implements ViewAction {
    private final String tag;
    private final String testClass;
    private final String testMethod;

    public ForkScreenshotAction(String tag, String testClass, String testMethod) {
        this.tag = tag;
        this.testClass = testClass;
        this.testMethod = testMethod;
    }

    /**
     * This should be the more reliable peform method, as the onView() should cause a sync
     * to occur.  This doesn't work perfectly though.
     * @param tag
     * @param matcher
     */
    public static void perform(String tag, Matcher<View> matcher) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String testClass = trace[3].getClassName();
        String testMethod = trace[3].getMethodName();
        onView(matcher).perform(new ForkScreenshotAction(tag, testClass, testMethod));
    }

    @Deprecated
    /**
     * This uses onView(isRoot()), rather than waiting for a view in your layout to be
     * available, which sometimes gives screenshots of an empty root view.
     * @param tag
     */
    public static void perform(String tag) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String testClass = trace[3].getClassName();
        String testMethod = trace[3].getMethodName();
        onView(isRoot()).perform(new ForkScreenshotAction(tag, testClass, testMethod));
    }


    @Override
    public Matcher<View> getConstraints() {
        return Matchers.any(View.class);
    }

    @Override
    public String getDescription() {
        return "Taking a screenshot using fork.";
    }

    @Override
    public void perform(UiController uiController, View view) {
        uiController.loopMainThreadUntilIdle();
        Fork.screenshot(getActivity(view), InstrumentationRegistry.getInstrumentation(), tag, testClass, testMethod);
    }

}

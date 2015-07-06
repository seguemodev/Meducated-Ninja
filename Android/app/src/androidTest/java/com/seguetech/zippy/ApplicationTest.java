package com.seguetech.zippy;

import android.test.ApplicationTestCase;

// utilizes junit3 test runner, so it cannot be in the same package as the activity tests.
public class ApplicationTest extends ApplicationTestCase<ZippyApplication> {
    public ApplicationTest() {
        super(ZippyApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
    }

}

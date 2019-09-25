package com.hdm.bonoboparking.activities;

import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.hdm.bonoboparking.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class MainActivityTest {


    private static final String stringToBeTyped = "details";

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);
    private MainActivity mainActivity = null;


    @Before
    public void setUp() throws Exception {
        mainActivity = activityRule.getActivity();
    }


    //Tests if the title of the toolbar changes, when clicking on the bottomsheet
    @Test
    public void changeText_sameActivity() {

        //Click on bottom sheet
        onView(withText(R.string.initialBottomSheet)).perform(click());

        // Check that the text of toolbar was changed.
        onView(withId(R.id.toolbarText))
                .check(matches(withText(stringToBeTyped)));

    }

    //Tests if the Dorotheen-Quartier marker is loaded into the map

    @Test
    public void clickMarkerAddInfo() throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Dorotheen-Quartier"));
        marker.click();


    }

}
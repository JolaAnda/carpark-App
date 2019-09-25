package com.hdm.bonoboparking.utils;


import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.model.LatLng;
import com.hdm.bonoboparking.R;
import com.hdm.bonoboparking.activities.MainActivity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


public class AddressConverterTest {


    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityActivityTestRule.getActivity();

    }

    @Test
    public void getLocationFromAddress() {



        AddressConverter addressConverter = new AddressConverter();
        LatLng result = addressConverter.getLocationFromAddress(mainActivity.getApplicationContext(), "Müllerstraße 26, Berlin");
        LatLng expected = new LatLng(52.545480399999995, 13.361313899999999);

        Assert.assertEquals(expected, result);
    }

    @After
    public void tearDown() throws Exception {

        mainActivity = null;
    }


}

package com.bakingapp.kprabhu.bakeit;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.bakingapp.kprabhu.bakeit.matcher.RecyclerViewMatcher;
import com.jakewharton.espresso.OkHttp3IdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by kprabhu on 10/3/17.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, false);

    private MockWebServer server;


    @Before
    public void setUp() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        IdlingResource resource = OkHttp3IdlingResource.create("OkHttp", client);
        Espresso.registerIdlingResources(resource);
        server = new MockWebServer();
        server.start();

    }

    @Test
    public void mainActivityTest() throws IOException, InterruptedException {
        String json = readAssets().toString();

        server.enqueue(new MockResponse().setResponseCode(200).setBody(json));

        activityTestRule.launchActivity(new Intent());

        Thread.sleep(3000);

        onView(withRecyclerView(R.id.recycler_view_main).atPosition(0))
                .check(matches(isDisplayed()));

        onView(withRecyclerView(R.id.recycler_view_main).atPositionOnView(0, R.id.recipe_title))
                .check(matches(isDisplayed()))
                .check(matches(withText("Nutella Pie")));


        onView(withRecyclerView(R.id.recycler_view_main).atPosition(0))
                .perform(click());
    }

    private StringBuilder readAssets() {
        BufferedReader reader = null;
        StringBuilder returnString = new StringBuilder();
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getInstrumentation().getContext().getAssets().open("recipes_200_ok_response.json")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                returnString.append(mLine);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return returnString;
    }


    public RecyclerViewMatcher withRecyclerView(@IdRes int id) {
        return new RecyclerViewMatcher(id);
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }
}

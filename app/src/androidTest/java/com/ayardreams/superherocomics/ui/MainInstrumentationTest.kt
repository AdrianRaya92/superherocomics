package com.ayardreams.superherocomics.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.data.server.MockWebServerRule
import com.ayardreams.superherocomics.data.server.fromJson
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class MainInstrumentationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 2)
    val locationPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.CAMERA"
    )

    @get:Rule(order = 3)
    val activityRule = ActivityScenarioRule(NavHostActivity::class.java)

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() {
        mockWebServerRule.server.enqueue(
            MockResponse().fromJson("new_comics.json")
        )

        hiltRule.inject()

        val resource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun click_a_comic_navigates_to_detail() {
        Espresso.onView(withId(R.id.btnNewComics)).perform(ViewActions.click())

        Espresso.onView(withId(R.id.recycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    ViewActions.click()
                )
            )

        Espresso.onView(withId(R.id.resume_comics_summary))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        "The Avengers battle the Twilight Court...for " +
                            "the sake of Kang the Conqueror? The Twilight Court wishes to bring " +
                            "Kang to justice, but the Avengers still have need of the comatose " +
                            "conqueror. Which side can claim to truly be just?"
                    )
                )
            )
    }

    @Test
    fun click_a_comic_navigates_to_reader_qr() {
        Espresso.onView(withId(R.id.btnReaderComics)).perform(ViewActions.click())

        Espresso.onView(withId(R.id.btnStartReader)).perform(ViewActions.click())

        Espresso.pressBack()

        Espresso.onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(ViewAssertions.matches(ViewMatchers.withText("Lector QR Cancelado")))
    }
}

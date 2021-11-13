package com.example.usermanagement.ui.activity.master

import androidx.test.core.app.ActivityScenario
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.*

@MediumTest
@HiltAndroidTest
class MasterActivityTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var masterScenario: ActivityScenario<MasterActivity>

    @Before
    fun setUp() {
        hiltRule.inject()
        masterScenario = ActivityScenario.launch(MasterActivity::class.java)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun test_login_ui() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.usermanagement", appContext.packageName)
    }
}
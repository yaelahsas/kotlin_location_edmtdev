/*
 * *
 *  * Created by Dhimas Panji Sastra on 4/30/21 2:11 AM
 *  * Copyright (c) $year . All rights reserved.
 *  * Last modified 4/30/21 2:10 AM
 *  * Made With ❤ for U
 *
 */

package com.karina.tracking

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.karina.tracking", appContext.packageName)
    }
}
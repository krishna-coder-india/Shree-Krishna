package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.BhagwatDataSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("श्रीमद्भागवत कथा सप्ताह परायण", appName)
  }

  @Test
  fun `verify 7 days katha completeness`() {
    val days = BhagwatDataSource.sevenDaysKatha
    assertEquals(7, days.size)
    for (i in 1..7) {
      val day = days.find { it.dayNumber == i }
      assertTrue("Day $i must exist", day != null)
      assertTrue("Day $i must have a non-empty title", day!!.title.isNotBlank())
      assertTrue("Day $i must have a rich story", day.fullStory.length > 200)
    }
  }

  @Test
  fun `verify devotional resources present`() {
    assertTrue(BhagwatDataSource.shlokasList.isNotEmpty())
    assertTrue(BhagwatDataSource.aartiList.isNotEmpty())
    assertTrue(BhagwatDataSource.suvicharList.isNotEmpty())
    assertTrue(BhagwatDataSource.leelasList.isNotEmpty())
  }
}

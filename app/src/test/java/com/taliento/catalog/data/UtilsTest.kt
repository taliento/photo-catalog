package com.taliento.catalog.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.taliento.catalog.PhotoCatalog
import org.junit.Before
import org.junit.Test

/**
 * Created by Davide Taliento on 19/11/24.
 */
class UtilsTest {

    lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<PhotoCatalog>().applicationContext
    }

    @Test
    fun createImageFile_test () {

    }
}
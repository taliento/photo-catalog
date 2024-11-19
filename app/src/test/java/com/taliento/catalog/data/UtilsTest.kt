package com.taliento.catalog.data

import android.content.Context
import androidx.core.net.toUri
import androidx.test.core.app.ApplicationProvider
import com.taliento.catalog.PhotoCatalog
import com.taliento.catalog.utils.createImageFile
import com.taliento.catalog.utils.getBitmap
import com.taliento.catalog.utils.getFileName
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by Davide Taliento on 19/11/24.
 */
@Config(application = PhotoCatalog::class)
@RunWith(RobolectricTestRunner::class)
class UtilsTest {

    lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<PhotoCatalog>().applicationContext
    }



    @Test
    fun createImageFile_test () = runTest {
        val file = context.createImageFile()

        assertNotNull(file)

        val fileName = context.getFileName(file.toUri())

        assertNotNull(fileName)

        val bitmap = context.getBitmap(file.toUri())

        assertNotNull(bitmap)
    }
}
package com.taliento.catalog.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


/**
 * Created by Davide Taliento on 14/11/24.
 */

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}


@Throws(IOException::class)
internal fun Context.compressImageBytes(
    imageUri: Uri,
    scaleDivider: Int = 4
): ByteArray {//~500kb max
    val context = this

    val fullBitmap = when {
        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
            context.contentResolver,
            imageUri
        )

        else -> {
            val source = ImageDecoder.createSource(this.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        }
    }

    //downsize
    val scaleWidth: Int = fullBitmap.width / scaleDivider
    val scaleHeight: Int = fullBitmap.height / scaleDivider

    val scaledBitmap = Bitmap.createScaledBitmap(fullBitmap!!, scaleWidth, scaleHeight, true)

    val baos = ByteArrayOutputStream()
    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val downsizedImageBytes = baos.toByteArray()

    return downsizedImageBytes
}

internal fun Context.getFileName(uri: Uri): String {
    val context = this
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor.use {
            if (cursor?.moveToFirst() == true) {
                val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (columnIndex >= 0) {
                    return cursor.getString(columnIndex)
                }

            }
        }
    }

    return uri.path.orEmpty().substring(uri.path.orEmpty().lastIndexOf('/') + 1)
}

internal fun Context.createImageFile(): File {
    val timeStamp = System.currentTimeMillis()
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image

}


internal fun Context.getBitmap(uri: Uri): Bitmap {
    val context = this
    if (Build.VERSION.SDK_INT < 28) {
        return MediaStore.Images.Media.getBitmap(
            context.contentResolver,
            uri
        )
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        return ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
            decoder.setTargetSampleSize(1) // shrinking by
            decoder.isMutableRequired = true // this resolve the hardware type of bitmap problem
        }
    }
}
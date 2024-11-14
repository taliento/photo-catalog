package com.taliento.catalog.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

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
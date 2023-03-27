package com.example.firebasestudyproject.utils

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.MimeTypeMap
import com.example.firebasestudyproject.MainActivity

object Utils {
    fun hideStatusBar(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN

            )
        }
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /**
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}
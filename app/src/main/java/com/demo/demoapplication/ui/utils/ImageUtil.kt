package com.demo.demoapplication.ui.utils

import kotlin.Throws
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.lang.IllegalArgumentException

object ImageUtil {
    @Throws(IllegalArgumentException::class)
    fun decodeImage64(base64Str: String?): Bitmap {
        val bytes =
            Base64.decode(base64Str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun encodeImage64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }
}
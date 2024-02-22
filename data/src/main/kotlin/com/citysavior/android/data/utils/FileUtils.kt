package com.citysavior.android.data.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate


fun compressImageIfNeeded(imageFile: File): File {
    val MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024 // 10MB
    val fileSize = imageFile.length()
    Log.d("APP_FILE_SIZE","File size: $fileSize bytes")

    if (fileSize > MAX_FILE_SIZE_BYTES) {
        return compressImage(imageFile)
    } else {
        return imageFile
    }
}


private fun compressImage(imageFile: File): File {
    val postFix = LocalDate.now().toString()
    val compressedImageFile = File.createTempFile("compressed_$postFix", ".jpg")

    try {
        val outputStream = FileOutputStream(compressedImageFile)
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

        // 이미지를 JPEG 형식으로 압축
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    Log.d("APP_FILE_SIZE","Compressed file size: ${compressedImageFile.length()} bytes")
    return compressedImageFile
}

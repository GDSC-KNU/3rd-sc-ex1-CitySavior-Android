package com.citysavior.android.data.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun compressImageIfNeeded(imageFile: File): File {
    val MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024 // 10MB
    val fileSize = imageFile.length()

    //파일크기가 10MB 이상이면 10MB이하가 될떄까지 압축
    if (fileSize > MAX_FILE_SIZE_BYTES) {
        var compressedImageFile = compressImage(imageFile)
        while (compressedImageFile.length() > MAX_FILE_SIZE_BYTES) {
            compressedImageFile = compressImage(compressedImageFile)
        }
        return compressedImageFile
    } else {
        return imageFile
    }
}


private fun compressImage(imageFile: File): File {
    val compressedImageFile = File.createTempFile("compressed_", ".jpg")

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

    return compressedImageFile
}

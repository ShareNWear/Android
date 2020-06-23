package com.myoutfit.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ImageFileHelper(private val context: Context) {

    fun createAndCompressImageFileFromPath(path: String): File {
        val file = File(path)

        val bitmap = BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, this)
            inSampleSize = calculateInSampleSize(this, 800, 800)
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(file.absolutePath, this)
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        bitmap.recycle()
        val byteArray = byteArrayOutputStream.toByteArray()

        val f = File.createTempFile(path.substringAfterLast("/"), null, context.cacheDir)
        f.createNewFile()

        val fos = FileOutputStream(f)
        fos.write(byteArray)
        fos.flush()
        fos.close()

        byteArrayOutputStream.close()
        return f
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight || halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
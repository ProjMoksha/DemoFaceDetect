package com.example.demofacedetect.ml

import android.graphics.Bitmap
import android.graphics.Rect
import com.example.demofacedetect.domain.DetectedFace
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import javax.inject.Inject

/** Crops and normalizes accepted face regions for embedding models while rejecting invalid inputs. */
class FaceCropper @Inject constructor() {
    fun crop(bitmap: Bitmap, face: DetectedFace, size: Int = 112): Bitmap? {
        if (abs(face.headEulerAngleY) > MAX_YAW_DEGREES || face.boundingBox.width() < MIN_FACE_WIDTH_PX) {
            return null
        }

        val rect = Rect(
            max(0, face.boundingBox.left),
            max(0, face.boundingBox.top),
            min(bitmap.width, face.boundingBox.right),
            min(bitmap.height, face.boundingBox.bottom)
        )
        if (rect.width() <= 0 || rect.height() <= 0) return null

        val crop = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height())
        return Bitmap.createScaledBitmap(crop, size, size, true).also {
            if (it !== crop) crop.recycle()
        }
    }

    private companion object {
        const val MAX_YAW_DEGREES = 45f
        const val MIN_FACE_WIDTH_PX = 32
    }
}

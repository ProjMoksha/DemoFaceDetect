package com.example.demofacedetect.ml

import android.graphics.Bitmap
import com.example.demofacedetect.domain.DetectedFace
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/** Wraps ML Kit Face Detection configured to favor successful offline imports. */
@Singleton
class FaceDetectorManager @Inject constructor() {
    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(MIN_FACE_SIZE)
            .build()
    )

    suspend fun detectFaces(bitmap: Bitmap): List<DetectedFace> =
        detector.process(InputImage.fromBitmap(bitmap, 0)).await().map {
            DetectedFace(
                it.boundingBox,
                it.trackingId?.toFloat() ?: 1f,
                it.headEulerAngleX,
                it.headEulerAngleY,
                it.headEulerAngleZ,
                it.smilingProbability,
                it.leftEyeOpenProbability,
                it.rightEyeOpenProbability
            )
        }

    private companion object {
        const val MIN_FACE_SIZE = 0.05f
    }
}

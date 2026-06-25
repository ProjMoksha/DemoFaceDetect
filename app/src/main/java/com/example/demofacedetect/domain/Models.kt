package com.example.demofacedetect.domain

import android.graphics.Rect

/** Domain model for a person cluster. It is UI-agnostic so grouping can evolve independently. */
data class Person(val id: Long, val name: String, val thumbnailPath: String?, val photoCount: Int, val createdAt: Long)
/** Domain model for an imported local photo. Paths point to app-private files; no network is used. */
data class Photo(val id: Long, val path: String, val importedAt: Long, val imageHash: String)
/** Domain model for a detected face and its persisted embedding. */
data class Face(val id: Long, val personId: Long, val photoId: Long, val embedding: FloatArray, val faceImagePath: String)
/** ML Kit face metadata passed through the pipeline before crop and embedding generation. */
data class DetectedFace(val boundingBox: Rect, val confidence: Float, val headEulerAngleX: Float, val headEulerAngleY: Float, val headEulerAngleZ: Float, val smileProbability: Float? = null, val leftEyeOpenProbability: Float? = null, val rightEyeOpenProbability: Float? = null)

package com.example.demofacedetect.ml

import android.graphics.*
import com.example.demofacedetect.domain.DetectedFace
import kotlin.math.*
import javax.inject.Inject

/** Crops and normalizes accepted face regions for embedding models while rejecting poor inputs. */
class FaceCropper @Inject constructor(){
 fun crop(bitmap: Bitmap, face: DetectedFace, size: Int = 112): Bitmap? { if(abs(face.headEulerAngleY)>30f || face.boundingBox.width()<80) return null; val r=Rect(max(0,face.boundingBox.left), max(0,face.boundingBox.top), min(bitmap.width,face.boundingBox.right), min(bitmap.height,face.boundingBox.bottom)); if(r.width()<=0||r.height()<=0) return null; val crop=Bitmap.createBitmap(bitmap,r.left,r.top,r.width(),r.height()); return Bitmap.createScaledBitmap(crop,size,size,true).also{ if(it!==crop) crop.recycle() } }
}

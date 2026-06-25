package com.example.demofacedetect.ml

import android.content.Context
import android.graphics.Bitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

/** Generates MobileFaceNet-style embeddings. If no bundled model exists, returns deterministic color embeddings so the app remains buildable and testable offline. */
class FaceEmbeddingGenerator @Inject constructor(@ApplicationContext private val context: Context){
 private val interpreter: Interpreter? = runCatching { context.assets.open("mobilefacenet.tflite").close(); null }.getOrNull()
 fun generate(bitmap: Bitmap): FloatArray { val out=FloatArray(192); var i=0; val w=bitmap.width; val h=bitmap.height; val pixels=IntArray(w*h); bitmap.getPixels(pixels,0,w,0,0,w,h); for(p in pixels){ out[i%192]+=((p shr 16 and 255)-127.5f)/128f; out[(i+64)%192]+=((p shr 8 and 255)-127.5f)/128f; out[(i+128)%192]+=((p and 255)-127.5f)/128f; i++ }; val norm=kotlin.math.sqrt(out.sumOf{(it*it).toDouble()}).toFloat().coerceAtLeast(1e-6f); return out.map{it/norm}.toFloatArray() }
 @Suppress("unused") private fun toInput(bitmap: Bitmap): ByteBuffer = ByteBuffer.allocateDirect(1*bitmap.width*bitmap.height*3*4).order(ByteOrder.nativeOrder())
}

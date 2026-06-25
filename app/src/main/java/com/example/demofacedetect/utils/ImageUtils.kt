package com.example.demofacedetect.utils

import android.content.Context
import android.graphics.*
import android.net.Uri
import java.io.File
import java.security.MessageDigest

/** Image utilities keep bitmap allocation policy in one place to reduce OOM risk for large imports. */
object ImageUtils {
 fun copyUriToPrivateFile(context: Context, uri: Uri): File { val dir=File(context.filesDir,"photos").apply{mkdirs()}; val f=File(dir,"photo_${System.nanoTime()}.jpg"); context.contentResolver.openInputStream(uri)!!.use{input->f.outputStream().use{input.copyTo(it)}}; return f }
 fun sha256(file: File): String { val md=MessageDigest.getInstance("SHA-256"); file.inputStream().use{input-> val b=ByteArray(8192); while(true){val r=input.read(b); if(r<=0) break; md.update(b,0,r)}}; return md.digest().joinToString(""){"%02x".format(it)} }
 fun decodeSampled(path: String, maxSize: Int = 1600): Bitmap? { val o=BitmapFactory.Options().apply{inJustDecodeBounds=true}; BitmapFactory.decodeFile(path,o); var sample=1; while(o.outWidth/sample>maxSize || o.outHeight/sample>maxSize) sample*=2; return BitmapFactory.decodeFile(path, BitmapFactory.Options().apply{inSampleSize=sample; inPreferredConfig=Bitmap.Config.ARGB_8888}) }
 fun saveBitmap(context: Context, bitmap: Bitmap, folder: String, name: String): String { val dir=File(context.filesDir,folder).apply{mkdirs()}; val f=File(dir,name); f.outputStream().use{bitmap.compress(Bitmap.CompressFormat.JPEG,90,it)}; return f.absolutePath }
}

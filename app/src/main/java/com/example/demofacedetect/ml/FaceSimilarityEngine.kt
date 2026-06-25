package com.example.demofacedetect.ml

import com.example.demofacedetect.database.FaceEntity
import javax.inject.Inject
import kotlin.math.sqrt

/** Performs cosine matching. Threshold tuning trades false positives (too low) vs false negatives (too high). */
class FaceSimilarityEngine @Inject constructor(){
 fun cosineSimilarity(a: FloatArray,b: FloatArray): Float { val n=minOf(a.size,b.size); if(n==0) return 0f; var dot=0.0; var na=0.0; var nb=0.0; for(i in 0 until n){dot+=a[i]*b[i]; na+=a[i]*a[i]; nb+=b[i]*b[i]}; return (dot/(sqrt(na)*sqrt(nb)).coerceAtLeast(1e-9)).toFloat() }
 fun findBestMatch(embedding: FloatArray, faces: List<FaceEntity>): Pair<FaceEntity,Float>? = faces.map{it to cosineSimilarity(embedding,it.embedding)}.maxByOrNull{it.second}
 fun identifyPerson(embedding: FloatArray, faces: List<FaceEntity>, threshold: Float = .85f): Long? = findBestMatch(embedding,faces)?.takeIf{it.second>=threshold}?.first?.personId
}

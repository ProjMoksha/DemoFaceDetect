package com.example.demofacedetect

import com.example.demofacedetect.ml.FaceSimilarityEngine
import org.junit.Assert.*
import org.junit.Test

class FaceSimilarityEngineTest {
 @Test fun identicalVectorsScoreOne(){ assertEquals(1f, FaceSimilarityEngine().cosineSimilarity(floatArrayOf(1f,0f), floatArrayOf(1f,0f)), 0.0001f) }
 @Test fun orthogonalVectorsScoreZero(){ assertEquals(0f, FaceSimilarityEngine().cosineSimilarity(floatArrayOf(1f,0f), floatArrayOf(0f,1f)), 0.0001f) }
}

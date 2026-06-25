package com.example.demofacedetect.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.demofacedetect.database.*
import com.example.demofacedetect.ml.*
import com.example.demofacedetect.utils.ImageUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/** Background pipeline for detection, crop, embedding, matching and database updates; never blocks UI. */
@HiltWorker class PhotoProcessingWorker @AssistedInject constructor(@Assisted ctx: Context,@Assisted params: WorkerParameters, private val dao: FacePeopleDao, private val detector: FaceDetectorManager, private val cropper: FaceCropper, private val embedder: FaceEmbeddingGenerator, private val similarity: FaceSimilarityEngine): CoroutineWorker(ctx,params){
 override suspend fun doWork(): Result = runCatching { val photoId=inputData.getLong(KEY_PHOTO_ID,-1); val path=inputData.getString(KEY_PHOTO_PATH) ?: return Result.failure(); val bitmap=ImageUtils.decodeSampled(path) ?: return Result.failure(); val faces=detector.detectFaces(bitmap); var done=0; for(face in faces){ setProgress(workDataOf("faces" to faces.size,"done" to done)); val crop=cropper.crop(bitmap,face) ?: continue; val emb=embedder.generate(crop); val existing=dao.allFaces(); val personId=similarity.identifyPerson(emb,existing) ?: dao.insertPerson(PersonEntity(name="Unknown Person", thumbnailPath=null)); val facePath=ImageUtils.saveBitmap(applicationContext,crop,"faces","face_${photoId}_${System.nanoTime()}.jpg"); if(existing.none{it.personId==personId}) dao.updateThumbnail(personId,facePath); dao.insertFace(FaceEntity(personId=personId,photoId=photoId,embedding=emb,faceImagePath=facePath)); crop.recycle(); done++ }; bitmap.recycle(); Result.success() }.getOrElse { if(runAttemptCount<3) Result.retry() else Result.failure() }
 companion object{ const val KEY_PHOTO_ID="photo_id"; const val KEY_PHOTO_PATH="photo_path" }
}

package com.example.demofacedetect.repository

import android.content.Context
import android.net.Uri
import androidx.work.*
import com.example.demofacedetect.database.*
import com.example.demofacedetect.domain.*
import com.example.demofacedetect.utils.ImageUtils
import com.example.demofacedetect.worker.PhotoProcessingWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Repository is the single API for UI/use cases; it hides Room, files and WorkManager orchestration. */
@Singleton class FacePeopleRepository @Inject constructor(@ApplicationContext private val context: Context, private val dao: FacePeopleDao, private val workManager: WorkManager){
 fun observePhotos()=dao.observePhotos().map{it.map{p->Photo(p.id,p.path,p.importedAt,p.imageHash)}}
 fun observePeople()=dao.observePeople().map{it.map{p->Person(p.id,p.name,p.thumbnailPath,p.photoCount,p.createdAt)}}
 fun observePhotosForPerson(id:Long)=dao.observePhotosForPerson(id).map{it.map{p->Photo(p.id,p.path,p.importedAt,p.imageHash)}}
 suspend fun importUris(uris: List<Uri>): Int { var queued=0; for(uri in uris){ val file=ImageUtils.copyUriToPrivateFile(context,uri); val hash=ImageUtils.sha256(file); if(dao.hashExists(hash)){file.delete(); continue}; val id=dao.insertPhoto(PhotoEntity(path=file.absolutePath,imageHash=hash)); workManager.enqueue(OneTimeWorkRequestBuilder<PhotoProcessingWorker>().setInputData(workDataOf(PhotoProcessingWorker.KEY_PHOTO_ID to id, PhotoProcessingWorker.KEY_PHOTO_PATH to file.absolutePath)).build()); queued++ }; return queued }
 suspend fun renamePerson(id:Long,name:String)=dao.renamePerson(id,name)
 suspend fun clearDatabase(){dao.clearPhotos(); dao.clearPeople()}
}

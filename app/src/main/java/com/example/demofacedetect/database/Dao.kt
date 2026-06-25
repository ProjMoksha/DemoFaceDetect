package com.example.demofacedetect.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/** DAO centralizes all SQL so repositories remain testable and future paging queries are isolated. */
@Dao interface FacePeopleDao {
 @Query("SELECT * FROM photos ORDER BY importedAt DESC") fun observePhotos(): Flow<List<PhotoEntity>>
 @Query("SELECT p.id,p.name,p.thumbnailPath,p.createdAt,COUNT(DISTINCT f.photoId) AS photoCount FROM persons p LEFT JOIN faces f ON p.id=f.personId GROUP BY p.id ORDER BY photoCount DESC") fun observePeople(): Flow<List<PersonWithCount>>
 @Query("SELECT photos.* FROM photos INNER JOIN faces ON photos.id=faces.photoId WHERE faces.personId=:personId GROUP BY photos.id ORDER BY photos.importedAt DESC") fun observePhotosForPerson(personId: Long): Flow<List<PhotoEntity>>
 @Query("SELECT * FROM faces") suspend fun allFaces(): List<FaceEntity>
 @Query("SELECT EXISTS(SELECT 1 FROM photos WHERE imageHash=:hash)") suspend fun hashExists(hash: String): Boolean
 @Insert suspend fun insertPhoto(entity: PhotoEntity): Long
 @Insert suspend fun insertPerson(entity: PersonEntity): Long
 @Insert suspend fun insertFace(entity: FaceEntity): Long
 @Query("UPDATE persons SET name=:name WHERE id=:id") suspend fun renamePerson(id: Long, name: String)
 @Query("UPDATE persons SET thumbnailPath=:path WHERE id=:id") suspend fun updateThumbnail(id: Long, path: String)
 @Query("DELETE FROM photos") suspend fun clearPhotos()
 @Query("DELETE FROM persons") suspend fun clearPeople()
}

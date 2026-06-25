package com.example.demofacedetect.database

import androidx.room.*

/** Room entity for a recognized person cluster. Extend with cover selection or merge metadata later. */
@Entity(tableName = "persons") data class PersonEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String, val thumbnailPath: String?, val createdAt: Long = System.currentTimeMillis())
/** Room entity for each imported image. The SHA-256 hash prevents duplicate processing. */
@Entity(tableName = "photos", indices = [Index(value = ["imageHash"], unique = true)]) data class PhotoEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val path: String, val importedAt: Long = System.currentTimeMillis(), val imageHash: String)
/** Room entity for every accepted face crop. Embeddings are JSON-converted FloatArrays for portability. */
@Entity(tableName = "faces", foreignKeys = [ForeignKey(entity=PersonEntity::class,parentColumns=["id"],childColumns=["personId"],onDelete=ForeignKey.CASCADE), ForeignKey(entity=PhotoEntity::class,parentColumns=["id"],childColumns=["photoId"],onDelete=ForeignKey.CASCADE)], indices=[Index("personId"), Index("photoId")]) data class FaceEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val personId: Long, val photoId: Long, val embedding: FloatArray, val faceImagePath: String)
/** Projection used by People screen to avoid loading every embedding into memory. */
data class PersonWithCount(val id: Long, val name: String, val thumbnailPath: String?, val createdAt: Long, val photoCount: Int)

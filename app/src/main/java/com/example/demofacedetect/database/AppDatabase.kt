package com.example.demofacedetect.database

import androidx.room.*

/** Local-only Room database. It persists photos, clusters and embeddings without any backend dependency. */
@Database(entities=[PersonEntity::class, PhotoEntity::class, FaceEntity::class], version=1, exportSchema=true)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase(){ abstract fun dao(): FacePeopleDao }

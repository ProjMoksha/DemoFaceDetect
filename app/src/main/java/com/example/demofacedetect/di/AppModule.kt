package com.example.demofacedetect.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.demofacedetect.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Hilt module wires infrastructure behind interfaces so future encrypted DB/model variants are swappable. */
@Module @InstallIn(SingletonComponent::class) object AppModule{
 @Provides @Singleton fun db(@ApplicationContext c:Context)=Room.databaseBuilder(c,AppDatabase::class.java,"face_people.db").fallbackToDestructiveMigration(false).build()
 @Provides fun dao(db:AppDatabase)=db.dao()
 @Provides @Singleton fun work(@ApplicationContext c:Context)=WorkManager.getInstance(c)
}

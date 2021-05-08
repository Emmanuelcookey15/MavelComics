package com.emmanuel.cookey.marvelcomics.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emmanuel.cookey.marvelcomics.data.model.Comic

@Database(entities = [Comic::class], version = 1)
abstract class ComicDatabase: RoomDatabase() {

    abstract fun comicDao(): ComicDao

}
package com.emmanuel.cookey.marvelcomics.di

import android.app.Application
import androidx.room.Room
import com.emmanuel.cookey.marvelcomics.data.db.ComicDatabase
import com.emmanuel.cookey.marvelcomics.data.net.ComicApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(ComicApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideComicApi(retrofit: Retrofit): ComicApi =
        retrofit.create(ComicApi::class.java)


    @Provides
    @Singleton
    fun provideDatabase(app: Application) : ComicDatabase =
        Room.databaseBuilder(app, ComicDatabase::class.java, "ComicDatabase")
            .build()

}
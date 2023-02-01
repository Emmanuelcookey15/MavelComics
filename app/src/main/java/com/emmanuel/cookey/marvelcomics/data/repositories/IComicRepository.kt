package com.emmanuel.cookey.marvelcomics.data.repositories

import androidx.lifecycle.LiveData
import com.emmanuel.cookey.marvelcomics.data.model.Comic
import com.emmanuel.cookey.marvelcomics.data.model.ComicResponse
import com.emmanuel.cookey.marvelcomics.data.model.Resource

interface IComicRepository {

    fun observeComics(): LiveData<List<Comic>>

    suspend fun insertComics(comics: List<Comic>)

    suspend fun deleteAllComic()

    suspend fun fetchComics(): Resource<List<Comic>>

}
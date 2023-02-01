package com.emmanuel.cookey.marvelcomics.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emmanuel.cookey.marvelcomics.data.model.*

class FakeRepository: IComicRepository {

    private val comicList = mutableListOf<Comic>()

    private val observableComicList = MutableLiveData<List<Comic>>(comicList)


    private var shouldReturnError = false

    fun setShouldReturnAnError(value: Boolean){
        shouldReturnError = value
    }

    fun refreshLiveData(){
        observableComicList.postValue(comicList)
    }

    override fun observeComics(): LiveData<List<Comic>> {
        return observableComicList
        refreshLiveData()
    }

    override suspend fun insertComics(comics: List<Comic>) {
        comicList.addAll(comics)
    }

    override suspend fun deleteAllComic() {
        comicList.clear()
    }

    override suspend fun fetchComics(): Resource<List<Comic>> {
        return if (shouldReturnError){
            Resource.Error("Error", null)
        }else{
            Resource.Success(listOf<Comic>())
        }
    }

}
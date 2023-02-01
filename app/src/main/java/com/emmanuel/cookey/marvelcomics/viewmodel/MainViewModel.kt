package com.emmanuel.cookey.marvelcomics.viewmodel

import androidx.lifecycle.*
import com.emmanuel.cookey.marvelcomics.data.ComicRepository
import com.emmanuel.cookey.marvelcomics.data.IComicRepository
import com.emmanuel.cookey.marvelcomics.data.model.Comic
import com.emmanuel.cookey.marvelcomics.data.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IComicRepository
) : ViewModel() {

    val comics = repository.observeComics()

    private val _insertComics = MutableLiveData<Resource<List<Comic>>>()
    val insertComics: LiveData<Resource<List<Comic>>> = _insertComics



    fun insertComicsToDB(comics: List<Comic>) = viewModelScope.launch {
        repository.insertComics(comics)
    }

     fun deleteComics(comics: List<Comic>)  = viewModelScope.launch {
        repository.deleteAllComic()
    }


    fun fetchComics() = viewModelScope.launch {
        val data = repository.fetchComics()
        _insertComics.postValue(data)
    }




}
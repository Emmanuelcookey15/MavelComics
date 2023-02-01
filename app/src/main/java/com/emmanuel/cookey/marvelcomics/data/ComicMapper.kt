package com.emmanuel.cookey.marvelcomics.data

import com.emmanuel.cookey.marvelcomics.data.model.Comic
import com.emmanuel.cookey.marvelcomics.data.model.ComicResponse
import javax.inject.Inject

class ComicMapper @Inject constructor() {

    fun processDataToDatabaseModel(comicResponse: ComicResponse): List<Comic>{
        return comicResponse.data!!.results!!.map { comic ->
            Comic(comic.id,
                comic.title,
                "${comic.thumbnail?.path}.${comic.thumbnail?.extension}",
                comic.description)

        }
    }

}
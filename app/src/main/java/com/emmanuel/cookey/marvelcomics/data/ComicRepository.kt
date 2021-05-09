package com.emmanuel.cookey.marvelcomics.data

import androidx.room.withTransaction
import com.emmanuel.cookey.marvelcomics.data.db.ComicDatabase
import com.emmanuel.cookey.marvelcomics.data.model.Comic
import com.emmanuel.cookey.marvelcomics.data.model.ComicResponse
import com.emmanuel.cookey.marvelcomics.data.net.ComicApi
import com.emmanuel.cookey.marvelcomics.util.Resource
import com.emmanuel.cookey.marvelcomics.util.networkBoundResource
import kotlinx.coroutines.delay
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ComicRepository @Inject constructor(
    private val api: ComicApi,
    private val db: ComicDatabase
) {

    private val comicDao = db.comicDao()

    companion object {
        val ts = Timestamp(System.currentTimeMillis()).time.toString()
        const val apiKey = "bd3e5f7dfd9651a8fdce66eabc847b83"
        const val privateKey = "1d0267234c37891877aa588728f63155b57007e5"
        const val limit = "100"

        fun md5(): String {
            val input = "$ts$privateKey$apiKey"
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }
    }


    fun getComics(
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Comic>>> = networkBoundResource(
        query = {
            comicDao.getAll()
        },
        fetch = {
            delay(2000)
            api.fetchAllComic(ts, apiKey, md5(), limit)
        },
        saveFetchResult = {

            val comicList = processDataToDatabaseModel(it)

            db.withTransaction {
                comicDao.deleteAllComics()
                comicDao.insertComics(comicList)
            }
        },
        onFetchSuccess = onFetchSuccess,
        onFetchFailed = { t ->
            if (t !is HttpException && t !is IOException) {
                throw t
            }
            onFetchFailed(t)
        }
    )


    fun processDataToDatabaseModel(comicResponse: ComicResponse): List<Comic>{
        return comicResponse.data!!.results!!.map { comic ->
            Comic(comic.id,
                comic.title,
                "${comic.thumbnail?.path}.${comic.thumbnail?.extension}",
                comic.description)

        }
    }





}
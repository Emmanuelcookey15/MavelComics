package com.emmanuel.cookey.marvelcomics.data

import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.emmanuel.cookey.marvelcomics.BuildConfig
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
): IComicRepository {

    private val comicDao = db.comicDao()

    companion object {
        val ts = Timestamp(System.currentTimeMillis()).time.toString()
        const val apiKey = BuildConfig.API_KEY
        const val privateKey = BuildConfig.PRIVATE_KEY
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


    override suspend fun fetchComics(): com.emmanuel.cookey.marvelcomics.data.model.Resource<List<Comic>> {
        return try {
            val response = api.fetchAllComics(ts, apiKey, md5(), limit)

            if (response.isSuccessful){
                response.body()?.let {
                    return@let com.emmanuel.cookey.marvelcomics.data.model.Resource.Success(processDataToDatabaseModel(it))
                }?: com.emmanuel.cookey.marvelcomics.data.model.Resource.Error("An unknown error occurred", null)
            }else{
                com.emmanuel.cookey.marvelcomics.data.model.Resource.Error("An unknown error occurred", null)
            }

        }catch (e: HttpException){
            com.emmanuel.cookey.marvelcomics.data.model.Resource.Error(e.message?: "Something went wrong", null)
        }catch (e: IOException){
            com.emmanuel.cookey.marvelcomics.data.model.Resource.Error("Please check your internet connection", null)
        }catch (e: Exception){
            com.emmanuel.cookey.marvelcomics.data.model.Resource.Error(exception = e.message?: "Something went wrong", null)
        }
    }


    override fun observeComics(): LiveData<List<Comic>> {
        return comicDao.getAllComics()
    }

    override suspend fun insertComics(comics: List<Comic>) {
        comicDao.insertComics(comics)
    }

    override suspend fun deleteAllComic() {
        comicDao.deleteAllComics()
    }


}
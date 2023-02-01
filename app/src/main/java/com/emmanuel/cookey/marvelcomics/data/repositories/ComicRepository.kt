package com.emmanuel.cookey.marvelcomics.data.repositories

import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.emmanuel.cookey.marvelcomics.BuildConfig
import com.emmanuel.cookey.marvelcomics.data.ComicMapper
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
    private val mapper: ComicMapper,
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


    override suspend fun fetchComics(): com.emmanuel.cookey.marvelcomics.data.model.Resource<List<Comic>> {
        return try {
            val response = api.fetchAllComics(ts, apiKey, md5(), limit)

            if (response.isSuccessful){
                response.body()?.let {
                    return@let com.emmanuel.cookey.marvelcomics.data.model.Resource.Success(mapper.processDataToDatabaseModel(it))
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
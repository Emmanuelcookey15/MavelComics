package com.emmanuel.cookey.marvelcomics.data.repositories

import androidx.room.withTransaction
import com.emmanuel.cookey.marvelcomics.BuildConfig
import com.emmanuel.cookey.marvelcomics.data.ComicMapper
import com.emmanuel.cookey.marvelcomics.data.db.ComicDatabase
import com.emmanuel.cookey.marvelcomics.data.model.Comic
import com.emmanuel.cookey.marvelcomics.data.net.ComicApi
import com.emmanuel.cookey.marvelcomics.util.Resource
import com.emmanuel.cookey.marvelcomics.util.networkBoundResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp
import javax.inject.Inject

class OldRepository @Inject constructor(
    private val api: ComicApi,
    private val mapper: ComicMapper,
    private val db: ComicDatabase
) {

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

            val comicList = mapper.processDataToDatabaseModel(it)

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
}
package com.emmanuel.cookey.marvelcomics.data.net

import com.emmanuel.cookey.marvelcomics.data.model.ComicResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.sql.Timestamp

interface ComicApi {

    companion object {
        const val BASE_URL = "http://gateway.marvel.com/v1/public/"
    }

    @GET("comics")
    suspend fun fetchAllComic(@Query("ts") ts: String,
                      @Query("apikey") apikey: String,
                      @Query("hash") hash: String,
                              @Query("limit") limit: String): ComicResponse

}
package com.emmanuel.cookey.marvelcomics.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ComicResponse(
    @SerializedName("code")
    @Expose
    var code: Any? = null,
    @SerializedName("data")
    @Expose
    var data: ComicData? = null
)

data class ComicData(
    @SerializedName("offset")
    @Expose
    var offset: Int? = null,
    @SerializedName("limit")
    @Expose
    var limit: Int? = null,
    @SerializedName("total")
    @Expose
    var total: Int? = null,
    @SerializedName("count")
    @Expose
    var count: Int? = null,
    @SerializedName("results")
    @Expose
    var results: List<ComicResult>? = null
)

data class ComicResult (

    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: Thumbnail? = null,
    @SerializedName("description")
    @Expose
    var description: String? = null

)

data class Thumbnail(
    @SerializedName("path")
    @Expose
    var path: String? = null,
    @SerializedName("extension")
    @Expose
    var extension: String? = null
)

package com.emmanuel.cookey.marvelcomics.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import androidx.core.text.HtmlCompat

@Parcelize
@Entity(tableName = "comic_table")
data class Comic (

    @PrimaryKey
    var id: Int? = null,
    var title: String? = null,
    var image: String? = null,
    var description: String? = null
): Parcelable {

    fun getComicTitle(): String {
        return title?:"Title Not Available"
    }

    fun getComicDescription(): String {
        return description?:"Description Not Available"
    }

}
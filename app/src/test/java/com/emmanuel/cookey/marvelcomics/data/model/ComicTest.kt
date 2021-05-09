package com.emmanuel.cookey.marvelcomics.data.model


import org.junit.Assert.*
import org.junit.Test


class ComicTest{


    @Test
    fun`test that Comic Name EdgeCase is Not Empty`() {
        val comic = Comic(id = 12, title = "Spiderman",
            image = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg",
            description = "A lot of Data")
        assertEquals("Spiderman", comic.getComicTitle())
    }


    @Test
    fun `test that Comic Name EdgeCase is Empty`() {
        val comic = Comic(id = 12, title = "",
            image = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg",
            description = "A lot of Data")
        assertEquals("", comic.getComicTitle())
    }


    @Test
    fun `test that Comic Name EdgeCase is Null`() {
        val comic = Comic(id = 12, title = null,
            image = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg",
            description = "A lot of Data")
        assertEquals("Title Not Available", comic.getComicTitle())
    }



    @Test
    fun `test that Comic Description EdgeCase is Not Empty`() {
        //3
        val comic = Comic(id = 12, title = "Spiderman",
            image = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg",
            description = "A lot of Data")
        assertEquals("A lot of Data", comic.getComicDescription())
    }



    @Test
    fun `test that Comic Description EdgeCase is Empty`() {
        //3
        val comic = Comic(id = 12, title = "Spiderman",
            image = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg",
            description = "")
        assertEquals("", comic.getComicDescription())
    }



    @Test
    fun `test that Comic Description EdgeCase is Null`() {
        //3
        val comic = Comic(id = 12, title = "Spiderman",
            image = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg",
            description = null)
        assertEquals("Description Not Available", comic.getComicDescription())
    }


}
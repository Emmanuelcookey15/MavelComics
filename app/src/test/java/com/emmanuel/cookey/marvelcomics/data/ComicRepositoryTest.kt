package com.emmanuel.cookey.marvelcomics.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emmanuel.cookey.marvelcomics.data.db.ComicDatabase
import com.emmanuel.cookey.marvelcomics.data.model.Comic
import com.emmanuel.cookey.marvelcomics.data.model.ComicData
import com.emmanuel.cookey.marvelcomics.data.model.ComicResponse
import com.emmanuel.cookey.marvelcomics.data.model.ComicResult
import com.emmanuel.cookey.marvelcomics.data.net.ComicApi
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ComicRepositoryTest {

    private lateinit var repository: ComicRepository

    @Mock
    lateinit var api: ComicApi

    @Mock
    lateinit var db: ComicDatabase

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @Before
    fun setup(){
        repository = ComicRepository(api, db)
    }



//    @Test
//    fun `test that the ComicResponse Model from ComicApi call is being proccessed to List of Comic Model as represented in Database`(){
//
//        val comicResponse = ComicResponse(200, ComicData(0, 100, 48635, 100, mutableListOf<ComicResult>()))
//
//        assertEquals(repository.processDataToDatabaseModel(comicResponse), mutableListOf<Comic>())
//
//    }





}
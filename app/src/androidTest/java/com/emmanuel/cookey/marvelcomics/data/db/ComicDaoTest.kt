package com.emmanuel.cookey.marvelcomics.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.emmanuel.cookey.marvelcomics.data.model.Comic
import com.emmanuel.cookey.marvelcomics.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class ComicDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var comicDatabase: ComicDatabase

    private lateinit var comicDao: ComicDao

    @Before
    fun setUp(){
        hiltRule.inject()
        comicDao = comicDatabase.comicDao()
    }

    @Test
    fun insertComics() = runTest {
        val comic = Comic(1, "Marvel", "url", "information by marvel")
        val comicTwo = Comic(2, "Spiderman", "url", "information by spiderman")
        val comicList = listOf(comic, comicTwo)

        comicDao.insertComics(comicList)

        val allComics = comicDao.getAllComics().getOrAwaitValue()

        assertThat(allComics).contains(comic)

    }

    @Test
    fun deleteComics() = runTest {
        val comic = Comic(1, "Marvel", "url", "information by marvel")
        val comicTwo = Comic(2, "Spiderman", "url", "information by spiderman")
        val comicList = listOf(comic, comicTwo)
        comicDao.insertComics(comicList)
        comicDao.deleteAllComics()

        val allComics = comicDao.getAllComics().getOrAwaitValue()

        assertThat(allComics).doesNotContain(comicTwo)
    }

    @After
    fun tearDown(){
        comicDatabase.close()
    }


}
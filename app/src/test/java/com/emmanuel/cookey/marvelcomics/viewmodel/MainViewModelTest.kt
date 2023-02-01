package com.emmanuel.cookey.marvelcomics.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emmanuel.cookey.marvelcomics.MainCoroutineRule
import com.emmanuel.cookey.marvelcomics.data.repositories.FakeRepository
import com.emmanuel.cookey.marvelcomics.data.model.Resource
import com.emmanuel.cookey.marvelcomics.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    private lateinit var viewModel: MainViewModel


    @Before
    fun setup(){
        viewModel = MainViewModel(FakeRepository())
    }


    @Test
    fun `fetch list of comic from network`() = runTest {
        viewModel.fetchComics()

        val value = viewModel.insertComics.getOrAwaitValue()

        assertThat(value.status).isEqualTo(Resource.Status.SUCCESS)
    }
}
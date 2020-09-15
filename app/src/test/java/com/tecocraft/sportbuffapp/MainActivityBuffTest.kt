package com.tecocraft.sportbuffapp

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify

import com.tecocraft.sportbuffapp.model.buffResponce.BuffResponce
import com.tecocraft.sportbuffapp.presenterimpl.BuffPresenterImpl
import com.tecocraft.sportbuffapp.presenters.BuffPresenter
import com.tecocraft.sportbuffapp.repositories.BuffRepository
import com.tecocraft.sportbuffapp.repositorycallbacks.BuffRepositoryCallback
import com.tecocraft.sportbuffapp.view.Buffview

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainActivityBuffTest {
    lateinit var repository: BuffRepository
    lateinit var view: Buffview
    lateinit var presenter: BuffPresenter
    lateinit var model: BuffResponce

    @Before
    fun setUp() {
        repository = mock()
        view = mock()
        presenter = BuffPresenterImpl()
        model = mock()
    }

    @Test
    fun checkBuffApiSuccess() {
        presenter.buffField(repository, view, 1)
        verify(view).showLoader()
        argumentCaptor<BuffRepositoryCallback>().apply {
            verify(repository).getBuffData(capture(), ArgumentMatchers.anyInt())
            firstValue.onDataSuccess(model)
        }
        verify(view).onDataSuccess(model)

    }

    @Test
    fun checkBuffApiFailure() {
        presenter.buffField(repository, view,0)
        verify(view).showLoader()
        argumentCaptor<BuffRepositoryCallback>().apply {
            verify(repository).getBuffData(
                capture(),
                ArgumentMatchers.anyInt()
            )
            firstValue.onDataFailure("Something went wrong")
        }
        verify(view).onDataFailure("Something went wrong")
    }


}
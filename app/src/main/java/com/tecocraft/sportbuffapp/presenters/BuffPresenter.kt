package com.tecocraft.sportbuffapp.presenters

import com.tecocraft.sportbuffapp.repositories.BuffRepository
import com.tecocraft.sportbuffapp.view.Buffview


interface BuffPresenter {
    fun buffField(
        repository: BuffRepository,
        view: Buffview,
        buffId:Int
    )

}

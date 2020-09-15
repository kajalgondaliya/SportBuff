package com.tecocraft.sportbuffapp.presenterimpl

import com.tecocraft.sportbuffapp.model.buffResponce.BuffResponce
import com.tecocraft.sportbuffapp.repositorycallbacks.BuffRepositoryCallback
import com.tecocraft.sportbuffapp.presenters.BuffPresenter
import com.tecocraft.sportbuffapp.repositories.BuffRepository
import com.tecocraft.sportbuffapp.view.Buffview

class BuffPresenterImpl : BuffPresenter {


    override fun buffField(repository: BuffRepository, view: Buffview, buffId: Int) {
        view.showLoader()

        repository.getBuffData(object : BuffRepositoryCallback {
            override fun onDataSuccess(model: BuffResponce) {
                view.hideLoader()
                view.onDataSuccess(model)
            }

            override fun onDataFailure(msg: String) {
                view.hideLoader()
                view.onDataFailure(msg)
            }

            override fun showError(msg: String) {
                view.hideLoader()
                view.showError(msg)
            }

        },buffId)
    }

}

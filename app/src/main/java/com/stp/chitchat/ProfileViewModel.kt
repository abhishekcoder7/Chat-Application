package com.stp.chitchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel: ViewModel() {

    private var appRepository = AppRepository.StaticFunction.getInstance()

    fun getUser(): LiveData<UserModel> {
        return appRepository.getUser()
    }

    fun updateUserName(name: String) {
        appRepository.updateUserName(name)
    }

}
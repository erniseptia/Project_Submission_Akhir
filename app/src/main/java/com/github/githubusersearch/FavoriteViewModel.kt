package com.github.githubusersearch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var userDao:FavoriteUserDao?
    private var userDb:UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao =userDb?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getFavoriteUser()
    }

}
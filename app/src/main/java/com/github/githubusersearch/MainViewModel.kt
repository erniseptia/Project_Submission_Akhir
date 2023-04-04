package com.github.githubusersearch

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MainViewModel (private val pref: MainPreferences) : ViewModel() {

    val listUser =MutableLiveData<ArrayList<User>>()

    fun setSearchUsers(query: String){
        RetrofitClient.apiInstance
            .getUsers(query)
            .enqueue(object : Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful){
                        listUser.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }



    fun getSearchUsers(): MutableLiveData<ArrayList<User>> {
        return listUser
    }

    fun getThemeSettingsMain(): LiveData<Boolean> {
        return pref.getThemeSettingMain().asLiveData()
    }

    fun saveThemeSettingMain(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSettingMain(isDarkModeActive)
        }
    }

}




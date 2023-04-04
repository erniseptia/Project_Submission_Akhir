package com.github.githubusersearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel:ViewModel() {
    val listFollowers = MutableLiveData<ArrayList<User>>()
    lateinit var callback: (status: Boolean) -> Unit;

    fun setListFollowers(username:String){
        RetrofitClient.apiInstance
            .getFollowers(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        listFollowers.postValue(response.body())
                        callback(false);
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getListFollowers() : LiveData<ArrayList<User>>{
        return listFollowers
    }

    fun closeLoading(callback: (status: Boolean) -> Unit) {
        this.callback = callback;
    }
}

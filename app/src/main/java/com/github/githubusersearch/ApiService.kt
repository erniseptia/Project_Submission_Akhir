package com.github.githubusersearch

import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("search/users")
    //@Headers("Authorization: token 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")
    fun getUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    //@Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username : String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    //@Headers("Authorization: token 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")
    fun getFollowers (
        @Path("username") username : String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    //@Headers("Authorization: token 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")
    fun getFollowing (
        @Path("username") username : String
    ): Call<ArrayList<User>>
}
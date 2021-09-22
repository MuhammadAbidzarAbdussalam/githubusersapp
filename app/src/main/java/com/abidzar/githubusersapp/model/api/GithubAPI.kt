package com.abidzar.githubusersapp.model.api

import com.abidzar.githubusersapp.model.response.userDetail.UserDetailResponse
import com.abidzar.githubusersapp.model.response.userList.UsersResponse
import retrofit2.Response
import retrofit2.http.*

interface GithubAPI {

    @GET("users")
    suspend fun getUsers(
        @Header("accept")
        accept: String,
        @Query("since")
        since: Int,
        @Query("per_page")
        per_page: Int
    ): Response<UsersResponse>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Header("accept")
        accept: String,
        @Path("username")
        username: String
    ): Response<UserDetailResponse>

}
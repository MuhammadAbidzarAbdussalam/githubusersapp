package com.abidzar.githubusersapp.model.repository

import com.abidzar.githubusersapp.model.api.RetrofitInstance
import com.abidzar.githubusersapp.util.Constants.Companion.HEADER_VALUE
import com.abidzar.githubusersapp.util.Constants.Companion.PER_PAGE

class UsersRepository {

    suspend fun getUsers(since: Int) = RetrofitInstance.api.getUsers(HEADER_VALUE, since, PER_PAGE)

    suspend fun getUserDetail(username: String) = RetrofitInstance.api.getUserDetail(HEADER_VALUE, username)

}
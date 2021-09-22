package com.abidzar.githubusersapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abidzar.githubusersapp.model.repository.UsersRepository
import com.abidzar.githubusersapp.model.response.userDetail.UserDetailResponse
import com.abidzar.githubusersapp.model.response.userList.UsersResponse
import com.abidzar.githubusersapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.nio.file.attribute.UserDefinedFileAttributeView

class UserViewModel(val usersRepository: UsersRepository) : ViewModel() {

    val userList: MutableLiveData<Resource<UsersResponse>> = MutableLiveData()

    val userDetail: MutableLiveData<Resource<UserDetailResponse>> = MutableLiveData()

    fun getUsers(since: Int) = viewModelScope.launch {
        userList.postValue(Resource.Loading())

        val response = usersRepository.getUsers(since)
        userList.postValue(handleResponse(response))
    }

    fun getUserDetail(username: String) = viewModelScope.launch {
        userDetail.postValue(Resource.Loading())

        val response = usersRepository.getUserDetail(username)
        userDetail.postValue(handleResponseDetail(response))
    }

    private fun handleResponse(response: Response<UsersResponse>): Resource<UsersResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message(), response.code())
    }

    private fun handleResponseDetail(response: Response<UserDetailResponse>): Resource<UserDetailResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message(), response.code())
    }

}
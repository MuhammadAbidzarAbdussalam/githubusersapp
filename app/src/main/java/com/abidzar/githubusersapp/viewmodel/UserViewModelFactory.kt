package com.abidzar.githubusersapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abidzar.githubusersapp.model.repository.UsersRepository

class UserViewModelFactory(  val usersRepository: UsersRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(usersRepository) as T
    }
}
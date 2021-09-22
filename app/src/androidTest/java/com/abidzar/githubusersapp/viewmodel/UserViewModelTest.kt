package com.abidzar.githubusersapp.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abidzar.githubusersapp.model.repository.UsersRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserViewModelTest {

    private lateinit var userViewModel: UserViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val usersRepository = UsersRepository()
        val userViewModelFactory = UserViewModelFactory(usersRepository)

        userViewModel = UserViewModel(usersRepository)


    }

    @Test
    fun testNotNull() {

        userViewModel.getUsers(0)

        val result = userViewModel.userList.getOrAwaitValue()

        assertThat(result).isNotNull()

    }

}
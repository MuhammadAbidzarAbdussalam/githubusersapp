package com.abidzar.githubusersapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abidzar.githubusersapp.R
import com.abidzar.githubusersapp.model.repository.UsersRepository
import com.abidzar.githubusersapp.model.response.userList.UsersResponseItem
import com.abidzar.githubusersapp.util.Resource
import com.abidzar.githubusersapp.view.adapter.UserListAdapter
import com.abidzar.githubusersapp.viewmodel.UserViewModel
import com.abidzar.githubusersapp.viewmodel.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import android.net.NetworkInfo

import android.net.ConnectivityManager


class MainActivity : AppCompatActivity() {

    val TAG = "UserList"
    lateinit var viewModel: UserViewModel
    lateinit var userListAdapter: UserListAdapter
    val linearLayoutManager = LinearLayoutManager(this)
    lateinit var userList: ArrayList<UsersResponseItem>

    var id_since = 0
    var isLoading = false
    var per_page = 10
    var page_count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        supportActionBar?.hide()

        setUpViewModel()

        if (isConnectedToInternet())
            viewModel.getUsers(id_since)
        else
            showError(1)

        viewModel.userList.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { userResponse ->

                        swipeRefresh.isRefreshing = false

                        println("heresss $id_since")
                        if (id_since == 0) {
                            println("here ${userResponse.size}")
                            userList = userResponse

                            setUpRecyclerView()

                        } else {
                            userList.addAll(userResponse)
                        }

                        val lastPosition = userResponse.size - 1

                        id_since = userResponse[lastPosition].id
                        page_count = userResponse.size

                        userListAdapter.notifyDataSetChanged()

                        isLoading = false

                    }
                }
                is Resource.Error -> {
                    showError(response.errorCode)
                    response.message?.let { message ->
                        Log.e(TAG, "An Error Occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        viewModel.userDetail.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { userDetailResponse ->

                        swipeRefresh.isRefreshing = false

                        var detailToast: String =
                            "Avatar Url\t : \t" + userDetailResponse.avatar_url + "\n"
                        detailToast =
                            detailToast + "Username\t : \t" + userDetailResponse.login + "\n"
                        detailToast =
                            detailToast + "Id\t : \t" + userDetailResponse.id.toString() + "\n"
                        detailToast = detailToast + "Email\t : \t" + userDetailResponse.email + "\n"
                        detailToast =
                            detailToast + "Location\t : \t" + userDetailResponse.location + "\n"
                        detailToast =
                            detailToast + "Created At\t : \t" + userDetailResponse.created_at + "\n"

                        val toast = Toast.makeText(this, detailToast, Toast.LENGTH_LONG)
                        toast.show()
                    }
                }
                is Resource.Error -> {
                    showError(response.errorCode)
                    response.message?.let { message ->
                        Log.e(TAG, "An Error Occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        rvGithubUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition =
                    linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if (!isLoading && isLastPosition && page_count == per_page) {

                    if (isConnectedToInternet()) {
                        id_since?.let { viewModel.getUsers(it) }
                        isLoading = true
                    } else {
                        swipeRefresh.isRefreshing = false
                        Toast.makeText(
                            this@MainActivity, "Oops, There is something went wrong. Check your internet connection \n" +
                                    "\n" +
                                    "Please Swipe Down To Refresh", Toast.LENGTH_LONG
                        ).show()
                    }


                }
            }

        })

        swipeRefresh.setOnRefreshListener {
            if (isConnectedToInternet()) {
                id_since = 0
                viewModel.getUsers(id_since)
            } else {
                swipeRefresh.isRefreshing = false
                if (this::userList.isInitialized) {
                    Toast.makeText(
                        this@MainActivity, "Oops, There is something went wrong. Check your internet connection \n" +
                                "\n" +
                                "Please Swipe Down To Refresh", Toast.LENGTH_LONG
                    ).show()
                } else {
                    showError(1)
                }
            }


        }

    }

    private fun setUpViewModel() {
        val usersRepository = UsersRepository()
        val userViewModelFactory = UserViewModelFactory(usersRepository)

        viewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
    }

    private fun setUpRecyclerView() {
        userListAdapter = UserListAdapter(userList)
        rvGithubUsers.apply {
            adapter = userListAdapter
            layoutManager = linearLayoutManager
        }

        userListAdapter.setOnItemClickListener {
            if (isConnectedToInternet()) {
                viewModel.getUserDetail(it.login)
            } else {
                swipeRefresh.isRefreshing = false
                Toast.makeText(
                    this@MainActivity, "Oops, There is something went wrong. Check your internet connection \n" +
                            "\n" +
                            "Please Swipe Down To Refresh", Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showError(errorCode: Int?) {
        progressBar.visibility = View.GONE
        txvErrorLoading.visibility = View.VISIBLE

        if (errorCode == 403)
            txvErrorLoading.text =
                "Oops, Request has reached the limit \n\nPlease try in another time"
        else
            txvErrorLoading.text =
                "Oops, There is something went wrong \n\nPlease Swipe Down To Refresh"
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
        txvErrorLoading.visibility = View.GONE
    }

    private fun showProgressBar() {
//        progressBar.visibility = View.VISIBLE
//        txvErrorLoading.visibility = View.VISIBLE
//        txvErrorLoading.text = "Loading..."

        swipeRefresh.isRefreshing = true

    }

    private fun isConnectedToInternet(): Boolean {
        val connectivity =
            applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (i in info.indices) if (info[i].state == NetworkInfo.State.CONNECTED) {
            return true
        }
        return false
    }
}
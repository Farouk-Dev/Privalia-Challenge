package com.example.testmid.activities.list_movies_activity.presenter

import android.content.Context
import android.util.Log
import com.example.testmid.R
import com.example.testmid.activities.list_movies_activity.interfaces.ViewInterface
import com.example.testmid.activities.list_movies_activity.interfaces.WsCallInterface
import com.example.testmid.models.ApiResponse
import com.example.testmid.models.Movie
import com.example.testmid.network.RetrofitManager
import com.example.testmid.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


class ListMoviesActivityPresenter() {
    private lateinit var context: Context
    private lateinit var viewInterface: ViewInterface
    private lateinit var wsCallInterface: WsCallInterface

    constructor(context: Context, viewInterface: ViewInterface, wsCallInterface: WsCallInterface) : this() {
        this.context = context
        this.viewInterface = viewInterface
        this.wsCallInterface = wsCallInterface
    }


    /* web service call*/
    fun getMovies(page: Int) {
        viewInterface.showProgressBar()
        RetrofitManager.getInstance(Constants.BASE_URL).service!!.stations(page.toString(), Constants.API_KEY).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>?) {
                viewInterface.hideProgressBar()
                if (response != null)
                    wsCallInterface.onSuccess(response.body())
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                viewInterface.hideProgressBar()
                Log.e("error",t.message)
                wsCallInterface.onFailure(t.message)
            }
        })
    }
}

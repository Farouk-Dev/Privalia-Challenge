package com.example.testmid.network

import com.example.testmid.models.ApiResponse
import com.example.testmid.models.Movie

import java.lang.reflect.Array
import java.util.ArrayList

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitServices {
    @GET("4/list/{page}")
    fun stations(@Path("page") page: String,
                 @Query("api_key")api_key:String): Call<ApiResponse>

}
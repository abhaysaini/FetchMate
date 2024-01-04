package com.example.FetchMate.data.api

import com.example.FetchMate.data.model.FileFoundModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ResponseApiInterface {
    @GET("/")
    suspend fun getData(
        @Header("X-RapidAPI-Key") header1: String,
        @Header("X-RapidAPI-Host") header2: String,
        @Query("q") parameter1: String,
        @Query("type") parameter2: String
    ): Response<FileFoundModel>
}
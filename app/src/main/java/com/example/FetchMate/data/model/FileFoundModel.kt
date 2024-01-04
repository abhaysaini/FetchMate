package com.example.FetchMate.data.model

import com.google.gson.annotations.SerializedName

data class FileFoundModel(
    @SerializedName("files_found")
    val fileFound : MutableList<ResponseModel>?,
    val status : String?
)
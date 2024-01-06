package com.example.FetchMate.data.model

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("file_id")
    val fileId: String?,
    @SerializedName("file_type")
    val fileType: String?,
    @SerializedName("file_name")
    val fileName: String?,
    @SerializedName("file_link")
    val fileLink: String?,
    @SerializedName("date_added")
    val dateAdded: String?,
    @SerializedName("time_ago")
    val timeAgo: String?,
    @SerializedName("file_size")
    val fileSize: String?,
    @SerializedName("file_size_bytes")
    val fileSizeBytes: String?,
    @SerializedName("referrer_link")
    val referrerLink: String?,
    @SerializedName("referrer_host")
    val referrerHost: String?,
    @SerializedName("readable_path")
    val readablePath: String?
)
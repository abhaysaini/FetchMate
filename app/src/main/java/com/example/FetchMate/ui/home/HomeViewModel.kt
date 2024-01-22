package com.example.FetchMate.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.FetchMate.data.api.RetrofitHelper
import com.example.FetchMate.data.model.ResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeViewModel : ViewModel() {

    fun fetchData(
        keyword: String,
        fileType: String,
        onDataLoaded: (MutableList<ResponseModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitHelper.responseApiInterface.getData(
                    header1 = com.example.FetchMate.BuildConfig.HEADER_1,
                    header2 = com.example.FetchMate.BuildConfig.HEADER_2,
                    parameter1 = keyword,
                    parameter2 = fileType
                )

                val responseTime =
                    (response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis).toDouble() / 1000.0
                Log.i("abhay", responseTime.toString())

                if (response.isSuccessful) {
                    val files = response.body()?.fileFound
                    if (files != null) {
                        withContext(Dispatchers.Main) {
                            onDataLoaded(files)
                        }
                    } else {
                        onError("No files found.")
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
                Timber.tag("HomeViewModel").e(e.toString())
            }
        }
    }
}

package com.example.FetchMate.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.FetchMate.databinding.ActivityHomeBinding
import com.example.FetchMate.data.api.RetrofitHelper
import com.example.FetchMate.data.model.ResponseModel
import com.example.FetchMate.ui.adapter.FileResponseAdapter
import com.pluto.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var fileType: String
    private lateinit var keyword: String
    private lateinit var fileResponseAdapter: FileResponseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.extras != null) {
            fileType = intent.getStringExtra("file_type_selected").toString()
            keyword = intent.getStringExtra("keyword").toString()
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitHelper.responseApiInterface.getData(
                    header1 = com.example.FetchMate.BuildConfig.HEADER_1  ,
                    header2 = com.example.FetchMate.BuildConfig.HEADER_2,
                    parameter1 = "Hulk",
                    parameter2 = fileType
                )
                val responseTime =
                    (response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis).toDouble() / 1000.0
                Log.i("abhay", responseTime.toString())
                if (response.isSuccessful) {
                    val files = response.body()?.fileFound
                    Log.i("abhay", files?.size.toString())
                    if (files != null) {
                        withContext(Dispatchers.Main){
                            updateRecyclerView(files)
                        }
                    }
                } else {
                    Timber.tag("abhay").i("Error" + response.code().toString())
                }
            } catch (e: Exception) {
                Timber.tag("abhay").e(e.toString())
            }
        }
    }

    private fun updateRecyclerView(files : MutableList<ResponseModel>) {
        Log.i("abhay","Number of items: ${files.size}")
        fileResponseAdapter = FileResponseAdapter(this, files)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = fileResponseAdapter
        fileResponseAdapter.notifyDataSetChanged()
    }
}
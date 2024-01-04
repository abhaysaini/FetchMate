package com.example.FetchMate.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.FetchMate.databinding.ActivityHomeBinding
import com.example.FetchMate.data.api.RetrofitHelper
import com.example.FetchMate.utils.Constants.HEADER_1
import com.example.FetchMate.utils.Constants.HEADER_2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var fileType: String
    lateinit var keyword: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.extras != null){
            fileType = intent.getStringExtra("file_type_selected").toString()
            keyword = intent.getStringExtra("keyword").toString()
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitHelper.responseApiInterface.getData(
                    header1 = HEADER_1,
                    header2 = HEADER_2,
                    parameter1 = keyword,
                    parameter2 = fileType
                )
                val responseTime = (response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis).toDouble()/1000.0
                Log.i("abhay",responseTime.toString())
                if(response.isSuccessful){
                    Log.i("abhay",response.body()?.status.toString())
                    Log.i("abhay",response.body()?.fileFound?.size.toString())
                    runOnUiThread{
                        binding.textview.text= response.body()?.fileFound?.get(0)?.toString() ?: ""
                    }
                }
                else{
                    Timber.tag("abhay").i("Error" + response.code().toString())
                }
            }
            catch (e:Exception){
                Timber.tag("abhay").e(e.toString())
            }
        }
    }
}
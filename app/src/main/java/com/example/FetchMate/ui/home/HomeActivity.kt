package com.example.FetchMate.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.FetchMate.R
import com.example.FetchMate.databinding.ActivityHomeBinding
import com.example.FetchMate.data.api.RetrofitHelper
import com.example.FetchMate.data.model.ResponseModel
import com.example.FetchMate.ui.adapter.FileResponseAdapter
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
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        val title = SpannableString("Found Files!!")
        title.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
            0,
            title.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )

        binding.toolbar.title = title
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_back))
        }

        if (intent.extras != null) {
            fileType = intent.getStringExtra("file_type_selected").toString()
            keyword = intent.getStringExtra("keyword").toString()
        }

//        homeViewModel.fetchData(
//            keyword,
//            fileType,
//            onDataLoaded = this::updateRecyclerView,
//            onError = {
//
//            })

        CoroutineScope(Dispatchers.IO).launch {
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
                    Log.i("abhay", files?.size.toString())
                    if (files != null) {
                        withContext(Dispatchers.Main) {
                            updateRecyclerView(files)
                        }
                    } else {
                        runOnUiThread {
                            binding.noFileImage.visibility = View.VISIBLE
                            binding.noFileFound.visibility = View.VISIBLE
                            binding.shimmerEffect.stopShimmer()
                            binding.shimmerEffect.visibility = View.GONE
                        }
                    }
                    runOnUiThread {
                        binding.shimmerEffect.stopShimmer()
                        binding.shimmerEffect.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                } else {
                    Timber.tag("abhay").i("Error" + response.code().toString())
                }
            } catch (e: Exception) {
                Timber.tag("abhay").e(e.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun updateRecyclerView(files: MutableList<ResponseModel>) {
        Log.i("abhay", "Number of items: ${files.size}")
        fileResponseAdapter = FileResponseAdapter(this, files.toMutableList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = fileResponseAdapter
        fileResponseAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerEffect.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerEffect.stopShimmer()
    }
}
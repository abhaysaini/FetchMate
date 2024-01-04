package com.example.FetchMate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.example.FetchMate.databinding.ActivityMainBinding
import com.example.FetchMate.ui.HomeActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var keyword: String
    lateinit var fileTypeSelected: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_items,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                Id: Long
            ) {
                fileTypeSelected = resources.getStringArray(R.array.spinner_items)[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Snackbar.make(binding.root, "Please select file type", Snackbar.LENGTH_LONG).show()
            }
        }

        keyword = binding.keyword.text.toString()
        Log.i("abhay", keyword)
        binding.searchButton.setOnClickListener {
            Intent(this, HomeActivity::class.java).apply {
                putExtra("keyword", keyword)
                putExtra("file_type_selected", fileTypeSelected)
                startActivity(this)
            }
        }
    }
}
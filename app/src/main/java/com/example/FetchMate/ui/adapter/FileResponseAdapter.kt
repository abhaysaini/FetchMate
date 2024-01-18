package com.example.FetchMate.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.FetchMate.data.model.ResponseModel
import com.example.FetchMate.databinding.ItemFileBinding

class FileResponseAdapter(
    private val context: Context,
    private val list: MutableList<ResponseModel>?
) : RecyclerView.Adapter<FileResponseAdapter.ViewHolder>() {
    lateinit var binding: ItemFileBinding

    inner class ViewHolder(private val binding: ItemFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(responseModel: ResponseModel?, position: Int) {
            binding.fileName.text = responseModel?.fileName
            binding.fileDateAdded.text = responseModel?.dateAdded
            binding.fileType.text = responseModel?.fileType
            binding.timeAgo.text = responseModel?.timeAgo
            binding.fileSize.text = when (responseModel?.fileSize) {
                "" -> "No Size"
                else -> responseModel?.fileSize
            }
            val url = responseModel?.fileLink
            binding.download.setOnClickListener {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                    context.startActivity(this)
                }
            }
            binding.share.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT,responseModel?.fileLink)
                context.startActivity(Intent.createChooser(intent,"Share Via"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemFileBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list?.size ?: -1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != -1) {
            val response = list?.get(position)
            holder.bind(response, position)
        }
    }
}
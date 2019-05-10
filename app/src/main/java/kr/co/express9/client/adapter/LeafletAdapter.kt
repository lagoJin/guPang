package kr.co.express9.client.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemLeafletBinding

class LeafletAdapter : RecyclerView.Adapter<LeafletAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val arrayList = listOf(
        R.drawable.background_banner_01,
        R.drawable.background_banner_02,
        R.drawable.background_banner_03
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemLeafletBinding = DataBindingUtil.inflate(inflater, R.layout.item_leaflet, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeafletAdapter.ViewHolder, position: Int) {
        Glide.with(holder.binding.root)
            .load(arrayList[position])
            .into(holder.binding.bannerImage)
    }

    class ViewHolder(internal val binding: ItemLeafletBinding) : RecyclerView.ViewHolder(binding.root)


    override fun getItemCount(): Int = arrayList.size
}

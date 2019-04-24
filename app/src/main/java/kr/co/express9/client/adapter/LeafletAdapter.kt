package kr.co.express9.client.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemLeafletBinding
import kr.co.express9.client.databinding.ItemMapMarketBinding

class LeafletAdapter : RecyclerView.Adapter<LeafletAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val arrayList = ArrayList<Leaflet>()

    init {
        initDummyList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemLeafletBinding = DataBindingUtil.inflate(inflater, R.layout.item_leaflet, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.leaflet = arrayList[position]
    }

    class ViewHolder(internal val binding: ItemLeafletBinding) : RecyclerView.ViewHolder(binding.root)

    data class Leaflet(
        var marketTitle: String,
        var marketAddress: String,
        var marketTime: String,
        var url: String
    )

    private fun initDummyList() {
        arrayList.add(Leaflet("인천", "인천 서구 심곡동", "운영시간 9:00 am - 11:30 pm ", ""))
        arrayList.add(Leaflet("천안", "충남 천안시 서북구", "운영시간 12:00 am - 21:00 pm ", ""))
        arrayList.add(Leaflet("우리집", "인천 서구 심곡동", "운영시간 9:00 am - 11:30 pm ", ""))
    }


}
package kr.co.express9.client.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemMapMarketBinding
import kr.co.express9.client.mvvm.model.data.Mart

class MapMartAdapter(private val martList: ArrayList<Mart>) : RecyclerView.Adapter<MapMartAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val arrayList = ArrayList<MarketDummy>()

    init {
        initDummyList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemMapMarketBinding = DataBindingUtil.inflate(inflater, R.layout.item_map_market, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = martList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.mart = martList[position]
    }


    class ViewHolder(internal val binding: ItemMapMarketBinding) : RecyclerView.ViewHolder(binding.root)

    data class MarketDummy(
        var marketTitle: String,
        var marketAddress: String,
        var marketTime: String
    )

    private fun initDummyList() {
        arrayList.add(MarketDummy("인천", "인천 서구 심곡동", "운영시간 9:00 am - 11:30 pm "))
        arrayList.add(MarketDummy("천안", "충남 천안시 서북구", "운영시간 12:00 am - 21:00 pm "))
        arrayList.add(MarketDummy("우리집", "인천 서구 심곡동", "운영시간 9:00 am - 11:30 pm "))
    }


}
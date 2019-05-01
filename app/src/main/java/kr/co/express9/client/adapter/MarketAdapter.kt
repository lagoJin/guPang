package kr.co.express9.client.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemMarketBinding

class MarketAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private val arrayList = ArrayList<Market>()

    init {
        initDummyList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemMarketBinding = DataBindingUtil.inflate(inflater, R.layout.item_market, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.binding.market = arrayList[position]
    }

    class ViewHolder(internal val binding: ItemMarketBinding) : RecyclerView.ViewHolder(binding.root)

    data class Market(
        var marketTitle: String,
        var marketAddress: String,
        var marketTime: String
    )

    private fun initDummyList() {
        arrayList.add(Market("메로나 슈퍼", "인천 서구 심곡동", "운영시간 9:00 am - 11:30 pm "))
        arrayList.add(Market("누가바 슈퍼", "충남 천안시 서북구", "운영시간 12:00 am - 21:00 pm "))
        arrayList.add(Market("돼지바 슈퍼", "인천 서구 심곡동", "운영시간 9:00 am - 11:30 pm "))
    }

}
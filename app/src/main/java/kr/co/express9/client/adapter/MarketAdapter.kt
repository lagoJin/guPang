package kr.co.express9.client.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemMarketBinding
import kr.co.express9.client.mvvm.model.data.Mart

class MarketAdapter(private val martList: ArrayList<Mart>, var cb: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    init {
        initDummyList()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemMarketBinding = DataBindingUtil.inflate(inflater, R.layout.item_market, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = martList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.binding.mart = martList[position]
        val popup = PopupMenu(context, holder.binding.btnPopup)
        popup.menuInflater.inflate(R.menu.menu_market, popup.menu)
        holder.binding.btnPopup.setOnClickListener {
            popup.show()
        }
        popup.setOnMenuItemClickListener {
            cb(it.itemId)
            martList.removeAt(it.itemId)
            true
        }
    }

    class ViewHolder(internal val binding: ItemMarketBinding) : RecyclerView.ViewHolder(binding.root)

    data class Market(
        var marketTitle: String,
        var marketAddress: String,
        var marketTime: String
    )

    private fun initDummyList() {
//        arrayList.add(Market("메로나 슈퍼", "인천 서구 심곡동", "운영시간 9:00 am - 11:30 pm "))
//        arrayList.add(Market("누가바 슈퍼", "충남 천안시 서북구", "운영시간 12:00 am - 21:00 pm "))
//        arrayList.add(Market("돼지바 슈퍼", "인천 서구 심곡동", "운영시간 9:00 am - 11:30 pm "))
    }

}
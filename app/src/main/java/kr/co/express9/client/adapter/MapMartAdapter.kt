package kr.co.express9.client.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemMapMarketBinding
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.util.Logger

class MapMartAdapter(private val martList: ArrayList<Mart>, var cb: (Mart, Boolean) -> Unit) : RecyclerView.Adapter<MapMartAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemMapMarketBinding = DataBindingUtil.inflate(inflater, R.layout.item_map_market, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = martList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.mart = martList[position]
        holder.binding.ivMarketFavorite.isChecked = User.getFavoriteMarts().contains(martList[position])

        holder.binding.ivMarketFavorite.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cb(martList[position], isChecked)
            } else {
                cb(martList[position], isChecked)
            }
        }

    }

    class ViewHolder(internal val binding: ItemMapMarketBinding) : RecyclerView.ViewHolder(binding.root)

}
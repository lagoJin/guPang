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

class MartAdapter(private val martList: ArrayList<Mart>, var cb: (Mart) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

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
        popup.menuInflater.inflate(R.menu.menu_mart, popup.menu)
        holder.binding.btnPopup.setOnClickListener {
            popup.show()
        }
        popup.setOnMenuItemClickListener {
            if (it.title == "삭제") {
                cb(martList[position])
                martList.remove(martList[position])
            }
            true
        }
    }

    class ViewHolder(internal val binding: ItemMarketBinding) : RecyclerView.ViewHolder(binding.root)

}
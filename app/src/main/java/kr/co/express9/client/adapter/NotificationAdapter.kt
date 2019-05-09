package kr.co.express9.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemNotificationBinding
import kr.co.express9.client.mvvm.model.data.Notification

class NotificationAdapter(val onDelete: (index: Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class VH(val b: ItemNotificationBinding) : RecyclerView.ViewHolder(b.root)

    var notificationList = ArrayList<Notification>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemNotificationBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_notification,
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemCount(): Int = notificationList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as VH
        holder.b.notification = notificationList[position]
        holder.b.ivDelete.setOnClickListener {
            onDelete(position)
        }

    }
}
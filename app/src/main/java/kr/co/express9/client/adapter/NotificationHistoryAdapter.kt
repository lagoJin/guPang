package kr.co.express9.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemNotificationHistoryBinding
import kr.co.express9.client.mvvm.model.data.Notification
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NotificationHistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class VH(val b: ItemNotificationHistoryBinding) : RecyclerView.ViewHolder(b.root)

    var notificationHistoryList = ArrayList<Notification>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemNotificationHistoryBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_notification_history,
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemCount(): Int = notificationHistoryList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as VH
        val context = holder.b.tvTitle.context
        val notification = notificationHistoryList[position]
        val title = context.getString(R.string.noti_title, notification.keyword)
        val body = context.getString(R.string.noti_body, notification.martName)
        holder.b.tvTitle.text = title
        holder.b.tvBody.text = body

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = notification.getMilliseconds

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dayStr = if(day < 10) "0$day" else day.toString()
        val monthStr = if(month < 10) "0$month" else month.toString()

        holder.b.tvDate.text = if(System.currentTimeMillis() - notification.getMilliseconds < 24*3600*1000) {
            val dt = Date(notification.getMilliseconds)
            val sdf = SimpleDateFormat("hh:mm aa")
            sdf.format(dt)
        } else {
            "$monthStr/$dayStr"
        }
    }
}
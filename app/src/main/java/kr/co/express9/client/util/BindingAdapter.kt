package kr.co.express9.client.util

import android.graphics.Paint
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import kr.co.express9.client.R
import kr.co.express9.client.mvvm.model.data.User
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

@BindingAdapter("bind_adapter")
fun RecyclerView.setBindAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    this.adapter = adapter
}

@BindingAdapter("bind_adapter")
fun ViewPager2.setBindAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    this.adapter = adapter
}

@BindingAdapter("bind_auto_complete_text")
fun AutoCompleteTextView.setBindArrayAdapter(adapter: ArrayAdapter<String>) {
    adapter.let { this.setAdapter(it) }
}

@BindingAdapter("bind_array_adapter_list")
fun AutoCompleteTextView.setBindArray(dataList: ArrayList<String>) {
    //출력이 되지 않음.
    dataList.let {
        val adapter = ArrayAdapter<String>(this.context, android.R.layout.simple_dropdown_item_1line, dataList)
        this.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }
}

@BindingAdapter("bind_image")
fun ImageView.setImageUrl(profileUrl: String?) {
    if (!TextUtils.isEmpty(profileUrl)) {
        Glide.with(this.context)
            .load(profileUrl)
            .thumbnail(0.1f)
            .error(R.mipmap.ic_launcher)
            .into(this)
    }
}

@BindingAdapter("bind_price")
fun TextView.setPrice(price: Int) {
    val nf = NumberFormat.getInstance()
    this.text = "${nf.format(price)}원"
}

@BindingAdapter("bind_date")
fun TextView.setDate(yyyyMMdd: String) {
    val sdf = SimpleDateFormat("yyyyMMdd")
    val date = sdf.parse(yyyyMMdd)
    val cal = Calendar.getInstance()
    cal.time = date
    val dayOfWeek = when (cal.get(Calendar.DAY_OF_WEEK)) {
        1 -> "일"
        2 -> "월"
        3 -> "화"
        4 -> "수"
        5 -> "목"
        6 -> "금"
        else -> "토"
    }
    val month = yyyyMMdd.substring(4, 6)
    val day = yyyyMMdd.substring(6, 8)
    this.text = "$month/$day ($dayOfWeek)"
}

@BindingAdapter("bind_cancel_line")
fun TextView.setCancelLine(draw: Boolean) {
    if (draw) this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, visibility: Boolean) {
    view.visibility = if (visibility) View.VISIBLE else View.GONE
}

@BindingAdapter("bind_mart_name")
fun martSeq2Name(view: TextView, martSeq: Int) {
    var martName = view.context.getString(R.string.no_mart_in_favorite_mart_list)
    User.getFavoriteMarts().forEach {
        if (it.martSeq == martSeq) {
            martName = it.name
            return@forEach
        }
    }
    view.text = martName
}
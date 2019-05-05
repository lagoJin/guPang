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
import java.text.NumberFormat

@BindingAdapter("bind_adapter")
fun RecyclerView.setBindAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    adapter.let { this.adapter = it }
}

@BindingAdapter("bind_adapter")
fun ViewPager2.setBindAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    adapter.let { this.adapter = it }
}

@BindingAdapter("bind_array_adapter")
fun AutoCompleteTextView.setBindArrayAdapter(dataList: LiveData<ArrayList<String>>) {

    //TODO 최초 검색 시 리스트 반영 x
    //출력이 되지 않음.
    dataList.let {
        val adapter = ArrayAdapter<String>(this.context, android.R.layout.simple_dropdown_item_1line, dataList.value!!)
        adapter.notifyDataSetChanged()
        this.setAdapter(adapter)
    }
}

@BindingAdapter("bind_image")
fun ImageView.setImageUrl(profileUrl: String?) {
    if (!TextUtils.isEmpty(profileUrl)) {
        Glide.with(this.context)
            .load(profileUrl)
            .into(this)
    }
}

@BindingAdapter("bind_price")
fun TextView.setPrice(price: Int) {
    val nf = NumberFormat.getInstance()
    this.text = nf.format(price) + "원"
}

@BindingAdapter("bind_cancel_line")
fun TextView.setCancelLine(draw: Boolean) {
    if (draw) this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, visibility: Boolean) {
    view.visibility = if (visibility) View.VISIBLE else View.GONE
}
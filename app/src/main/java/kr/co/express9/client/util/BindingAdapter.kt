package kr.co.express9.client.util

import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.express9.client.mvvm.model.data.Address

@BindingAdapter("bind_adapter")
fun setBindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    adapter.let {
        view.adapter = it
    }
}

@BindingAdapter("bind_array_adapter")
fun AutoCompleteTextView.setBindArrayAdapter(dataList: LiveData<ArrayList<String>>) {
    dataList.let {
        val adapter = ArrayAdapter<String>(this.context, android.R.layout.simple_dropdown_item_1line, dataList.value!!)
        adapter.notifyDataSetChanged()
        this.setAdapter(adapter)
    }
}

@BindingAdapter("url")
fun setImageUrl(view: ImageView, profileUrl: String?) {
    if (!TextUtils.isEmpty(profileUrl)) {
        Glide.with(view.context)
            .load(profileUrl)
            .into(view)
    }
}
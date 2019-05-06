package kr.co.express9.client.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemProductBinding
import kr.co.express9.client.mvvm.model.data.Product
import kr.co.express9.client.mvvm.view.ProductActivity
import kr.co.express9.client.util.extension.launchActivity

class ProductAdapter(var showTitle: Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class VH(val b: ItemProductBinding) : RecyclerView.ViewHolder(b.root)

    var productList = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemProductBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_product,
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as VH
        holder.b.title.visibility = if (showTitle && position == 1) View.VISIBLE else View.GONE
        holder.b.product = productList[position]
        holder.b.cvLayout.setOnClickListener {
            it.context.launchActivity<ProductActivity> {
                putExtra("product", productList[position])
            }
        }
    }
}

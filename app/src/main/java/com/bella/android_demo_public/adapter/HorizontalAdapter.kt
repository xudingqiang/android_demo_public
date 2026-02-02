package com.bella.android_demo_public.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bella.android_demo_public.R
import org.w3c.dom.Text

class HorizontalAdapter(private val itemList: List<DemoItem>) : RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {

    // 数据类，定义单项的数据结构
    data class DemoItem(val title: String, val subTitle: String, val iconResId: Int)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivIcon: ImageView
        var tvTitle: TextView
        var tvSubTitle: TextView

        // 可选：为整个项添加点击监听
        init {
            ivIcon = itemView?.findViewById<ImageView>(R.id.ivIcon)!!
            tvTitle= itemView?.findViewById<TextView>(R.id.tvTitle)!!
            tvSubTitle =   itemView?.findViewById<TextView>(R.id.tvSubTitle)!!


            itemView.setOnClickListener {
                // 可以在这里处理点击事件，通过 adapterPosition 获取位置
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // 执行点击操作，例如触发回调
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horizontal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.tvTitle.text = item.title
        holder.tvSubTitle.text = item.subTitle
        holder.ivIcon.setImageResource(item.iconResId)
    }

    override fun getItemCount() = itemList.size
}
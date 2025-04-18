package com.bella.android_demo_public.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails
import androidx.recyclerview.widget.RecyclerView
import com.bella.android_demo_public.R
import com.bella.android_demo_public.bean.ItemInfo
import com.bella.android_demo_public.svg.SVG
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.Collections

class AppListAdapter(val context: Context,private val items: List<ItemInfo>) :
    RecyclerView.Adapter<AppListAdapter.AppListViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()
    var selectionMode = true

    fun getInputStreamFromFile(filePath: String): InputStream? {
        return try {
            FileInputStream(File(filePath))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    override fun onBindViewHolder(holder: AppListViewHolder, position: Int) {
        val item = items.get(position)
        holder.txtTitle.text = item.title
        val path = "volumes/6dbf70eb-c233-4017-9fc8-e4e37bc3fe1d" + item.IconPath
        if (path.contains("svg")) {
            val svg = SVG.getFromInputStream(getInputStreamFromFile(path))
            val picture = svg.renderToPicture()
            val bitmap = Bitmap.createBitmap(
                picture.width,
                picture.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            canvas.drawPicture(picture)
            holder.imgIcon.setImageBitmap(bitmap);
        } else {
            holder.imgIcon.setImageBitmap(BitmapFactory.decodeFile(path));
        }

        holder.checkBox.isChecked = selectedItems.contains(position)
//        holder.checkBox.visibility = if (selectionMode) View.VISIBLE else View.GONE
        setBgColor(holder.itemView,position)

        holder.itemView.setOnClickListener {
            if("".equals(items.get(position).title)){

            }else{
                if (selectionMode) {
                    setBgColor(holder.itemView,position)
                    toggleSelection(position)
                } else {
                    // 正常点击逻辑
                }
            }
        }

//        holder.itemView.setOnLongClickListener {
//            selectionMode = true
//            toggleSelection(position)
//            true
//        }

    }

    fun setBgColor(view:View,position: Int){
        if (selectedItems.contains(position)) {
            view.setBackgroundColor(context.getColor(R.color.desktop_select_bg))
        } else {
            view.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_app_item, parent, false)
        return AppListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        // 确保位置有效
        if (fromPosition < 0 || fromPosition >= items.size ||
            toPosition < 0 || toPosition >= items.size
        ) {
            return
        }

        // 交换数据源中的位置
//        Collections.swap(items, fromPosition, toPosition)

        // 通知适配器项目移动
//        notifyItemMoved(fromPosition, toPosition)
    }


    fun swap(fromPosition: Int, toPosition: Int){
        Collections.swap(items, fromPosition, toPosition)
        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)
    }

    fun toggleSelection(position: Int) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
        } else {
            selectedItems.add(position)
        }
        notifyItemChanged(position)
    }

    fun clearSelection() {
        selectedItems.clear()
        selectionMode = false
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<ItemInfo> {
        return selectedItems.map { items[it] }
    }


    class AppListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView
        var imgIcon: ImageView
        var checkBox: CheckBox
        var rootView: LinearLayout

        init {
            txtTitle = itemView?.findViewById<TextView>(R.id.txtTitle)!!
            imgIcon = itemView?.findViewById<ImageView>(R.id.imgIcon)!!
            checkBox = itemView?.findViewById<CheckBox>(R.id.checkBox)!!
            rootView  = itemView?.findViewById<LinearLayout>(R.id.rootView)!!
        }

        fun bind(content: String?, isSelected: Boolean) {
            txtTitle.text = content
//            txtTitle.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.WHITE)
            rootView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.TRANSPARENT)
        }

        val itemDetails: ItemDetails<Long>
            get() = object : ItemDetails<Long>() {
                override fun getPosition(): Int {
                    return adapterPosition
                }

                override fun getSelectionKey(): Long {
                    return adapterPosition.toLong()
                }
            }
    }
}
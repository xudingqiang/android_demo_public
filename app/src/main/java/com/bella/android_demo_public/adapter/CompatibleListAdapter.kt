package com.bella.android_demo_public.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bella.android_demo_public.R
import com.bella.android_demo_public.bean.CompatibleList
import com.bella.android_demo_public.utils.CompUtils
import com.bella.android_demo_public.utils.CompUtils.parseEnChJson
import com.bella.android_demo_public.utils.CompatibleConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CompatibleListAdapter(
    private val context: Context,
    private var packageName: String,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CompatibleListAdapter.ViewHolder>() {
    private var list = emptyList<CompatibleList>()
    private lateinit var popupWindow: PopupWindow;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_compatible_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val keyCode = item.keyCode
        holder.txtKey.text = keyCode
        val keyDescStr = CompUtils.parseEnChJson(context, item.keyDesc)
        holder.txtTitle.text = keyDescStr


        holder.recyclerView?.layoutManager = LinearLayoutManager(context);
        if (packageName != null && !"".equals(packageName)) {
            val adapter = CompatiblePageListAdapter(context, packageName, item)
            holder.recyclerView?.adapter = adapter;
            GlobalScope.launch(Dispatchers.IO) {
                var uniqueList = CompatibleConfig.queryValueListByKeyCodeAndPackageName(
                    context,
                    packageName,
                    item.keyCode
                );

                withContext(Dispatchers.Main) {
                    adapter?.setData(uniqueList!!)
                }
            }
        } else {
            val adapter = CompatiblePkgListAdapter(context, packageName, item)
            holder.recyclerView?.adapter = adapter;

            GlobalScope.launch(Dispatchers.IO) {
                var listPkgs = CompatibleConfig.queryValueListByKeyCode(context, item.keyCode)
                var uniqueList = listPkgs?.distinctBy { it.packageName }

                withContext(Dispatchers.Main) {
                    adapter?.setData(uniqueList!!)
                }
            }
        }


        val noteStr = parseEnChJson(context, item.notes)
        holder.imgRemarks?.setOnHoverListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_HOVER_ENTER -> {
                    showPopupWindow(context, v, noteStr)
                    true
                }

                MotionEvent.ACTION_HOVER_EXIT -> {
                    if (popupWindow != null) {
                        popupWindow.dismiss()
                    }
                    true
                }

                else -> false
            }
        }

        holder.switchDown.setOnCheckedChangeListener { _, isChecked ->
            holder.recyclerView.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

    }

    fun setData(newData: List<CompatibleList>) {
        list = newData
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, keyCode: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootView: LinearLayout = itemView.findViewById<LinearLayout>(R.id.rootView)
            ?: throw IllegalArgumentException("ImagLinearLayouteView not found")
        val txtKey: TextView = itemView.findViewById<TextView>(R.id.txtKey)
            ?: throw IllegalArgumentException("TextView not found")
        val txtTitle: TextView = itemView.findViewById<TextView>(R.id.txtTitle)
            ?: throw IllegalArgumentException("TextView not found")
        val recyclerView: RecyclerView = itemView.findViewById<RecyclerView>(R.id.recyclerView)
            ?: throw IllegalArgumentException("RecyclerView not found")
        val imgRemarks: ImageView = itemView.findViewById<ImageView>(R.id.imgRemarks)
            ?: throw IllegalArgumentException("ImageView not found")
        val switchDown: Switch = itemView.findViewById<Switch>(R.id.switchDown)
            ?: throw IllegalArgumentException("Switch not found")
    }

    private fun showPopupWindow(context: Context, anchorView: View, content: String) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_tip_view, null)
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupView.findViewById<TextView>(R.id.txtContent)?.text = content;
        popupWindow.isOutsideTouchable = true
//        popupWindow.showAsDropDown(anchorView,20,-48)
        popupWindow.showAsDropDown(anchorView , 16 ,-40)

//        val anchorLocation = IntArray(2)
//        anchorView.getLocationOnScreen(anchorLocation)
//
//        val anchorX = anchorLocation[0]
//        val anchorY = anchorLocation[1]
//
//        val screenWidth = context.resources.displayMetrics.widthPixels
//        val screenHeight = context.resources.displayMetrics.heightPixels
//
//        val popupWidth = popupWindow.contentView.measuredWidth
//        val popupHeight = popupWindow.contentView.measuredHeight
//
//        var popupX = anchorX
//        var popupY = anchorY + anchorView.height
//
//        if (popupY + popupHeight > screenHeight) {
//            popupY = anchorY - popupHeight
//        }
//
//        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, 0, popupY)
    }


}
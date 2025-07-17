package com.bella.android_demo_public.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bella.android_demo_public.R
import com.bella.android_demo_public.bean.WifiInfo

class WifiListAdapter (val context: Context) :
    RecyclerView.Adapter<WifiListAdapter.NetWorkViewHolder>() {
    private var list = emptyList<WifiInfo>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetWorkViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_wifi_info, parent, false)
        return NetWorkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: NetWorkViewHolder, position: Int) {
        val item = list[position]
        holder.txtWifiName.setText(item.wifiName)

        holder.rootView.setOnClickListener({
            showCustomDialog(holder.rootView);
        })


        val  signal = item.signal
        if(item.status == 1){
            holder.txtWifiStatus.visibility = View.VISIBLE
            holder.layoutWifiIcon.setBackgroundResource(R.drawable.round_background_blue)
            if (signal >= 80) {
                holder.imgSignal.setImageResource(R.mipmap.icon_wifi_four_white);
            } else if (signal >= 50) {
                holder.imgSignal.setImageResource(R.mipmap.icon_wifi_three_white);
            } else if (signal > 20) {
                holder.imgSignal.setImageResource(R.mipmap.icon_wifi_two_white);
            } else {
                holder.imgSignal.setImageResource(R.mipmap.icon_wifi_one_white);
            }
        }else{
            holder.txtWifiStatus.visibility = View.GONE
            holder.layoutWifiIcon.setBackgroundResource(R.drawable.round_background_grep)
            if (signal >= 80) {
                holder.imgSignal.setImageResource(R.mipmap.icon_wifi_four);
            } else if (signal >= 50) {
                holder.imgSignal.setImageResource(R.mipmap.icon_wifi_three);
            } else if (signal > 20) {
                holder.imgSignal.setImageResource(R.mipmap.icon_wifi_two);
            } else {
                holder.imgSignal.setImageResource(R.mipmap.icon_wifi_one);
            }
        }

    }

    private fun showCustomDialog(view: View) {
        val builder = AlertDialog.Builder(context)
        val customView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_wifi_connect_by_pwd, null)

        builder.setView(customView)
        val dialog = builder.create()
        dialog.show()
//        val editText = customView.findViewById<AppCompatEditText>(R.id.editText)
//            ?: throw IllegalArgumentException("EditText not found")
//        val txtCancel = customView.findViewById<TextView>(R.id.txtCancel)
//            ?: throw IllegalArgumentException("TextViewTextView not found")
//        val txtConfirm = customView.findViewById<TextView>(R.id.txtConfirm)
//            ?: throw IllegalArgumentException("TextView not found")
//        txtCancel?.setOnClickListener {
//            dialog.dismiss()
//        }

    }

    fun setData(newData: List<WifiInfo>) {
        list = newData
        notifyDataSetChanged()
    }

    inner class NetWorkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootView: RelativeLayout = itemView.findViewById<RelativeLayout>(R.id.rootView)
            ?: throw IllegalArgumentException("RelativeLayout not found")
        val layoutWifiIcon: LinearLayout = itemView.findViewById<LinearLayout>(R.id.layoutWifiIcon)
            ?: throw IllegalArgumentException("LinearLayout not found")
        val txtWifiName: TextView = itemView.findViewById<TextView>(R.id.txtWifiName)
            ?: throw IllegalArgumentException("TextView not found")
        val txtWifiType: TextView = itemView.findViewById<TextView>(R.id.txtWifiType)
            ?: throw IllegalArgumentException("TextView not found")
        val txtWifiStatus: TextView = itemView.findViewById<TextView>(R.id.txtWifiStatus)
            ?: throw IllegalArgumentException("TextView not found")

        val imgSignal: ImageView = itemView.findViewById<ImageView>(R.id.imgSignal)
            ?: throw IllegalArgumentException("ImageView not found")
        val imgWifi: ImageView = itemView.findViewById<ImageView>(R.id.imgWifi)
            ?: throw IllegalArgumentException("ImageView not found")
        val imgWifiInfo: ImageView = itemView.findViewById<ImageView>(R.id.imgWifiInfo)
            ?: throw IllegalArgumentException("ImageView not found")

    }

}
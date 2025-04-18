package com.bella.android_demo_public.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bella.android_demo_public.R
import com.bella.android_demo_public.bean.CompatibleList
import com.bella.android_demo_public.bean.CompatibleValue
import com.bella.android_demo_public.utils.CompUtils
import com.bella.android_demo_public.utils.CompatibleConfig
import com.bella.android_demo_public.utils.LogTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CompatiblePkgListAdapter(
    private val context: Context,
    private val packageName: String,
    private val compatibleList: CompatibleList
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list = emptyList<CompatibleValue>()
    private lateinit var popupWindow: PopupWindow;

    companion object {
        const val TYPE_SWITCH = "switch"
        const val TYPE_SELECT = "select"
        const val TYPE_INPUT = "input"
        const val TYPE_PAGE = "page"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (TYPE_PAGE.equals(compatibleList.inputType)) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_compatible_pkg_list, parent, false)
            return PackageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_compatible_item, parent, false)
            return PageViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when (holder) {
            is PackageViewHolder -> {
                val appData = CompUtils.getAppInfo(context, item.packageName)
                holder.txtTitle.text = appData?.name
                holder.imgIcon.setImageDrawable(appData?.icon);

                holder.switchDown.setOnCheckedChangeListener { _, isChecked ->
                    holder.recyclerView.visibility = if (isChecked) View.VISIBLE else View.GONE
                }

                holder.recyclerView?.layoutManager = LinearLayoutManager(context);
                val adapter =
                    compatibleList?.let { CompatiblePageListAdapter(context, packageName, it) }
                holder.recyclerView?.adapter = adapter;


                GlobalScope.launch(Dispatchers.IO) {
                    var listPkgs = CompatibleConfig.queryValueListByKeyCodeAndPackageName(
                        context,
                        item.packageName,
                        item.keyCode
                    )
                    withContext(Dispatchers.Main) {
                        adapter?.setData(listPkgs!!)
                    }
                }

            }

            is PageViewHolder -> {
                val appData = CompUtils.getAppInfo(context, item.packageName)
                holder.txtTitle.text = appData?.name
                holder.imgIcon.setImageDrawable(appData?.icon);

                if ("".equals(packageName)) {
                    holder.rootView.setPadding(48, 0, 16, 0);
                }

                if (TYPE_INPUT.equals(compatibleList.inputType)) {
                    holder.layoutSwitch.visibility = View.GONE
                    holder.spinner.visibility = View.GONE
                    holder.txtInput.visibility = View.VISIBLE
                    holder.txtInput.text = item.value ?: ""
                    holder.txtInput.setOnClickListener({
                        showCustomDialog(holder.txtInput, item)
                    })
                } else if (TYPE_SELECT.equals(compatibleList.inputType)) {
                    holder.layoutSwitch.visibility = View.GONE
                    holder.spinner.visibility = View.VISIBLE
                    holder.txtInput.visibility = View.GONE
                    val listOptions =
                        CompUtils.parseJson(compatibleList.optionJson) as Array<String>
                    LogTool.i(
                        "resolutions " + listOptions?.size + " , " + (listOptions?.get(0) ?: "")
                    )
                    val adapter = ArrayAdapter(
                        context,
                        R.layout.spinner_item_selected,
                        listOptions
                    ).also { adapter ->
                        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown)
                    }
                    holder.spinner.adapter = adapter

                    if (item.value == null || "".equals(item.value)) {
                        holder.spinner.setSelection(0)
                    } else {
                        val index = listOptions.indexOf(item.value);
                        holder.spinner.setSelection(index)
                    }

                    holder.spinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                CompatibleConfig.updateValueData(
                                    context,
                                    item.keyCode,
                                    item.packageName,
                                    item.activityName,
                                    listOptions.get(position)
                                )
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }
                        }
                } else {
                    holder.layoutSwitch.visibility = View.VISIBLE
                    holder.spinner.visibility = View.GONE
                    holder.txtInput.visibility = View.GONE
                    holder.switchComp.isChecked = item.value == "true"

                    holder.switchComp.isChecked = "true".equals(item.value)

                    holder.switchComp.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
                        CompatibleConfig.updateValueData(
                            context,
                            item.keyCode,
                            item.packageName,
                            item.activityName,
                            b.toString()
                        )
                    })
                }


            }
        }
    }

    fun setData(newData: List<CompatibleValue>) {
        list = newData
        notifyDataSetChanged()
    }

    private fun showPopupWindow(context: Context, view: View, content: String) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_tip_view, null)
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupView.findViewById<TextView>(R.id.txtContent)?.text = content;
        popupWindow.isOutsideTouchable = true
        popupWindow.showAsDropDown(view)
    }

    private fun showCustomDialog(view: TextView, item: CompatibleValue) {
        val builder = AlertDialog.Builder(context)
        val customView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_custom_layout, null)

        builder.setView(customView)
        val dialog = builder.create()
        dialog.show()
        val editText = customView.findViewById<EditText>(R.id.editText) ?: throw IllegalArgumentException("EditText not found")
        val txtCancel = customView.findViewById<TextView>(R.id.txtCancel) ?: throw IllegalArgumentException("TextViewTextView not found")
        val txtConfirm = customView.findViewById<TextView>(R.id.txtConfirm) ?: throw IllegalArgumentException("TextView not found")
        txtCancel?.setOnClickListener {
            dialog.dismiss()
        }
        editText?.setText(item.value);
        txtConfirm?.setOnClickListener {
            val inputText = editText?.text.toString()
            CompatibleConfig.updateValueData(
                context,
                item.keyCode,
                item.packageName,
                item.activityName,
                inputText
            )
            view.text = inputText
            dialog.dismiss()
        }
    }

    inner class PackageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootView: LinearLayout = itemView.findViewById<LinearLayout>(R.id.rootView)
            ?: throw IllegalArgumentException("ImagLinearLayouteView not found")
        val recyclerView: RecyclerView = itemView.findViewById<RecyclerView>(R.id.recyclerView)
            ?: throw IllegalArgumentException("RecyclerView not found")
        val txtTitle: TextView = itemView.findViewById<TextView>(R.id.txtTitle)
            ?: throw IllegalArgumentException("TextView not found")
        val switchDown: Switch = itemView.findViewById<Switch>(R.id.switchDown)
            ?: throw IllegalArgumentException("Switch not found")
        val imgIcon: ImageView = itemView.findViewById<ImageView>(R.id.imgIcon)
            ?: throw IllegalArgumentException("ImageView not found")
    }


    inner class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootView: LinearLayout = itemView.findViewById<LinearLayout>(R.id.rootView)
            ?: throw IllegalArgumentException("ImagLinearLayouteView not found")
        val txtTitle: TextView = itemView.findViewById<TextView>(R.id.txtTitle)
            ?: throw IllegalArgumentException("TextView not found")
        val spinner: AppCompatSpinner = itemView.findViewById<AppCompatSpinner>(R.id.spinner)
            ?: throw IllegalArgumentException("AppCompatSpinner not found")
        val txtInput: TextView =
            itemView.findViewById<TextView>(R.id.txtInput) ?: throw IllegalArgumentException(
                "TextView not found"
            )
        val layoutSwitch: LinearLayout = itemView.findViewById<LinearLayout>(R.id.layoutSwitch)
            ?: throw IllegalArgumentException("LinearLayout not found")
        val switchComp: CheckBox = itemView.findViewById<CheckBox>(R.id.switchComp)
            ?: throw IllegalArgumentException("Switch not found")
        val imgIcon: ImageView = itemView.findViewById<ImageView>(R.id.imgIcon)
            ?: throw IllegalArgumentException("ImageView not found")
    }
}
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
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.RecyclerView
import com.bella.android_demo_public.R
import com.bella.android_demo_public.adapter.CompatiblePkgListAdapter.Companion.TYPE_INPUT
import com.bella.android_demo_public.adapter.CompatiblePkgListAdapter.Companion.TYPE_SELECT
import com.bella.android_demo_public.bean.CompatibleList
import com.bella.android_demo_public.bean.CompatibleValue
import com.bella.android_demo_public.utils.CompUtils
import com.bella.android_demo_public.utils.CompatibleConfig

class CompatiblePageListAdapter(
    private val context: Context,
    private val packageName: String,
    private val compatibleList: CompatibleList
) : RecyclerView.Adapter<CompatiblePageListAdapter.ViewHolder>() {
    private var list = emptyList<CompatibleValue>()
    private lateinit var popupWindow: PopupWindow;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_compatible_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        if (item.activityName == null || "".equals(item.activityName)) {
            holder.txtTitle.text = item.keyCode;
        } else {
            holder.txtTitle.text = item.activityName
        }

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
            val listOptions = CompUtils.parseJson(compatibleList.optionJson) as Array<String>
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

            holder.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootView: LinearLayout = itemView.findViewById<LinearLayout>(R.id.rootView)
            ?: throw IllegalArgumentException("ImagLinearLayouteView not found")
        val txtTitle: TextView = itemView.findViewById<TextView>(R.id.txtTitle)
            ?: throw IllegalArgumentException("TextView not found")
        val spinner: AppCompatSpinner = itemView.findViewById<AppCompatSpinner>(R.id.spinner)
            ?: throw IllegalArgumentException("AppCompatSpinner not found")

        val layoutSwitch: LinearLayout = itemView.findViewById<LinearLayout>(R.id.layoutSwitch)
            ?: throw IllegalArgumentException("LinearLayout not found")
        val txtInput: TextView =
            itemView.findViewById<TextView>(R.id.txtInput) ?: throw IllegalArgumentException(
                "TextView not found"
            )
        val switchComp: CheckBox = itemView.findViewById<CheckBox>(R.id.switchComp)
            ?: throw IllegalArgumentException("Switch not found")
    }
}
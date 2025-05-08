package com.bella.android_demo_public.activity

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bella.android_demo_public.R
import com.bella.android_demo_public.adapter.WifiListAdapter
import com.bella.android_demo_public.bean.WifiInfo
import com.bella.android_demo_public.utils.LogTool
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PreferenceTestActivity : AppCompatActivity() {
    var txtWifi: TextView? = null
    var txtWired: TextView? = null
    var txtAddWifi: TextView? = null
    var txtSavedNoData: TextView? = null
    var txtOtherNoData: TextView? = null
    var recyclerViewSaved: RecyclerView? = null
    var recyclerViewOther: RecyclerView? = null
    var switchComp: CheckBox? = null
    var layoutWifi: LinearLayout? = null
    var layoutWired: LinearLayout? = null
    var btnCancel: AppCompatButton? = null
    var btnSave: AppCompatButton? = null
    var spinnerConfigInterface: AppCompatSpinner? = null
    var spinnerIpSettings: AppCompatSpinner? = null
    var editIpAddress: AppCompatEditText? = null
    var editDefaultGateway: AppCompatEditText? = null
    var editSubnetMask: AppCompatEditText? = null
    var editPreferredDns: AppCompatEditText? = null
    var editAlternativeDns: AppCompatEditText? = null

    lateinit var context: Context;
    lateinit var  job: Job;

    var adapteerSaved: WifiListAdapter? = null;
    var adapteerOther: WifiListAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_preference_test)
        setContentView(R.layout.layout_net_work)
        context = this;
        txtWifi = findViewById(R.id.txtWifi)
        txtWired = findViewById(R.id.txtWired)
        txtAddWifi = findViewById(R.id.txtAddWifi)
        txtSavedNoData = findViewById(R.id.txtSavedNoData)
        txtOtherNoData = findViewById(R.id.txtOtherNoData)
        recyclerViewSaved = findViewById(R.id.recyclerViewSaved)
        recyclerViewOther = findViewById(R.id.recyclerViewOther)
        switchComp = findViewById(R.id.switchComp)
        layoutWired = findViewById(R.id.layoutWired)
        layoutWifi = findViewById(R.id.layoutWifi)
        btnCancel = findViewById(R.id.btnCancel)
        btnSave = findViewById(R.id.btnSave)

        editIpAddress = findViewById(R.id.editIpAddress)
        editPreferredDns = findViewById(R.id.editPreferredDns)
        editSubnetMask = findViewById(R.id.editSubnetMask)
        editAlternativeDns = findViewById(R.id.editAlternativeDns)
        editDefaultGateway = findViewById(R.id.editDefaultGateway)


        spinnerConfigInterface = findViewById(R.id.spinnerConfigInterface)
        spinnerIpSettings = findViewById(R.id.spinnerIpSettings)

        txtWifi?.isSelected = true



        txtWifi?.setOnClickListener({
            txtWifi!!.isSelected = true;
            txtWired!!.isSelected = false;

            layoutWired?.visibility = View.GONE
            layoutWifi?.visibility = View.VISIBLE
        })

        btnSave?.setOnClickListener({
            LogTool.i("id : "+spinnerIpSettings?.selectedItem + "   :  "+editIpAddress?.text.toString())

        })

        txtAddWifi?.setOnClickListener({
            val builder = AlertDialog.Builder(context)
            val customView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_wifi_connect, null)

            builder.setView(customView)
            val dialog = builder.create()
            dialog.show()

            val txtCancel = customView.findViewById<TextView>(R.id.txtCancel)
                ?: throw IllegalArgumentException("TextView not found")
            val txtConfirm = customView.findViewById<TextView>(R.id.txtConfirm)
                ?: throw IllegalArgumentException("TextView not found")
            txtCancel?.setOnClickListener {
                dialog.dismiss()
            }
            txtConfirm?.setOnClickListener {
                dialog.dismiss()
            }
        })

        txtWired?.setOnClickListener({
            txtWired!!.isSelected = true;
            txtWifi!!.isSelected = false;

            layoutWired?.visibility = View.VISIBLE
            layoutWifi?.visibility = View.GONE
        })



        switchComp?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                recyclerViewSaved?.visibility = View.VISIBLE
                txtSavedNoData?.visibility = View.GONE

                recyclerViewOther?.visibility = View.VISIBLE
                txtOtherNoData?.visibility = View.GONE
            } else {
                recyclerViewSaved?.visibility = View.GONE
                txtSavedNoData?.visibility = View.VISIBLE


                recyclerViewOther?.visibility = View.GONE
                txtOtherNoData?.visibility = View.VISIBLE
            }
        }

        recyclerViewSaved?.layoutManager = LinearLayoutManager(this);
        recyclerViewOther?.layoutManager = LinearLayoutManager(this);


        var listSaved = ArrayList<WifiInfo>();
        for (i in 0..6) {
            val wifiInfo = WifiInfo();
            wifiInfo.wifiName = "name " + i;
            wifiInfo.signal = 78 + i;
            listSaved.add(wifiInfo);
        }
        adapteerSaved = WifiListAdapter(context)
        recyclerViewSaved?.adapter = adapteerSaved
        adapteerSaved?.setData(listSaved)


        try {
            var  listOther = ArrayList<WifiInfo>();
            for (i in 0..20) {
                val wifiInfo = WifiInfo();
                wifiInfo.wifiName = "name " + i;
                wifiInfo.signal = 78 + i;
                listOther.add(wifiInfo);
            }
            adapteerOther = WifiListAdapter(context)
            recyclerViewOther?.adapter = adapteerOther
            adapteerOther?.setData(listOther)
        } catch (e: Exception) {
            e.printStackTrace()
            TODO("Not yet implemented")
        }


        // 数据源
        val spinnerItems = listOf("Item 1", "Item 2", "Item 3", "Item 4")
        // 创建 ArrayAdapter
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerItems).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        // 设置适配器
        spinnerConfigInterface?.adapter = adapter




        // 设置选中项监听器
        spinnerConfigInterface?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = spinnerItems[position]
                LogTool.i("selectedItem "+selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 可选：处理未选择项的情况
            }
        }

        job = GlobalScope.launch {
            repeat(10) { i ->
                println("协程运行中... $i")
                delay(5000)
            }
        }

//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.settings_container, SettingsFragment())
//            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
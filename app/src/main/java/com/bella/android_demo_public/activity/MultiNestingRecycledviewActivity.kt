package com.bella.android_demo_public.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bella.android_demo_public.R;
import com.bella.android_demo_public.adapter.CompatibleListAdapter
import com.bella.android_demo_public.bean.CompatibleList
import com.bella.android_demo_public.utils.CompUtils
import com.bella.android_demo_public.utils.LogTool
import com.bella.android_demo_public.view.model.CompatibleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MultiNestingRecycledviewActivity : AppCompatActivity(),
    CompatibleListAdapter.OnItemClickListener {
    private var recyclerView: RecyclerView? = null;
    private var adapter: CompatibleListAdapter? = null;
    private var list: List<CompatibleList>? = null;
    private var context: Context? = null;
    private var packageName :String ? = "";

    private lateinit var viewModel: CompatibleViewModel


    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_nesting_recycledview)
        recyclerView = findViewById(R.id.recyclerView)
        context = this ;

//        packageName = "com.bella.android_demo_public";//getIntent().getStringExtra("packageName");
//        if(packageName == null){
//            packageName = "";
//        }
        recyclerView?.layoutManager = LinearLayoutManager(this);

        list = ArrayList()

        adapter = CompatibleListAdapter(this, packageName!!, this)
        recyclerView?.adapter = adapter;


        viewModel = ViewModelProvider(this).get(CompatibleViewModel::class.java)


        viewModel._compatibleLists.observe(this,Observer { result ->
            // 更新UI界面
            LogTool.i("11111 result "+result)
            list =result
            adapter?.setData(list!!)
        })

        viewModel.loadCompatibleData()

        GlobalScope.launch {
            CompUtils.parseList(context, getResources().openRawResource(R.raw.comp_config_list))
            CompUtils.parseValue(context, getResources().openRawResource(R.raw.comp_config_value))
        }

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {

            }
        }

//        GlobalScope.launch(Dispatchers.IO) {
//            // 在工作线程中执行某些操作
//            list = BellaDataBase.getInstance(context).compatibleListDao().getAllCompatibleList();
//
//            // 切换到主线程更新 UI
//            withContext(Dispatchers.Main) {
//                adapter?.setData(list!!)
//                LogTool.i("list "+list)
//            }
//        }

    }

    private suspend  fun getData() {
//        list.c
//        val tempList: List<Map<String, Any>> = CompatibleConfig.queryListData(context)
//        if (tempList != null) {
//            list.addAll(tempList)
//        }
//        compatibleListAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int, packageName: String) {
        TODO("Not yet implemented")


    }
}
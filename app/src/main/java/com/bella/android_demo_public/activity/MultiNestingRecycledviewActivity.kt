package com.bella.android_demo_public.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bella.android_demo_public.BellaDataBase
import com.bella.android_demo_public.R;
import com.bella.android_demo_public.adapter.CompatibleListAdapter
import com.bella.android_demo_public.bean.CompatibleList
import com.bella.android_demo_public.bean.User
import com.bella.android_demo_public.utils.CompUtils
import com.bella.android_demo_public.utils.LogTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream


class MultiNestingRecycledviewActivity : AppCompatActivity(),
    CompatibleListAdapter.OnItemClickListener {
    private var recyclerView: RecyclerView? = null;
    private var adapter: CompatibleListAdapter? = null;
    private var list: List<CompatibleList>? = null;
    private var context: Context? = null;
    private var packageName :String ? = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_nesting_recycledview)
        recyclerView = findViewById(R.id.recyclerView)
        context = this ;

//        packageName = "com.bella.android_demo_public";//getIntent().getStringExtra("packageName");
        if(packageName == null){
            packageName = "";
        }
        recyclerView?.layoutManager = LinearLayoutManager(this);

        list = ArrayList()

        adapter = CompatibleListAdapter(this, packageName!!, this)
        recyclerView?.adapter = adapter;



        GlobalScope.launch {
            CompUtils.parseList(context, getResources().openRawResource(R.raw.comp_config_list))
            CompUtils.parseValue(context, getResources().openRawResource(R.raw.comp_config_value))
        }

        GlobalScope.launch(Dispatchers.IO) {
            // 在工作线程中执行某些操作
            list = BellaDataBase.getInstance(context).compatibleListDao().getAllCompatibleList();

            // 切换到主线程更新 UI
            withContext(Dispatchers.Main) {
                adapter?.setData(list!!)
                LogTool.i("list "+list)
            }
        }

    }

    private fun getData() {
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
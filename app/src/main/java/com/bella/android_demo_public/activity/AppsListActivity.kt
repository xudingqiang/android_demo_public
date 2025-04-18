package com.bella.android_demo_public.activity

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bella.android_demo_public.R
import com.bella.android_demo_public.adapter.AppListAdapter
import com.bella.android_demo_public.bean.ItemInfo
import com.bella.android_demo_public.utils.LogTool
import com.bella.android_demo_public.utils.NetUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AppsListActivity : Activity() {
    lateinit var recyclerView: RecyclerView;
    lateinit var adapter: AppListAdapter;
    lateinit var list: MutableList<ItemInfo> ;
    lateinit var context:Context ;
    val maxX = 14
    val maxY = 9

    var fromPosition = 0
    var toPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apps_list)
        context = this;
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)!!
        recyclerView.layoutManager = GridLayoutManager(this, 8, RecyclerView.HORIZONTAL,false)
        // 数据初始化
        list = ArrayList()

        //        registerForContextMenu(test14);
//        val list: MutableList<String> = java.util.ArrayList()
//        list.add("")

        //        registerForContextMenu(test14);
        for (i in 0..maxX) {
            for (j in 0..maxY) {
                var  info :ItemInfo = ItemInfo((i*14+j),""+(i*14+j),i,j,"","");
                list.add(info)
            }
        }

        MainScope().launch(Dispatchers.Main) {
            val tempList = withContext(Dispatchers.IO) {
                NetUtils.getLinuxDesktopApp() as List<ItemInfo>
            }

            if(tempList !=null && tempList.size >0 ){
                for(i in 0 .. tempList.size-1 ){
                    list[i] = tempList.get(i)
                }
            }

            adapter = AppListAdapter(context,list)
            recyclerView.adapter = adapter
        }


        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun isLongPressDragEnabled(): Boolean = true
            override fun isItemViewSwipeEnabled(): Boolean = false


            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                 fromPosition = viewHolder.adapterPosition
                 toPosition = target.adapterPosition

                // 通知适配器移动项目
                if(adapter != null){
                    LogTool.i("onMove fromPosition : "+fromPosition + ",toPosition: "+toPosition)
                    adapter.moveItem(fromPosition, toPosition)
//                    adapter.notifyDataSetChanged()
//                    adapter.notifyItemChanged(fromPosition)
//                    adapter.notifyItemChanged(toPosition)
                }
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                LogTool.i("onSwiped "+direction)
                // No-op, we don't want to swipe items
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                val swipeFlags = 0
                LogTool.i("getMovementFlags "+dragFlags)
                return makeMovementFlags(dragFlags, swipeFlags)
               // return super.getMovementFlags(recyclerView, viewHolder)
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                LogTool.i("onSelectedChanged fromPosition: "+fromPosition +" ,toPosition: "+toPosition)
                when (actionState) {
                    ItemTouchHelper.ACTION_STATE_DRAG -> {
                        // 拖拽开始时改变项目外观
                        viewHolder?.itemView?.setBackgroundColor(context.getColor(R.color.desktop_select_bg))
                    }
                    ItemTouchHelper.ACTION_STATE_IDLE ->{
                        adapter.swap(fromPosition,toPosition)
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT)
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)

    }
}
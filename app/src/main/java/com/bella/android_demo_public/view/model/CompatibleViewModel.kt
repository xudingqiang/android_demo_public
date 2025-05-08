package com.bella.android_demo_public.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bella.android_demo_public.BellaDataBase
import com.bella.android_demo_public.bean.CompatibleList
import kotlinx.coroutines.launch

class CompatibleViewModel(application: Application) : AndroidViewModel(application) {

    val resultLiveData = MutableLiveData<List<CompatibleList>>()

    val _compatibleLists = MutableLiveData<List<CompatibleList>>()
//    val compatibleLists: LiveData<List<CompatibleList>> = _compatibleLists

    fun setCompatibleLists(list: List<CompatibleList>) {
        _compatibleLists.value = list
    }

    private val compatibleListDao = BellaDataBase.getInstance(application).compatibleListDao()

    fun loadData(): LiveData<List<CompatibleList>> {
//        val list = MutableLiveData<List<CompatibleList>>()
//        val items = ArrayList<CompatibleList>();
//        setCompatibleLists(items);
//        list.value = items;
        return  compatibleListDao.getAllCompatibleListData();
    }

    fun loadCompatibleData(){
        viewModelScope.launch {
            val result = compatibleListDao.getAllCompatibleList()
            _compatibleLists.value = result
        }
    }

    fun updateUser(compatibleList: CompatibleList) {
        viewModelScope.launch {
            compatibleListDao.update(compatibleList)
        }
    }
}
package com.bella.android_demo_public.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bella.android_demo_public.bean.CompatibleList

@Dao
interface CompatibleListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(compatibleList: CompatibleList)

    @Update
     fun update(compatibleList: CompatibleList)

    @Delete
     fun delete(compatibleList: CompatibleList)

    @Query("SELECT * FROM COMPATIBLE_LIST")
     fun getAllCompatibleList(): List<CompatibleList>

    @Query("SELECT * FROM COMPATIBLE_LIST  WHERE KEY_CODE LIKE :arg0")
     fun queryCompatibleBykeyCode(arg0: String): CompatibleList
}
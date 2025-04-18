package com.bella.android_demo_public.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bella.android_demo_public.bean.CompatibleValue

@Dao
interface CompatibleValueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(compatibleValue: CompatibleValue)

    @Update
    fun update(compatibleValue: CompatibleValue)

    @Delete
    fun delete(compatibleValue: CompatibleValue)

    @Query("SELECT * FROM COMPATIBLE_VALUE")
    fun getAllCompatibleValue(): List<CompatibleValue>

    @Query("SELECT *  FROM COMPATIBLE_VALUE   WHERE KEY_CODE LIKE :arg0   GROUP BY PACKAGE_NAME ")
    fun queryCompatibleBykeyCode(arg0: String): List<CompatibleValue>

    @Query("SELECT  * FROM COMPATIBLE_VALUE  WHERE KEY_CODE LIKE :arg0 AND PACKAGE_NAME LIKE :arg1 ")
    fun queryCompatibleBykeyCodeAndPackageName(arg0: String, arg1: String): List<CompatibleValue>

    @Query("SELECT * FROM COMPATIBLE_VALUE  WHERE KEY_CODE LIKE :arg0 AND PACKAGE_NAME LIKE :arg1 AND ACTIVITY_NAME LIKE :arg2")
    fun queryCompatibleByValue(arg0: String, arg1: String, arg2: String): CompatibleValue

}
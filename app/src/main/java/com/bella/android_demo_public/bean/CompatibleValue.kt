package com.bella.android_demo_public.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "COMPATIBLE_VALUE",
    indices = [Index(
        name = "unique_compav",
        value = ["PACKAGE_NAME", "ACTIVITY_NAME", "KEY_CODE"],
        unique = true
    )]
)
data class CompatibleValue(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_ID")
    var id: Int,

    @ColumnInfo(name = "KEY_CODE")
    var keyCode: String = "",

    @ColumnInfo(name = "PACKAGE_NAME")
    var packageName: String = "",

    @ColumnInfo(name = "ACTIVITY_NAME")
    var activityName: String = "",

    @ColumnInfo(name = "VALUE")
    var value: String = "",

    @ColumnInfo(name = "NOTES")
    var notes: String = "",

    @ColumnInfo(name = "APP_NAME")
    var appName: String = "",

    @ColumnInfo(name = "IS_ENABLE")
    var isEnable: String = "",

    @ColumnInfo(name = "CREATE_DATE")
    var createDate: String = "",

    @ColumnInfo(name = "EDIT_DATE")
    var editDate: String = "",

    @ColumnInfo(name = "IS_DEL")
    var isDel: String = "",

    @ColumnInfo(name = "FIELDS1")
    var fields1: String = "",

    @ColumnInfo(name = "FIELDS2")
    var fields2: String = "",
) {
    @Ignore
    constructor() : this(0, "", "", "", "", "", "", "", "", "", "", "", "")
}

package com.bella.android_demo_public.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "COMPATIBLE_LIST",
    indices = [Index(
        name = "unique_compa",
        value = ["KEY_CODE"],
        unique = true
    )]
)
data class CompatibleList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_ID")
    var id: Int ,

    @ColumnInfo(name = "KEY_CODE")
    var keyCode: String = "",

    @ColumnInfo(name = "KEY_DESC")
    var keyDesc: String = "",

    @ColumnInfo(name = "DEFAULT_VALUE")
    var defaultValue: String = "",

    @ColumnInfo(name = "INPUT_TYPE")
    var inputType: String = "",

    @ColumnInfo(name = "NOTES")
    var notes: String = "",

    @ColumnInfo(name = "OPTION_JSON")
    var optionJson: String = "",

    @ColumnInfo(name = "CREATE_DATE")
    var createDate: String = "",

    @ColumnInfo(name = "EDIT_DATE")
    var editDate: String = "",

    @ColumnInfo(name = "IS_DEL")
    var isDel: String = "",

    @ColumnInfo(name = "FIELDS1")
    var fields1: String = "",

    @ColumnInfo(name = "FIELDS2")
    var fields2: String = ""
){
    @Ignore
    constructor() : this(0, "", "", "","","","","","","","","")
}

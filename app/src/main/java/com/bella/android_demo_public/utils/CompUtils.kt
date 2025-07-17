package com.bella.android_demo_public.utils

import android.content.Context
import android.content.pm.LauncherActivityInfo
import android.content.pm.LauncherApps
import android.os.Build
import android.os.UserManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.bella.android_demo_public.BellaDataBase
import com.bella.android_demo_public.bean.AppData
import com.bella.android_demo_public.bean.CompatibleList
import com.bella.android_demo_public.bean.CompatibleValue
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory


object CompUtils {
    fun isChineseLanguage(context: Context): Boolean {
        val locale = context.resources.configuration.locale
        return locale.language == "zh"
    }

    fun parseEnChJson(context: Context, json: String): String {
        return try {
            val jsonObject = JSONObject(json)
            val enText = jsonObject.getString("en")
            val chText = jsonObject.getString("ch")
            if (isChineseLanguage(context)) chText else enText
        } catch (e: Exception) {
            Log.e("CompUtils", "Error parsing JSON", e)
            ""
        }
    }




    fun parseList(context: Context?, inputStream: InputStream?) {
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val document: Document = builder.parse(inputStream)
            val rootElement: Element = document.getDocumentElement()
            val nodeList: NodeList = rootElement.getElementsByTagName("compatible")
            //            LogTools.Companion.i("nodeList length: " + nodeList.getLength());
            for (i in 0 until nodeList.length) {
                val compatibleElement: Element = nodeList.item(i) as Element
                val date: String = compatibleElement.getAttribute("date")
                val isDel: String = compatibleElement.getAttribute("isdel")

                val keyCode: String =
                    compatibleElement.getElementsByTagName("keycode").item(0).getTextContent()
                val keyDesc: String =
                    compatibleElement.getElementsByTagName("keydesc").item(0).getTextContent()
                val defaultValue: String =
                    compatibleElement.getElementsByTagName("defaultvalue").item(0).getTextContent()
                val optionJson: String =
                    compatibleElement.getElementsByTagName("optionjson").item(0).getTextContent()
                val inputType: String =
                    compatibleElement.getElementsByTagName("inputtype").item(0).getTextContent()
                val notes: String =
                    compatibleElement.getElementsByTagName("notes").item(0).getTextContent()


                var item = BellaDataBase.getInstance(context).compatibleListDao()
                    .queryCompatibleBykeyCode(keyCode)


                if (item == null) {
                    LogTool.i("keycode " + keyCode + " ,keydesc " + keyDesc)
                    item = CompatibleList();
                    item.keyCode = keyCode;
                    item.keyDesc = keyDesc;
                    item.notes = notes;
                    item.isDel = isDel;
                    item.editDate = date;
                    item.defaultValue = defaultValue;
                    item.inputType = inputType;
                    item.optionJson = optionJson;
                    item.createDate = Utils.getCurDateTime();
                    BellaDataBase.getInstance(context).compatibleListDao().insert(item)
                } else {
                    LogTool.i("item    is not  null " + date)
                    if (!date.equals(item.editDate)) {
                        item.keyDesc = keyDesc;
                        item.notes = notes;
                        item.isDel = isDel;
                        item.editDate = date;
                        item.defaultValue = defaultValue;
                        item.inputType = inputType;
                        item.optionJson = optionJson;
                        BellaDataBase.getInstance(context).compatibleListDao().update(item)
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun parseValue(context: Context?, inputStream: InputStream?) {
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val document = builder.parse(inputStream)
            val rootElement = document.documentElement
            val itemList = rootElement.getElementsByTagName("item")
            for (i in 0 until itemList.length) {
                val keycodeElement = itemList.item(i) as Element
                val updateDate = keycodeElement.getAttribute("update_date")
                val isDel = keycodeElement.getAttribute("isdel")
                val keyCode = keycodeElement.getAttribute("key_code")
                val packageList = keycodeElement.getElementsByTagName("package")
                for (j in 0 until packageList.length) {
                    val packageElement = packageList.item(j) as Element
                    val packageName = packageElement.getAttribute("name")
                    val activityList = packageElement.getElementsByTagName("activity")
                    for (k in 0 until activityList.length) {
                        val activityElement = activityList.item(k) as Element
                        val activityName = activityElement.getAttribute("name")
                        val defaultValue = activityElement.textContent.replace("\\s".toRegex(), "")
                        LogTool.w("keyCode: $keyCode ,packageName: $packageName ,activityName: $activityName ,defaultValue: $defaultValue")
                        var item = BellaDataBase.getInstance(context).compatibleValueDao()
                            .queryCompatibleByValue(keyCode, packageName, activityName)

                        if (item == null) {
                            item = CompatibleValue();
                            item.keyCode = keyCode;
                            item.packageName = packageName;
                            item.activityName = activityName;
                            item.value = defaultValue;
                            item.isDel = isDel;
                            item.createDate = Utils.getCurDateTime();
                            BellaDataBase.getInstance(context).compatibleValueDao().insert(item)
                        } else {
                            if (!updateDate.equals(item.editDate)) {
                                item.keyCode = keyCode;
                                item.packageName = packageName;
                                item.activityName = activityName;
                                item.value = defaultValue;
                                item.isDel = isDel;
                                item.editDate = updateDate;
                                BellaDataBase.getInstance(context).compatibleValueDao().update(item)
                            }
                        }
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun getAllApps(context: Context): List<AppData> {
        val list: MutableList<AppData> = ArrayList()
        try {
            val launcherApps =
                context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
            val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
            val userHandles = userManager.userProfiles
            val activityInfoList: MutableList<LauncherActivityInfo> = ArrayList()
            for (userHandle in userHandles) {
                activityInfoList.addAll(launcherApps.getActivityList(null, userHandle))
            }

            for (info in activityInfoList) {
                val appData = AppData()
                appData.name = StringUtils.ToString(info.label)
                appData.packageName = StringUtils.ToString(info.componentName.packageName)
                appData.icon = info.getIcon(0)
                appData.componentName = info.componentName
                list.add(appData)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun getAppInfo(context: Context?, packageName: String): AppData? {
        val appList: List<AppData?> = getAllApps(context!!)
        val result = appList.stream()
            .filter { appData: AppData? -> appData!!.packageName == packageName }
            .findFirst()
        return result.orElse(null)
    }

    fun setSystemProperty(key: String, value: String) {
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val setMethod = systemPropertiesClass.getDeclaredMethod(
                "set",
                String::class.java,
                String::class.java
            )
            setMethod.invoke(null, key, value)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurDateTime(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedTime = currentTime.format(formatter)
        return formattedTime
    }

     fun parseJson(jsonArrayString: String): Array<String>? {
        try {
            val jsonArray = JSONArray(jsonArrayString)
            val stringArray = Array(jsonArray.length()) { "" }
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i).toString()
                stringArray[i] = jsonObject
            }
            return stringArray
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

     fun jsonToMap(jsonObject: JSONObject): Map<String, Any>? {
        try {
            val map: MutableMap<String, Any> = HashMap()
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = jsonObject[key]
                map[key] = value
            }
            return map
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

}
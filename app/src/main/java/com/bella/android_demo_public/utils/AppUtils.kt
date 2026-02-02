package com.bella.android_demo_public.utils

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.bella.android_demo_public.bean.AppData

object AppUtils {

    public fun uninstallApp(mContext: Context, packageName:String) {
        val packageUri = Uri.parse("package:${packageName}")
        val uninstallIntent = Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri)
        mContext?.startActivity(uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    @SuppressLint("SoonBlockedPrivateApi")
    public fun createShortcut(mContext: Context, packageName:String,name:String) {
//        Log.d(AllAppsWindow.TAG, "createShortcut() called with: app = [${app.name}]")
//        val icon = Icon.createWithBitmap(drawableToBitmap(app.icon!!))
        val shortcutManager: ShortcutManager? =
            mContext?.getSystemService(ShortcutManager::class.java)
        if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported) {
            val launchIntentForPackage: Intent = mContext?.getPackageManager()
                ?.getLaunchIntentForPackage(packageName) as Intent
            launchIntentForPackage.action = Intent.ACTION_MAIN
            val pinShortcutInfo = ShortcutInfo.Builder(mContext, name)
                .setLongLabel(name)
                .setShortLabel(name)
//                .setIcon(icon)
                .setIntent(launchIntentForPackage)
                .build()
            val pinnedShortcutCallbackIntent =
                shortcutManager.createShortcutResultIntent(pinShortcutInfo)
            val successCallback = PendingIntent.getBroadcast(
                mContext, 0,
                pinnedShortcutCallbackIntent, PendingIntent.FLAG_IMMUTABLE
            )
            shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.intentSender)
        }
    }


    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        val bitmap = Bitmap.createBitmap(
            width,
            height,
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        )
        val canvas = Canvas(bitmap)
        //canvas.drawColor(0xff33B5E5);
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
        return bitmap
    }

}
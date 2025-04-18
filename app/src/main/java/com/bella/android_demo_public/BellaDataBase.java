package com.bella.android_demo_public;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bella.android_demo_public.bean.CompatibleList;
import com.bella.android_demo_public.bean.CompatibleValue;
import com.bella.android_demo_public.bean.RegionInfo;
import com.bella.android_demo_public.bean.User;
import com.bella.android_demo_public.dao.CompatibleListDao;
import com.bella.android_demo_public.dao.CompatibleValueDao;
import com.bella.android_demo_public.dao.RegionDao;
import com.bella.android_demo_public.dao.UserDao;

@Database(entities = {User.class, RegionInfo.class, CompatibleList.class, CompatibleValue.class}, version = 1, exportSchema = false)
public abstract class BellaDataBase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract RegionDao regionDao();

    public abstract CompatibleListDao compatibleListDao();

    public abstract CompatibleValueDao compatibleValueDao();

    private static BellaDataBase instance;

    public static synchronized BellaDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                            BellaDataBase.class, "bella_database.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
